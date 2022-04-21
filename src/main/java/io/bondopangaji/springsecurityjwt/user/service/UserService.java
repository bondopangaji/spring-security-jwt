/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.user.service;

import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;

import java.util.List;

/**
 * Business logic interface.
 * @author Bondo Pangaji
 */
public interface UserService {

    /**
     * Add new user.
     */
    User saveUser(User user);

    /**
     * Add new role.
     */
    Role saveRole(Role role);

    /**
     * Add new role to user.
     */
    void saveRoleToUser(String username, String roleName);

    /**
     * Fetch user by username.
     */
    User getUserByUsername(String username);

    /**
     * Fetch all user.
     */
    List<User> getUsers();

}
