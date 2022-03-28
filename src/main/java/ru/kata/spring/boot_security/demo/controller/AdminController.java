package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final Service service;

    @Autowired
    public AdminController(Service service) {
        this.service = service;
    }

    @PostConstruct
    public void addUser() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        service.saveRole(roleAdmin);
        service.saveRole(roleUser);
        User user = new User("Ivan","Ivanov",23,"ivanov");
        user.setRoles(service.allRoles().stream()
                .filter(e -> e.getName().equals("ROLE_ADMIN"))
                .collect(Collectors.toList()));
        service.saveUser(user);
    }

    @GetMapping()
    public String index(@AuthenticationPrincipal User userPrinc, Model model) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Role role : userPrinc.getRoles()) {
            stringBuffer.append(role.getName().substring(5));
            stringBuffer.append(" ");
        }
        model.addAttribute("surName", userPrinc.getSurName());
        model.addAttribute("rolesUser", stringBuffer);
        return "admin";
    }

    @PostMapping()
    public String create(@ModelAttribute("user")  User user) {
        service.saveUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user1") User user) {
        service.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }
}
