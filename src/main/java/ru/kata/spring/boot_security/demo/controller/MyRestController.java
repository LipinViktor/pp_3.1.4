package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.Service;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private Service service;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = service.allUsers();
        return allUsers != null && !allUsers.isEmpty()?
                new ResponseEntity<>(allUsers, HttpStatus.OK):
                new ResponseEntity<>(allUsers, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User userFromDb = service.findUserById(id);
        return userFromDb != null? new ResponseEntity<>(userFromDb, HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        service.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        service.updateUser(user);
        return service.findUserById(user.getId()) != null?
        new ResponseEntity<>(HttpStatus.OK):
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> removeUser(@PathVariable long id) {
        service.deleteUser(id);
        return service.findUserById(id) == null?
        new ResponseEntity<>(HttpStatus.OK):
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
