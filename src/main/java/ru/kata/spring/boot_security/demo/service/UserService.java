package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles);

    void createUser(User user);
    User readUserById(Integer id);
    List<User> readAllUsers();
    void updateUser(Integer id, User user);
    void deleteUser(Integer id);

    boolean isUserRole(String username, String roleName);

}