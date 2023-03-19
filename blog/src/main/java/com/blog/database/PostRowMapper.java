package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Post;
 
public class PostRowMapper implements RowMapper<Post> {
 
	@Override
	public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
		int postID = rs.getInt("post_ID");
		String authorID = rs.getString("user_ID");
		String title = rs.getString("title");
		String content = rs.getString("content");
		String creationDate = rs.getString("creation_date");
		String lastModified = rs.getString("last_modified");
		int upvotes = rs.getInt("upvotes");
		int downvotes = rs.getInt("downvotes");
		boolean isDeleted = rs.getBoolean("is_deleted");
		int views = rs.getInt("views");
		boolean allowComments = rs.getBoolean("allow_comments");
		return new Post(postID, authorID, title, content, creationDate, lastModified, upvotes, downvotes, isDeleted, views, allowComments);
	}
}
