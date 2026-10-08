package com.shreya.securityApplication.config;

import com.shreya.securityApplication.entity.enums.Permission;
import com.shreya.securityApplication.filter.JwtAuthFilter;
import com.shreya.securityApplication.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;



import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.shreya.securityApplication.entity.enums.Permission.POST_DELETE;
import static com.shreya.securityApplication.entity.enums.Role.ADMIN;
import static com.shreya.securityApplication.entity.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private static final String[] publicRoutes = {"/error", "/auth/**", "/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(publicRoutes).permitAll()
                        .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/posts/**").hasAnyRole(ADMIN.name(), CREATOR.name())
                        .requestMatchers(HttpMethod.POST, "/posts/**").hasAuthority(Permission.POST_CREATE.name())
                        //This is linr is conflicting the the above GET method code ..
//                        .requestMatchers(HttpMethod.GET,"/posts/**").hasAuthority(Permission.POST_VIEW.name())
                        .requestMatchers(HttpMethod.PUT,"/posts/**").hasAuthority(Permission.POST_UPDATE.name())
                        //we can also do the above by importing them statically like we have done just below.
                        .requestMatchers(HttpMethod.DELETE,"/posts/**").hasAuthority(POST_DELETE.name())
//                        .requestMatchers("/posts/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .csrf(csrfConfig-> csrfConfig.disable())
                .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config-> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler)
                );
//                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }

//    @Bean
//    UserDetailsService inMemoryUserDetailsService(){
//        UserDetails normalUser = User.withUsername("shreya")
//                .password(passwordEncoder().encode("shreya123"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User.withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }
//

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }


}
