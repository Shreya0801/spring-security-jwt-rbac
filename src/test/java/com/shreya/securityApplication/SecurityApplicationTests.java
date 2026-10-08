package com.shreya.securityApplication;

import com.shreya.securityApplication.entity.UserEntity;
import com.shreya.securityApplication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {

		UserEntity userEntity = new UserEntity(4L, "shalu@gmail.com","1234");
		String token = jwtService.generateToken(userEntity);
		System.out.println(token);

		Long id = jwtService.getUserIdFromToken(token);
		System.out.println(id);

	}

}
