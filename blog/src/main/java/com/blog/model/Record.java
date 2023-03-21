package com.blog.model;

import org.json.JSONObject;

/**
 * Abstract class for objects that can be saved in the database.
 */
public abstract class Record {
    private boolean isDeleted;

    public Record() {
    }

    public Record(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Returns the JSON representation of this object.
     *
     * @return JSONObject
     */
    public JSONObject asJSONObject() {
        return new JSONObject()
                .put("isDeleted", isDeleted);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
