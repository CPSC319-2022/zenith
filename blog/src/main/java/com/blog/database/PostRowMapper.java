package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Post;
 
public class PostRowMapper implements RowMapper<Post> {
 
	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		Post post = new Post(rs.getInt("post_ID"));
		post.setAuthorID(rs.getInt("user_ID"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
		post.setCreationDate(rs.getString("creation_date"));
		post.setLastModified(rs.getString("last_modified"));
		post.setUpvotes(rs.getInt("upvotes"));
		post.setDownvotes(rs.getInt("downvotes"));
		post.setDeleted(rs.getBoolean("is_deleted"));
		post.setAllowComments(rs.getBoolean("allow_comments"));
		return post;
	}
}
