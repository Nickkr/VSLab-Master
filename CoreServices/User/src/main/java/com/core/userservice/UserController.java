package com.core.userservice;

import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:18082")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        //console.log("I am in greeting");
       // return new User(counter.incrementAndGet(),
       userRepository.save(new User((int)counter.incrementAndGet(), "username","firstname", "lastname", "password", Role.values()[0]));
        return new User((int)counter.incrementAndGet(), 
            String.format(template, name), "firstname", "lastname", "password", Role.values()[0]);
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
      try {
        // TODO: Check, if user already exists
        // TODO: SQL injection??
        // TODO: Rollentyp abfragen und entsprechend setzen oder immer User setzen und Ändern nur mit späteren Post möglich?
        
        User _user = userRepository.save(new User((int)counter.incrementAndGet(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getPassword(), Role.USER));
        return new ResponseEntity<>(_user, HttpStatus.CREATED);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
      Optional<User> userData = userRepository.findById(id);
  
      if (userData.isPresent()) {
        return new ResponseEntity<>(userData.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      User _user = userData.get();
      _user.setUsername(user.getUsername());
      _user.setFirstname(user.getFirstname());
      _user.setLastname(user.getLastname());
      _user.setPassowrd(user.getPassword());
      return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}