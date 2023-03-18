--SQL commands in this file are templates when you call ExecuteQuery function in Java
--e.g. result = statement.ExecuteQuery("SELECT * FROM Users");
--Note that you need to change the attribute names in the templates to variable names!

------------------------------
--Create new item
--

INSERT INTO User
VALUES(user_ID, user_password, username, creationDate, creationDate, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)
--Example: INSERT INTO User
--         VALUES("1", 12345, "user1", "2023-03-01 01:02:03", "2023-3-1 01:02:03", DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)

INSERT INTO Post
VALUES(post_ID, user_ID, title, content, creationDate, creationDate, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)
--Example: INSERT INTO Post
--         VALUES(1, "1", "title1", "content1", "2023-03-01 01:02:03", "2023-03-01 01:02:03", DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)

INSERT INTO Comment
VALUES(post_ID, comment_number, user_ID, content, creationDate, creationDate, DEFAULT, DEFAULT, DEFAULT)
--Example: INSERT INTO Comment
--         VALUES(1, 1, "1", "content1", "2023-03-01 01:02:03", "2023-03-01 01:02:03", DEFAULT, DEFAULT, DEFAULT)

------------------------------
--Delete item from table
--

DELETE FROM User WHERE user_ID = x
-- x is an string, post and comment created by this user will be also deleted

DELETE FROM Post WHERE post_ID = x
-- x is an int, comment under this post will be also deleted

DELETE FROM Comment WHERE post_ID = x AND comment_number = y
-- x, y are int

------------------------------
--Some use cases
--

SELECT username FROM User WHERE contributor = true
--Get username of all contributors

SELECT Count(*) FROM Post
--Get the total number of all posts

SELECT user_ID, content, comment_time FROM Comment WHERE post_ID = x
--Get all comments under a post for display