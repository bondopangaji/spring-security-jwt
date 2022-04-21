/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.user.service;

import io.bondopangaji.springsecurityjwt.error.CustomException;
import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.repository.RoleRepository;
import io.bondopangaji.springsecurityjwt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Business logic implementation.
 * @author Bondo Pangaji
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * Dependency injection using lombok @RequiredArgsConstructor.
     * @see RequiredArgsConstructor
     */
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) throws ConstraintViolationException {
        log.info("Add new user `{}`", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Add new role `{}`", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void saveRoleToUser(String username, String roleName) {
        log.info("Add role `{}` to user `{}`", roleName, username);

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        if (user == null) {
            throw new CustomException("User not found!");
        }

        if (role == null) {
            throw new CustomException("Role not found!");
        }

        user.getRoles().add(role);

        userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("Fetch user by username `{}`", username);

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new CustomException("User not found!");
        }

        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetch all users");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (user == null) {
            log.error("User `{}` not found in the database!", username);
            throw new CustomException("User `" + username + "` not found in the database!");
        }

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

}
