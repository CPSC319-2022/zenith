package com.blog.model;

import org.json.JSONObject;

/**
 * Abstract class for objects that can be saved in the database.
 *
 * Methods
 * ----------
 * boolean  isDeleted()
 * void     setDeleted(boolean deleted)
 */
abstract class Record {
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
        JSONObject json = new JSONObject();

        json.put("isDeleted", isDeleted);

        return json;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
