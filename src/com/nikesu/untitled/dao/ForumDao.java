package com.nikesu.untitled.dao;

import com.nikesu.untitled.entity.Forum;

import java.util.ArrayList;

/**
 * @author 倪可塑
 * @version 1.0
 */
public interface ForumDao {

    /**
     * 向数据库中添加一个新板块
     * @param forum 要添加至数据库的 Forum 对象，addForum() 方法不对
     *              forum 各个字段的合法性进行检查，故为了确保添加成功，
     *              传入的参数 forum 必须至少已经设置好了合法的 forumName。
     * @return 成功返回 true，否则返回 false
     */
    boolean addForum(Forum forum);

    /**
     * 根据 forumId 获取 Forum 对象
     * @param forumId
     * @return forumId 对应的 Forum 对象，如不存在返回 null
     */
    Forum getForumById(String forumId);

    /**
     * 根据 forumName 获取 Forum 对象
     * @param forumName
     * @return forumName 对应的 Forum 对象，如不存在返回 null
     */
    Forum getForumByName(String forumName);

    /**
     * 获取所有板块。
     * @return 按 forumId 递增排序的所有 Forum 的 ArrayList，
     *         如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Forum> getForums();

    /**
     * 更新 forumId 对应的板块的 forumName
     * @param forumId
     * @param forumName
     * @return 成功返回 true，否则返回 false
     */
    boolean updateName(String forumId, String forumName);

    /**
     * 删除 forumId 对应的板块。
     * @param forumId
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delForumById(String forumId);

    /**
     * 删除所有板块。
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delAllForums();

    /**
     *
     * @param forumId
     * @return 如果存在 ID 为指定的 forumId 的板块，返回 true，否则返回 false
     */
    boolean hasId(String forumId);

    /**
     *
     * @param forumName
     * @return 如果存在名称为指定的 forumName 的板块，返回 true，否则返回 false
     */
    boolean hasName(String forumName);
}
