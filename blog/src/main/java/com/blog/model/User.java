package com.blog.model;

import java.time.Clock;

public class User {

    private Integer user_ID;

    private String username;

    private Integer password;

    private String avatarPath;

    private String email;

    private Clock creationDate;

    private Clock lastLogin;

    private boolean ban_status;

    private boolean contributor;

    private boolean administrator;
}
