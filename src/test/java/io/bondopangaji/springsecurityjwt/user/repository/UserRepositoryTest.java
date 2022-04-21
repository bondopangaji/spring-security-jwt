package io.bondopangaji.springsecurityjwt.user.repository;

import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("bondo");

        when(userRepository.findByUsername("bondo")).thenReturn(user);

        User result = userRepository.findByUsername("bondo");

        assertEquals(user, result);
    }

    @Test
    public void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("bondo")).thenReturn(null);

        User user = userRepository.findByUsername("bondo");

        assertNull(user);
    }
}