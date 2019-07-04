package com.nikesu.untitled.entity;

public class UserGroup {
    private String userGroupId;
    private String userGroupName;
    private String allowPost;
    private String allowReply;
    private String allowEditPost;
    private String allowTopPost;
    private String allowDelPost;
    private String allowEditUser;
    private String allowBanUser;
    private String allowEditForum;

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getAllowPost() {
        return allowPost;
    }

    public void setAllowPost(String allowPost) {
        this.allowPost = allowPost;
    }

    public String getAllowReply() {
        return allowReply;
    }

    public void setAllowReply(String allowReply) {
        this.allowReply = allowReply;
    }

    public String getAllowEditPost() {
        return allowEditPost;
    }

    public void setAllowEditPost(String allowEditPost) {
        this.allowEditPost = allowEditPost;
    }

    public String getAllowTopPost() {
        return allowTopPost;
    }

    public void setAllowTopPost(String allowTopPost) {
        this.allowTopPost = allowTopPost;
    }

    public String getAllowDelPost() {
        return allowDelPost;
    }

    public void setAllowDelPost(String allowDelPost) {
        this.allowDelPost = allowDelPost;
    }

    public String getAllowEditUser() {
        return allowEditUser;
    }

    public void setAllowEditUser(String allowEditUser) {
        this.allowEditUser = allowEditUser;
    }

    public String getAllowBanUser() {
        return allowBanUser;
    }

    public void setAllowBanUser(String allowBanUser) {
        this.allowBanUser = allowBanUser;
    }

    public String getAllowEditForum() {
        return allowEditForum;
    }

    public void setAllowEditForum(String allowEditForum) {
        this.allowEditForum = allowEditForum;
    }
}
