package com.core.userservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
 // List<User> findByPublished(String firstname);
  //List<User> findByTitleContaining(String firstname);
}