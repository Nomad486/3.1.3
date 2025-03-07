package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImp(UserRepository userRepository,
                          RoleService roleService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void createUser(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(roleService.findByName("ROLE_USER"));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User readUserById(Integer id) {
        User user = userRepository.findById(id);
        return (user != null) ? user : new User();
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void updateUser(Integer id, User user) {
        try {
            User user0 = readUserById(id);
            user0.setUsername(user.getUsername());
            user0.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user0.setRoles(user.getRoles());
            userRepository.save(user0);
        } catch (NullPointerException e) {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUserRole(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

}