package com.blog.model;

import com.blog.exception.UserDoesNotExistException;
import com.blog.exception.UserIsDeletedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.blog.model.UserLevel.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitTests {
    private User guest;

    @BeforeEach
    void beforeEach() {
        try {
            guest = new User("1");
        } catch (UserIsDeletedException e) {
            throw new RuntimeException(e);
        } catch (UserDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void guestConstructor() {
        assertEquals(0, guest.getUserID());
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
    }

    @Test
    void userIDSet() {
        assertEquals(0, guest.getUserID());
        // guest.setUserID(1);
        assertEquals(1, guest.getUserID());
    }

    @Test
    void userNameSet() {
        // TODO: guest.getUsername()
        guest.setUsername("Guest");
        assertEquals("Guest", guest.getUsername());
    }

    @Test
    void guestToOtherLevels() {
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
        guest.setUserLevel(READER);
        assertEquals(UserLevel.GUEST, guest.getUserLevel());
        guest.setUserLevel(CONTRIBUTOR);
        assertEquals(UserLevel.CONTRIBUTOR, guest.getUserLevel());
        guest.setUserLevel(ADMIN);
        assertEquals(UserLevel.ADMIN, guest.getUserLevel());
    }

    @Test
    void userCreateionDateSet() {
        // TODO:
        // guest.setCreationDate("");
        // assertEquals("", guest.getCreationDate());
    }

    @Test
    void userLastLoginSet() {
        // TODO:
        // guest.setCreationDate("");
        // assertEquals("", guest.getCreationDate());
    }

    @Test
    void guestToOtherStatus() {
        guest.setUserStatus(UserStatus.AWAY);
        assertEquals(UserStatus.AWAY, guest.getUserStatus());
        guest.setUserStatus(UserStatus.BUSY);
        assertEquals(UserStatus.BUSY, guest.getUserStatus());
        guest.setUserStatus(UserStatus.OFFLINE);
        assertEquals(UserStatus.OFFLINE, guest.getUserStatus());
        guest.setUserStatus(UserStatus.ONLINE);
        assertEquals(UserStatus.ONLINE, guest.getUserStatus());
    }

    @Test
    void userProfilePictureSet() {
        // TODO:
        guest.setProfilePicture("");
        assertEquals("", guest.getProfilePicture());
    }

}