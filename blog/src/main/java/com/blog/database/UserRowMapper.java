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
		String userId = rs.getString("user_ID");
		String username = rs.getString("username");
		UserLevel userLevel = UserLevel.READER;
		if (rs.getInt("user_level") == 1) {
			userLevel = UserLevel.CONTRIBUTOR;
		} else if (rs.getInt("user_level") == 2) {
			userLevel = UserLevel.ADMIN;
		}
		String creationDate = rs.getString("creation_date");
		String lastLogin = rs.getString("last_login");
		UserStatus userStatus = null;
		int status = rs.getInt("user_status");
		if (status == 0) {
			userStatus = UserStatus.ONLINE;
		} else if (status == 1) {
			userStatus = UserStatus.AWAY;
		} else if (status == 2) {
			userStatus = UserStatus.BUSY;
		} else {
			userStatus = UserStatus.OFFLINE;
		}
		String profilePicture = rs.getString("profile_picture");
		String bio = rs.getString("bio");
		boolean isDeleted = rs.getBoolean("is_deleted");
		return new User(userId, username, userLevel, creationDate, lastLogin, userStatus, profilePicture, bio, isDeleted);
	}
}
