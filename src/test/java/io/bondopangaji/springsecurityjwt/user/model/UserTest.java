package io.bondopangaji.springsecurityjwt.user.model;

import io.bondopangaji.springsecurityjwt.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    public void testEquals() {
        User user1 = new User();
        user1.setId(1L);
        user.setId(1L);

        assertEquals(user, user1);
    }

    @Test
    public void testEqualsIdIsNull() {
        User user2 = new User();
        assertNotEquals(user, user2);
    }

    @Test
    public void testEqualsClassesAreDifferent() {
        assertNotEquals(user, new Object());
    }

    @Test
    public void testHashCode() {
        assertEquals(user.getClass().hashCode(), user.hashCode());
    }
}