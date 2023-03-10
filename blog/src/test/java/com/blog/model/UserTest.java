package com.blog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.blog.model.UserLevel.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private User guest0;

    @BeforeEach
    void beforeEach() {
        guest0 = new User();
    }

    @Test
    void guestConstructor() {
        assertEquals(0, guest0.getUserID());
        assertEquals(UserLevel.GUEST, guest0.getUserLevel());
    }

    @Test
    void guestToOtherRoles() {
        assertEquals(UserLevel.GUEST, guest0.getUserLevel());
        guest0.setUserLevel(READER);
        assertEquals(UserLevel.GUEST, guest0.getUserLevel());
        guest0.setUserLevel(CONTRIBUTOR);
        assertEquals(UserLevel.CONTRIBUTOR, guest0.getUserLevel());
        guest0.setUserLevel(ADMIN);
        assertEquals(UserLevel.ADMIN, guest0.getUserLevel());
    }
}