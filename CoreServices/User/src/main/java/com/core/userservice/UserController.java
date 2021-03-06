package com.core.userservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class UserController {

  @Autowired
  UserRepository userRepository;

  private List<User> userCache = new ArrayList<User>();

  // Configure access for administrators only.
  @PreAuthorize("hasRole('ADMIN')")
  @HystrixCommand(fallbackMethod = "getUsersCache")
  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    try {
      List<User> users = new ArrayList<User>();
      userRepository.findAll().forEach(users::add);
      userCache.clear();
		  users.forEach(u -> userCache.add(u));
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Configure access for administrators only.
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<List<User>> getUsersCache() {
    return new ResponseEntity<>(userCache, HttpStatus.OK);
  } 

  // Configure access for authorized applications or administrators.
  @PreAuthorize("hasAuthority('UserCoreAccessCreateUser') or hasRole('ADMIN')")
  @HystrixCommand
  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    try {
      User userData = userRepository.findByUsername(user.getUsername());
      if(userData != null){
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
      }else{
        User _user = userRepository.save(new User(user.getUsername(), user.getFirstname(), user.getLastname(), user.getPassword(), user.getRole()));
        return new ResponseEntity<>(_user, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Configure access for administrators only.
  @PreAuthorize("hasRole('ADMIN')")
  @HystrixCommand
  @DeleteMapping("/users")
	public ResponseEntity<HttpStatus> deleteAllUsers() {
		try {
			userRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

/*   @HystrixCommand
  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      return new ResponseEntity<>(userData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  } */

  // Configure access for authorized applications or administrators or users restricted to their own user account.
  @PreAuthorize("hasAuthority('UserCoreAccessGetUserByName') or hasRole('ADMIN') or (hasRole('USER') and #username == principal)")
  @HystrixCommand
  @GetMapping("/users/{username}")
  public ResponseEntity<User> getUserByName(@PathVariable("username") String username) {
   // Optional<User> userData = userRepository.findById(id);
    User user = userRepository.findByUsername(username);
    if (user != null) {
      return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // Configure access for administrators or users restricted to their own user account.
  @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #username == principal)")
  @HystrixCommand
  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(
    @PathVariable("id") int id,
    @RequestBody User user
  ) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      User _user = userData.get();
      _user.setUsername(user.getUsername() == null ? _user.getUsername() : user.getUsername());
      _user.setFirstname(user.getFirstname() == null ? _user.getFirstname() : user.getFirstname());
      _user.setLastname(user.getLastname() == null ? _user.getLastname() :  user.getLastname());
      _user.setPassowrd(user.getPassword() == null ? _user.getPassword() : user.getPassword());
      return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // Configure access for administrators or users restricted to their own user account.
  @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #username == principal)")
  @HystrixCommand
  @DeleteMapping("/users/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
    try {
      userRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
