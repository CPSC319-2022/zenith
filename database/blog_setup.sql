--Note that this is just the record of database setup
--you don't need to use the commands inside this file

--Create tables:
--for user status 0:online, 1:away, 2:busy, 3:offline
--for user level: 1:reader, 2: contributor, 3: admin
CREATE TABLE User(
    user_ID VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    creation_date CHAR(30) NOT NULL,
    last_active CHAR(30) NOT NULL,
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
    creation_date CHAR(30) NOT NULL,
    last_modified CHAR(30) NOT NULL,
    upvotes INTEGER NOT NULL DEFAULT 0,
    downvotes INTEGER NOT NULL DEFAULT 0,
    views INTEGER NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    allow_comments BOOLEAN NOT NULL DEFAULT true,
    thumbnail_url VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY(post_ID),
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

CREATE TABLE Comment(
    post_ID INTEGER,
    comment_number INTEGER,
    user_ID VARCHAR(255),
    content VARCHAR(10000) NOT NULL,
    creation_date CHAR(30) NOT NULL,
    last_modified CHAR(30) NOT NULL,
    upvotes INTEGER NOT NULL DEFAULT 0,
    downvotes INTEGER NOT NULL DEFAULT 0,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(post_ID, comment_number),
    FOREIGN KEY(post_ID) REFERENCES Post(post_ID) ON DELETE CASCADE,
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

CREATE TABLE Vote_Post(
    post_ID INTEGER,
    user_ID VARCHAR(255),
    is_upvoted BOOLEAN,
    PRIMARY KEY(post_ID, user_ID),
    FOREIGN KEY(post_ID) REFERENCES Post(post_ID) ON DELETE CASCADE,
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

CREATE TABLE Vote_Comment(
    post_ID INTEGER,
    comment_number INTEGER,
    user_ID VARCHAR(255),
    is_upvoted BOOLEAN,
    PRIMARY KEY(post_ID, comment_number, user_ID),
    FOREIGN KEY(post_ID, comment_number) REFERENCES Comment(post_ID, comment_number) ON DELETE CASCADE,
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

CREATE TABLE Promotion_Request(
    request_ID INTEGER,
    user_ID VARCHAR(255),
    target_level TINYINT NOT NULL,
    request_time CHAR(30) NOT NULL,
    reason VARCHAR(500),
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(request_ID),
    FOREIGN KEY(user_ID) REFERENCES User(user_ID) ON DELETE CASCADE
);

--Delete tables:
drop table Promotion_Request;
drop table Vote_Comment;
drop table Vote_Post;
drop table Comment;
drop table Post;
drop table User;