package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.User;
 
public class UserRowMapper implements RowMapper<User> {
 
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUserID(rs.getInt("user_ID"));
		user.setUsername(rs.getString("username"));
		user.setProfilePicture(rs.getString("avatar"));
		// TODO: add other fields
		return user;
	}
}
