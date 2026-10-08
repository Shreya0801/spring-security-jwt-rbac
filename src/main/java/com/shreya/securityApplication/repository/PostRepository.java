package com.shreya.securityApplication.repository;

import com.shreya.securityApplication.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
