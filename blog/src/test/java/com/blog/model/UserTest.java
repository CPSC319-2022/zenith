package com.blog.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void guestConstructor() {
        User guest = new User();

        assertEquals(0, guest.getUserID());
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
    }
}