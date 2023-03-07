package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Post;
 
public class PostRowMapper implements RowMapper<Post> {
 
	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		Post post = new Post();
		// TODO: add new constructor for post
		post.setPostID(rs.getInt("post_ID"));
		post.setAuthorID(rs.getInt("user_ID"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        // TODO: add other fields
		return post;
	}
}
