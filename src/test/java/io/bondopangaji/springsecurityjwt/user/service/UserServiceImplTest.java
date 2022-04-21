package io.bondopangaji.springsecurityjwt.user.service;

import io.bondopangaji.springsecurityjwt.error.CustomException;
import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.repository.RoleRepository;
import io.bondopangaji.springsecurityjwt.user.repository.UserRepository;
import io.bondopangaji.springsecurityjwt.user.service.UserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setName("Bondo");
        user.setUsername("bondo");
        user.setEmail("bondo@gmail.com");
        user.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals("Bondo", result.getName());
        assertEquals("bondo", result.getUsername());
        assertEquals("bondo@gmail.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void testSaveRole() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = userService.saveRole(role);

        assertEquals("ROLE_ADMIN", result.getName());
    }

    @Test
    public void testSaveRoleToUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Bondo");
        user.setUsername("bondo");
        user.setEmail("bondo@gmail.com");
        user.setPassword("password");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_ADMIN");

        when(userRepository.findByUsername("bondo")).thenReturn(user);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(role);

        userService.saveRoleToUser("bondo", "ROLE_ADMIN");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveRoleToUserUserNotFound() {
        String username = "username";
        String roleName = "roleName";

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.saveRoleToUser(username, roleName));
    }

    @Test
    public void testSaveRoleToUserRoleNotFound() {
        String username = "admin";
        String roleName = "ADMIN";

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);
        when(roleRepository.findByName(roleName)).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.saveRoleToUser(username, roleName));
    }


    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setId(1L);
        user.setName("Bondo");
        user.setUsername("bondo");
        user.setEmail("bondo@gmail.com");
        user.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        User result = userService.getUserByUsername("bondo");

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("admin")).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.getUserByUsername("admin"));
    }

    @Test
    public void testGetUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User().setId(1L).setName("Bondo").setUsername("bondo").setEmail("bondo@gmail.com"));
        users.add(new User().setId(2L).setName("Bondo").setUsername("bondo").setEmail("bondo@gmail.com"));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.getRoles().add(role);

        when(userRepository.findByUsername("test")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("test");

        assertEquals("test", userDetails.getUsername());
        assertEquals("test", userDetails.getPassword());
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        when(userRepository.findByUsername("admin")).thenReturn(null);

        assertThrows(CustomException.class, () -> userService.loadUserByUsername("admin"));
    }
}