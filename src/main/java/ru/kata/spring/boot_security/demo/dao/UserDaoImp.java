package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Set<Role> allRoles;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT us FROM User us", User.class).getResultList();
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public void save(User user) {
        if (user.getRoles() == null) {
            user.setRoles(allRoles.stream().filter(n -> n.getName().contains("USER")).collect(Collectors.toList()));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        em.persist(user);
    }

    @Override
    public void update(long id, User user) {
        user.setId(id);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        em.merge(user);
    }

    @Override
    public void delete(long id) {
        em.createQuery("DELETE FROM User WHERE id=:id").setParameter("id", id).executeUpdate();
    }

    @Override
    public User findByUserName(String name) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.name=:username", User.class);
        User user = query.setParameter("username", name).getSingleResult();
        return user;
    }
}
