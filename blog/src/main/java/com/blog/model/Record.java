package com.blog.model;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
