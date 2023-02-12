CREATE TABLE Users(
    user_ID INTEGER,
    user_password CHAR(32) NOT NULL,
    username CHAR(32) NOT NULL,
    avatar VARCHAR(250) DEFAULT NULL,
    ban_status BOOLEAN NOT NULL DEFAULT false,
    contributor BOOLEAN NOT NULL DEFAULT false,
    administrator BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(user_ID)
);

CREATE TABLE Post(
    post_ID INTEGER,
    user_ID INTEGER,
    thumbnail VARCHAR(250),
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    post_time TIMESTAMP NOT NULL,
    PRIMARY KEY(post_ID),
    FOREIGN KEY(user_ID) REFERENCES Users(user_ID) ON DELETE CASCADE
);

CREATE TABLE Comment(
    post_ID INTEGER,
    comment_number INTEGER,
    user_ID INTEGER,
    content VARCHAR(10000) NOT NULL,
    comment_time TIMESTAMP NOT NULL,
    PRIMARY KEY(post_ID, comment_number),
    FOREIGN KEY(post_ID) REFERENCES Post(post_ID) ON DELETE CASCADE,
    FOREIGN KEY(user_ID) REFERENCES Users(user_ID) ON DELETE CASCADE
);