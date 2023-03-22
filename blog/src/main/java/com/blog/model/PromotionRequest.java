package com.blog.model;

import com.blog.database.Database;
import com.blog.exception.DoesNotExistException;
import org.json.JSONObject;

public class PromotionRequest extends Record {
    public static final int NEW_PROMOTION_REQUEST_ID = 0;

    private final int requestID;
    private String userID;
    private UserLevel target;
    private String requestTime;
    private String reason;

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

    /**
     * Factory method to retrieve the promotion request with the given requestID.
     *
     * @param requestID The promotion request to retrieve.
     * @return The promotion request with the given requestID.
     */
    public static PromotionRequest retrieve(int requestID) throws DoesNotExistException {
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
        return super.asJSONObject()
                .put("requestID", requestID)
                .put("userID", userID)
                .put("target", target)
                .put("requestTime", requestTime)
                .put("reason", reason);
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
}
