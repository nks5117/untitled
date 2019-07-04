package com.nikesu.untitled.dao;

import com.nikesu.untitled.entity.UserGroup;

import java.util.ArrayList;

/**
 * @author 倪可塑
 * @version 1.0
 */
public interface UserGroupDao {

    /**
     * 向数据库中添加一个新用户组
     * @param userGroup 要添加至数据库的 UserGroup 对象，addUserGroup()
     *                  方法不对 userGroup 各个字段的合法性进行检查，故为
     *                  了确保添加成功，传入的参数 userGroup 必须至少已经
     *                  设置好了合法的 userGroupName。
     * @return 成功返回 true，否则返回 false
     */
    boolean addUserGroup(UserGroup userGroup);

    /**
     * 根据 userGroupId 获取 UserGroup 对象
     * @param userGroupId
     * @return userGroupId 对应的 UserGroup 对象，如不存在返回 null
     */
    UserGroup getUserGroupById(String userGroupId);

    /**
     * 根据 userGroupName 获取 UserGroup 对象
     * @param userGroupName
     * @return userGroupName 对应的 UserGroup 对象，如不存在返回 null
     */
    UserGroup getUserGroupByName(String userGroupName);

    /**
     * 获取所有用户组。
     * @return 按 userGroupId 递增排序的所有 UserGroup 的 ArrayList，
     *         如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<UserGroup> getUserGroups();

    /**
     * 更新 userGroupId 对应的用户组的 uerGroupName
     * @param userGroupId
     * @param uerGroupName
     * @return 成功返回 true，否则返回 false
     */
    boolean updateUserGroupName(String userGroupId, String uerGroupName);

    /**
     * 更新 userGroupId 对应的用户组的 allowPost 属性
     * @param userGroupId
     * @param allowPost 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowPost(String userGroupId, String allowPost);

    /**
     * 更新 userGroupId 对应的用户组的 allowReply 属性
     * @param userGroupId
     * @param allowReply 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowReply(String userGroupId, String allowReply);

    /**
     * 更新 userGroupId 对应的用户组的 allowEditPost 属性
     * @param userGroupId
     * @param allowEditPost 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowEditPost(String userGroupId, String allowEditPost);

    /**
     * 更新 userGroupId 对应的用户组的 allowTopPost 属性
     * @param userGroupId
     * @param allowTopPost 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowTopPost(String userGroupId, String allowTopPost);

    /**
     * 更新 userGroupId 对应的用户组的 allowDelPost 属性
     * @param userGroupId
     * @param allowDelPost 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowDelPost(String userGroupId, String allowDelPost);

    /**
     * 更新 userGroupId 对应的用户组的 allowEditUser 属性
     * @param userGroupId
     * @param allowEditUser 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowEditUser(String userGroupId, String allowEditUser);

    /**
     * 更新 userGroupId 对应的用户组的 allowBanUser 属性
     * @param userGroupId
     * @param allowBanUser 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowBanUser(String userGroupId, String allowBanUser);

    /**
     * 更新 userGroupId 对应的用户组的 allowEditForum 属性
     * @param userGroupId
     * @param allowEditForum 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateAllowEditForum(String userGroupId, String allowEditForum);

    /**
     * 删除 userGroupId 对应的用户组。
     * @param userGroupId
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delUserGroupById(String userGroupId);

    /**
     * 删除所有用户组。
     * @return 成功返回 true，否则返回 false
     */
    boolean delAllUserGroups();

    /**
     *
     * @param userGroupId
     * @return 如果存在 ID 为指定的 userGroupId 的用户组，返回 true，否则返回 false
     */
    boolean hasId(String userGroupId);

    /**
     *
     * @param userGroupName
     * @return 如果存在用户组名为指定的 userGroupName 的用户组，返回 true，否则返回 false
     */
    boolean hasName(String userGroupName);
}
