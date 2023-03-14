package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Comment;
 
public class CommentRowMapper implements RowMapper<Comment> {
 
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Comment comment = new Comment(rs.getInt("post_ID"), rs.getInt("comment_number"));
		comment.setAuthorID(rs.getInt("user_ID"));
        comment.setContent(rs.getString("content"));
		comment.setCreationDate(rs.getString("creation_date"));
		comment.setLastModified(rs.getString("last_modified"));
		comment.setUpvotes(rs.getInt("upvotes"));
		comment.setDownvotes(rs.getInt("downvotes"));
		comment.setDeleted(rs.getBoolean("is_deleted"));
		return comment;
	}
}
