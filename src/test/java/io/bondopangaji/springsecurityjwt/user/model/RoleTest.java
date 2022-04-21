package io.bondopangaji.springsecurityjwt.user.model;

import io.bondopangaji.springsecurityjwt.user.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    public void testEquals() {
        Role role1 = new Role();
        role1.setId(1L);
        role.setId(1L);

        assertEquals(role, role1);
    }

    @Test
    public void testEqualsIdIsNull() {
        Role role2 = new Role();
        assertNotEquals(role, role2);
    }

    @Test
    public void testEqualsObjectIsNull() {
        assertNotEquals(null, role);
    }

    @Test
    public void testHashCode() {
        assertEquals(role.getClass().hashCode(), role.hashCode());
    }
}