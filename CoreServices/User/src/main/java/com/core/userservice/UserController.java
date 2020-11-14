package com.core.userservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:18082")
@RestController
public class UserController {

  @Autowired
  UserRepository userRepository;

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    try {
      List<User> users = new ArrayList<User>();
      userRepository.findAll().forEach(users::add);
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    try {
      // TODO: Check, if user already exists
      // TODO: SQL injection??
      // TODO: Rollentyp abfragen und entsprechend setzen oder immer User setzen und Ändern nur mit späteren Post möglich?

      List<User> userData = userRepository.findByUsername(user.getUsername());
      if(!userData.isEmpty()){
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
      }else{
        User _user = userRepository.save(
          new User(
            (int) counter.incrementAndGet(), user.getUsername(),
            user.getFirstname(),
            user.getLastname(),
            user.getPassword(),
            Role.USER
          )
        );
        return new ResponseEntity<>(_user, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/users")
	public ResponseEntity<HttpStatus> deleteAllUsers() {
		try {
			userRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
  public ResponseEntity<User> updateUser(
    @PathVariable("id") int id,
    @RequestBody User user
  ) {
    Optional<User> userData = userRepository.findById(id);

    // TODO: prüfen, ob alle Variablen besetzt sind?
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

  @DeleteMapping("/users/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
    System.out.println("I'm here in delete.");
    System.out.println("The id is " + id);
    System.out.println(((Object) id).getClass().getName());
    //userRepository.findById(id);
    int test = id;
    // userRepository.deleteById(test);
    //Optional<User> userData = userRepository.findById(id);
    // userRepository.delete(userData);
    //userRepository.deleteById();

    // userRepository.deleteAll();

    try {
      userRepository.deleteById(id);
      System.out.println("User deleted");
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {

      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
