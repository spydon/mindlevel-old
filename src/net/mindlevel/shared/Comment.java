package net.mindlevel.shared;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username, comment;
    private int id;
    private Date timestamp;
    private int parentId; //If parentId is 0 the comment is a direct comment and not a reply to somebody else.
    private int threadId;
    private int level;

    protected Comment() {}

    public Comment(int threadId) { //To add non-existing parent comment
        this.id = 0;
        this.parentId = 0;
        setLevel(0);
        this.setThreadId(threadId);
    }

    public Comment(int id,
                   int threadId,
                   String username,
                   String comment,
                   int parentId,
                   Date timestamp) {
        this.setId(id);
        this.setThreadId(threadId);
        this.setUsername(username);
        this.setComment(comment);
        this.setParentId(parentId);
        this.timestamp = timestamp;
    }

    public Comment(int threadId,
                   String username,
                   String comment,
                   int parentId) {
        this.setThreadId(threadId);
        this.setUsername(username);
        this.setComment(comment);
        this.setParentId(parentId);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}