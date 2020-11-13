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
    UserRepository tutorialRepository;

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        //console.log("I am in greeting");
       // return new User(counter.incrementAndGet(),
       tutorialRepository.save(new User((int)counter.incrementAndGet(), "username","firstname", "lastname", "password", Role.values()[0]));
        return new User((int)counter.incrementAndGet(), 
            String.format(template, name), "firstname", "lastname", "password", Role.values()[0]);
    }
    
    @PostMapping("/tutorials")
    public ResponseEntity<User> createUser(@RequestBody User tutorial) {
      try {
        User _tutorial = tutorialRepository.save(new User((int)counter.incrementAndGet(), "username","firstname", "lastname", "password", Role.values()[0]));
        return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
}