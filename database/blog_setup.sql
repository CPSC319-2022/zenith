--Note that this is just the record of database setup
--you don't need to use the commands inside this file

--Create tables:
--for user status 0:online, 1:away, 2:busy, 3:offline
--for user level: 0:reader, 1: contributor, 2: admin
CREATE TABLE User(
    user_ID VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    creation_date CHAR(27) NOT NULL,
    last_login CHAR(27) NOT NULL,
    user_status TINYINT NOT NULL DEFAULT 0,
    profile_picture VARCHAR(255) DEFAULT NULL,
    bio VARCHAR(1000) DEFAULT NULL,
    user_level TINYINT NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(user_ID)
);

CREATE TABLE Post(
    post_ID INTEGER,
    user_ID VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    content VARCHAR(20000) NOT NULL,
    creation_date CHAR(27) NOT NULL,
    last_modified CHAR(27) NOT NULL,
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
    user_ID VARCHAR(255),
    content VARCHAR(10000) NOT NULL,
    creation_date CHAR(27) NOT NULL,
    last_modified CHAR(27) NOT NULL,
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