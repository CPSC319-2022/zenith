--SQL commands in this file are templates when you call ExecuteQuery function in Java
--e.g. result = statement.ExecuteQuery("SELECT * FROM Users");
--Note that you need to change the attribute names in the templates to variable names!

------------------------------
--Create new item
--

INSERT INTO User
VALUES(user_ID, user_password, username, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT)
--System generate: user_ID(int)
--User input: user_password(string), username(string)

INSERT INTO Post
VALUES(post_ID, user_ID, thumbnail, title, content, post_time, DEFAULT)
--System generate: post_ID(int), user_ID(int), post_time(Timestamp)
--User input: thumbnail(string)(url for image, use DEFAULT if user leaves it blank),
--            title(string), content(string) 

INSERT INTO Comment
VALUES(post_ID, comment_number, user_ID, content, post_time, DEFAULT)
--System generate: post_ID(int), comment_number(int), user_ID(int), post_time(Timestamp)
--User input: content(string) 

------------------------------
--Delete item from table
--

DELETE FROM User WHERE user_ID = x
-- x is an int, post and comment created by this user will be also deleted

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