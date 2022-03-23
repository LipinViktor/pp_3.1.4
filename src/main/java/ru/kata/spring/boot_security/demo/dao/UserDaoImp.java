package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
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
        System.out.println("++++++++Это Сэйв ДАО");
        String result;
        TypedQuery<String> str;
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        em.persist(user);
        User userDb = findByUserName(user.getName());
        Collection<Role> roles = user.getRoles();
        for (Role r : roles) {
            if (r.getName().contains("ROLE_ADMIN")) {
                result = String.format("INSERT INTO viktorconnect.user_roles (user_id, role_id) VALUES (%d, 1)", userDb.getId());
                int a = em.createNativeQuery(result).executeUpdate();
            } else {
                result = String.format("INSERT INTO viktorconnect.user_roles (user_id, role_id) VALUES (%d, 2)", userDb.getId());
                int b = em.createNativeQuery(result).executeUpdate();
                System.out.println(b);
            }
        }

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
