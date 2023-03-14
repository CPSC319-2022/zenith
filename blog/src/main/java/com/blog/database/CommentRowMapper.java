package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Comment;
 
public class CommentRowMapper implements RowMapper<Comment> {
 
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		int postID = rs.getInt("post_ID");
		int commentID = rs.getInt("comment_number");
		int authorID = rs.getInt("user_ID");
		String content = rs.getString("content");
		String creationDate = rs.getString("creation_date");
		String lastModified = rs.getString("last_modified");
		int upvotes = rs.getInt("upvotes");
		int downvotes = rs.getInt("downvotes");
		boolean isDeleted = rs.getBoolean("is_deleted");
		return new Comment(postID, commentID, authorID, content, creationDate, lastModified, upvotes, downvotes, isDeleted);
	}
}
