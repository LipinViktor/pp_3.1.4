package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.Service;

@Controller
public class UserController {

    private final Service service;

    @Autowired
    public UserController(Service service) {
        this.service = service;
    }

    @GetMapping("/user")
    public String showUser(@AuthenticationPrincipal User user, Model model) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Role role : user.getRoles()) {
            stringBuffer.append(role.getName().substring(5));
            stringBuffer.append(" ");
        }
        model.addAttribute("principalRoles", stringBuffer);
        model.addAttribute("user", user);
        return "user";
    }
}
