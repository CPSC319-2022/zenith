package com.blog.database;

import java.sql.ResultSet;
import java.sql.SQLException;
 
import org.springframework.jdbc.core.RowMapper;

import com.blog.model.PromotionRequest;
import com.blog.model.UserLevel;

public class PromotionRequestRowMapper implements RowMapper<PromotionRequest>  {
    @Override
	public PromotionRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
		int requestID = rs.getInt("request_ID");
        String userId = rs.getString("user_ID");
		UserLevel target = UserLevel.READER;
		if (rs.getInt("target_level") == 2) {
			target = UserLevel.CONTRIBUTOR;
		} else if (rs.getInt("target_level") == 3) {
			target = UserLevel.ADMIN;
		}
		String requestTime = rs.getString("request_time");
		String reason = rs.getString("reason");
		boolean isDeleted = rs.getBoolean("is_deleted");
		return new PromotionRequest(requestID, userId, target, requestTime, reason, isDeleted);
	}
}
