package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.User;
import com.blog.model.UserLevel;
import com.blog.model.UserStatus;
 
public class UserRowMapper implements RowMapper<User> {
 
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		int userId = rs.getInt("user_ID");
		String username = rs.getString("username");
		UserLevel userLevel = null;
		if (rs.getBoolean("administrator")) {
			userLevel = UserLevel.ADMIN;
		} else if (rs.getBoolean("contributor")) {
			userLevel = UserLevel.CONTRIBUTOR;
		} else {;
			userLevel = UserLevel.READER;
		}
		String creationDate = rs.getString("creation_date");
		String lastLogin = rs.getString("last_login");
		UserStatus userStatus = UserStatus.OFFLINE;
		// todo
		String profilePicture = rs.getString("profile_picture");
		boolean isDeleted = rs.getBoolean("is_deleted");
		return new User(userId, username, userLevel, creationDate, lastLogin, userStatus, profilePicture, isDeleted);
	}
}
