package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.repositories.UserRepositories;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserDetailsService {

    private final UserRepositories userRepositories;

    @Autowired
    public UserServiceImpl(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }


    @Transactional
    public void updateUser(int id, User user) {
        user.setId(id);
        userRepositories.save(user);
    }

    @Transactional
    public User getUserAtId(Integer id) {
        Optional<User> findUser = userRepositories.findById(id);
        return findUser.orElse(null);
    }

    @Transactional
    public void saveUser(User user) {
        userRepositories.save(user);
    }

    @Transactional
    public void removeUserById(Integer id) {
        userRepositories.delete(getUserAtId(id));
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepositories.findAll();
    }

    @Transactional
    public User findByName(String name) {
        return userRepositories.findByName(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails loadedUser;

        User user = findByName(username);
        if (user==null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            loadedUser = new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),
                    user.getRoles());
        }
        return loadedUser;
    }
}
