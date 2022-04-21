package io.bondopangaji.springsecurityjwt.user.repository;

import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.repository.RoleRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void testFindByName() {
        Role role = new Role();
        role.setName("ADMIN");

        when(roleRepository.findByName("ADMIN")).thenReturn(role);

        Role result = roleRepository.findByName("ADMIN");

        assertEquals("ADMIN", result.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        when(roleRepository.findByName("admin")).thenReturn(null);

        Role role = roleRepository.findByName("admin");

        assertNull(role);
    }
}