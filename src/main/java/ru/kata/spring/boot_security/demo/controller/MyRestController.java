package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exceptionhandling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> allUsers = userService.findAll();
        return allUsers;
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        User userFromDb = userService.findById(id);
        if (userFromDb == null) {
            throw new NoSuchUserException(
                    "User with id = " + id + " not found in DataBase!!!");
        }
        return userFromDb;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userService.save(user);
        return userService.findById(user.getId());
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        System.out.println("!!!!!Я ПУТ_РЕСТ И Я ВКЛЮЧИЛСЯ!!!!!"+user);
        userService.update(user.getId(), user);
        return userService.findById(user.getId());
    }

    @DeleteMapping("/users/{id}")
    public void removeUser(@PathVariable long id) {
        userService.delete(id);
    }
}
