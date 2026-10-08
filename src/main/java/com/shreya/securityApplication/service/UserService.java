package com.shreya.securityApplication.service;

import com.shreya.securityApplication.dto.SignUpDTO;
import com.shreya.securityApplication.dto.UserDTO;
import com.shreya.securityApplication.entity.UserEntity;
import com.shreya.securityApplication.exception.ResourceNotFoundException;
import com.shreya.securityApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new BadCredentialsException("User with email "+username+" not found"));
    }

    public UserEntity getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with id "+userId+" not found."));
    }

    public UserDTO signUp(SignUpDTO signUpDTO) {
       Optional<UserEntity> userEntity= userRepository.findByEmail(signUpDTO.getEmail());
       if(userEntity.isPresent())
           throw new BadCredentialsException("User with email already exists "+signUpDTO.getEmail());

       UserEntity userToBeCreatedUser = modelMapper.map(signUpDTO, UserEntity.class);
       userToBeCreatedUser.setPassword(passwordEncoder.encode(userToBeCreatedUser.getPassword()));
       UserEntity savedUser = userRepository.save(userToBeCreatedUser);

       return modelMapper.map(savedUser, UserDTO.class);
    }


    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public Object save(UserEntity newUser) {
        return userRepository.save(newUser);
    }
}
