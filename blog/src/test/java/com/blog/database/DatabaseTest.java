package com.blog.database;

import org.junit.jupiter.api.Test;

import com.blog.model.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    @Test
    void testRetrieveUser() {
        User guest = new User(1);
        Database.retrieve(guest);
        
        assertEquals(1, guest.getUserID());
        assertEquals("2023-03-01 01:02:03", guest.getCreationDate());
        assertEquals("2023-03-01 01:02:03", guest.getLastLogin());
        assertEquals(UserLevel.READER, guest.getUserLevel());
        assertEquals("user1", guest.getUsername());
        assertEquals(false, guest.isDeleted());
    }
}