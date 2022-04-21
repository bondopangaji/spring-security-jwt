/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt;

import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

/**
 * Bootstrap Application
 * @author Bondo Pangaji
 */
@SpringBootApplication
public class SpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }

    @ConditionalOnProperty(
            prefix = "command.line.runner",
            value = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ADMIN"));
            userService.saveRole(new Role(null, "USER"));

            userService.saveUser(new User(
                    null,
                    "John Doe",
                    "john123",
                    "johndoe@mail.com",
                    "password",
                    new ArrayList<>()
            ));

            userService.saveUser(new User(
                    null,
                    "Jane Doe",
                    "jane123",
                    "janedoe@mail.com",
                    "password",
                    new ArrayList<>()
            ));

            userService.saveRoleToUser("john123", "ADMIN");
            userService.saveRoleToUser("jane123", "USER");
        };
    }

}
