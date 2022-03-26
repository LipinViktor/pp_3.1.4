package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface Service {

    List<User> allUsers();

    List<Role> allRoles();

    User findUserById(Long id);

    Role findRoleByName(String name);

    void saveUser(User user);

    void saveRole(Role role);

    void updateUser(User user);

    void deleteUser(Long id);
}
