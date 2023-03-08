package com.blog.database;

import org.junit.jupiter.api.Test;

import com.blog.model.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    @Test
    void testRetrieveUser() {
        User guest = new User();
        guest.setUserID(1);
        Database.retrieve(guest);
        
        assertEquals(1, guest.getUserID());
        assertEquals("test", guest.getUsername());
    }
}