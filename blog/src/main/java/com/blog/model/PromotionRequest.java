package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.BlogException;
import com.blog.exception.DoesNotExistException;
import com.blog.exception.IsDeletedException;

import org.json.JSONObject;

public class PromotionRequest extends Record {
    public static final int NEW_PROMOTION_REQUEST_ID = 0;

    private int requestID;
    private String userID;
    private UserLevel target;
    private String requestTime;
    private String reason;

    // Optional fields
    private String username;

    public PromotionRequest(int requestID) {
        this.requestID = requestID;
    }

    public PromotionRequest(int requestID,
                            String userID,
                            UserLevel target,
                            String requestTime,
                            String reason,
                            boolean isDeleted) {
        super(isDeleted);
        this.requestID = requestID;
        this.userID = userID;
        this.target = target;
        this.requestTime = requestTime;
        this.reason = reason;
    }

    public PromotionRequest(int requestID,
                            String userID,
                            UserLevel target,
                            String requestTime,
                            String reason,
                            boolean isDeleted,
                            String username) {
        super(isDeleted);
        this.requestID = requestID;
        this.userID = userID;
        this.target = target;
        this.requestTime = requestTime;
        this.reason = reason;
        this.username = username;
    }

    /**
     * Factory method to retrieve the promotion request with the given requestID.
     *
     * @param requestID The promotion request to retrieve.
     * @return The promotion request with the given requestID.
     * @throws BlogException
     */
    public static PromotionRequest retrieve(int requestID) throws BlogException {
        PromotionRequest request = new PromotionRequest(requestID);
        Database.retrieve(request);
        return request;
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        JSONObject result = super.asJSONObject()
                .put("requestID", requestID)
                .put("userID", userID)
                .put("target", target)
                .put("requestTime", requestTime)
                .put("reason", reason);

        if (username == null) {
            return result;
        }

        return result.put("username", username);
    }

    /**
     * Returns the JSON string of this object
     *
     * @return String
     */
    public String asJSONString() {
        return asJSONObject().toString();
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public UserLevel getTarget() {
        return target;
    }

    public void setTarget(UserLevel target) {
        this.target = target;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
