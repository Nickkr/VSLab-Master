package com.core.userservice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
  User findByUsername(String username);
  //List<User> findByTitleContaining(String firstname);
 // List<User> deleteById(int id);

}