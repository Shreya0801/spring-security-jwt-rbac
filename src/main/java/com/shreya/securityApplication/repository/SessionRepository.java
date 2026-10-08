package com.shreya.securityApplication.repository;

import com.shreya.securityApplication.entity.Session;
import com.shreya.securityApplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUser(UserEntity user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
