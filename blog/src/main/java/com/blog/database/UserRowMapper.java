package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.User;
import com.blog.model.UserLevel;
 
public class UserRowMapper implements RowMapper<User> {
 
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User(rs.getInt("user_ID"));
		user.setUsername(rs.getString("username"));
		user.setCreationDate(rs.getString("creation_date"));
		user.setLastLogin(rs.getString("last_login"));
		user.setProfilePicture(rs.getString("profile_picture"));
		user.setDeleted(rs.getBoolean("is_deleted"));
		if (rs.getBoolean("administrator")) {
			user.setUserLevel(UserLevel.ADMIN);
		} else if (rs.getBoolean("contributor")) {
			user.setUserLevel(UserLevel.CONTRIBUTOR);
		} else {
			user.setUserLevel(UserLevel.READER);
		}
		return user;
	}
}
