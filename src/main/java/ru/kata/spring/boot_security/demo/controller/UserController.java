package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void addUser() {
        User user = new User();
        user.setName("Ivan");
        user.setSurName("Ivanov");
        user.setAge(24);
        user.setPassword("ivanov");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role("ROLE_ADMIN"));
        //roleSet.add(new Role("ROLE_USER"));
        user.setRoles(roleSet);
        userService.save(user);
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(@ModelAttribute("user") User user, Principal principal, Model model) {
        User userPrinc = userService.findByUserName(principal.getName());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("princ", userPrinc);
        return "index";
    }

    @PostMapping()
    public String create(@ModelAttribute("user")  User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") User user, @PathVariable("id") int id) {
        userService.update(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
