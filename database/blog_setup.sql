--Note that this is just the record of database setup
--you don't need to use the commands inside this file

--Create tables:
CREATE TABLE User(
    user_ID INTEGER,
    user_password CHAR(32) NOT NULL,
    username CHAR(32) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_login TIMESTAMP NOT NULL,
    profile_picture VARCHAR(250) DEFAULT NULL,
    contributor BOOLEAN NOT NULL DEFAULT false,
    administrator BOOLEAN NOT NULL DEFAULT false,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(user_ID)
);

CREATE TABLE Post(
    post_ID INTEGER,
    user_ID INTEGER,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    upvotes INTEGER NOT NULL DEFAULT 0,
    downvotes INTEGER NOT NULL DEFAULT 0,
    views INTEGER NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    allow_comments BOOLEAN NOT NULL DEFAULT true,
    PRIMARY KEY(post_ID),
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

CREATE TABLE Comment(
    post_ID INTEGER,
    comment_number INTEGER,
    user_ID INTEGER,
    content VARCHAR(10000) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    upvotes INTEGER NOT NULL DEFAULT 0,
    downvotes INTEGER NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(post_ID, comment_number),
    FOREIGN KEY(post_ID) REFERENCES Post(post_ID) ON DELETE CASCADE,
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

--Delete tables:
drop table Comment;
drop table Post;
drop table User;