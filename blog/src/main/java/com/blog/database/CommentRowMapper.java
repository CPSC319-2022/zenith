package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.Comment;
 
public class CommentRowMapper implements RowMapper<Comment> {
 
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Comment comment = new Comment();
		// TODO: add new constructor for comment
		comment.setPostID(rs.getInt("post_ID"));
		comment.setCommentID(rs.getInt("comment_number"));
		comment.setAuthorID(rs.getInt("user_ID"));
        comment.setContent(rs.getString("content"));
        // TODO: add other fields
		return comment;
	}
}
