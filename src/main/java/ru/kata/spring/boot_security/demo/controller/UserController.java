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
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    private Set<Role> allRoles;

    @PostConstruct
    public void addUser() {
        User user = new User("Ivan","Ivanov",25,"ivanov");
        user.setRoles(allRoles.stream().filter(n->n.getName().contains("ADMIN")).collect(Collectors.toList()));
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
        model.addAttribute("roles", allRoles);
        return "index";
    }

    @PostMapping()
    public String create(@ModelAttribute("user")  User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user1") User user, @PathVariable("id") long id) {
        userService.update(id, user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
