package com.nikesu.untitled.dao;

import com.nikesu.untitled.entity.User;

import java.util.ArrayList;

/**
 * @author 倪可塑
 * @version 1.0
 */
public interface UserDao {

    /**
     * 向数据库中添加一个新用户
     * @param user 要添加至数据库的 User 对象，addUser()
     *             方法不对 user 各个字段的合法性进行检查，
     *             故为了确保添加成功，传入的参数 user 必
     *             须至少已经设置好了合法的 userName、
     *             password 和 userGroupId。
     * @return 成功返回 true，否则返回 false
     */
    boolean addUser(User user);

    /**
     * 根据 userID 获取 User 对象
     * @param userId
     * @return userId 对应的 User 对象，如不存在返回 null。
     */
    User getUserById(String userId);

    /**
     * 根据 userName 获取 User 对象
     * @param userName
     * @return userName 对应的 User 对象，如不存在返回 null。
     */
    User getUserByName(String userName);

    /**
     * 获取所有用户。
     * @return 按 userID 递增排序的所有 User 的 ArrayList，
     *         如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<User> getUsers();

    /**
     * 对所有用户按 userId 排序，返回一个包含从第 beg 个用户到第 end - 1 个用户的
     * ArrayList（从 0 开始计数）。如 getUsers(0,2) 将返回一个由 user[0] 和
     * user[1] 构成的 ArrayList。
     * 如果参数 beg 大于等于用户总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于用户总数，将返回从第 beg 个用户到最后一个用户的 ArrayList。
     * @param beg
     * @param end
     * @return
     */
    ArrayList<User> getUsers(int beg, int end);

    /**
     * 获取指定用户组的所有用户。
     * @param userGroupId 用户组的 userGroupId
     * @return 按 userID 递增排序的指定用户组的所有 User 的
     *         ArrayList，如果数据库为空，返回一个长度为 0
     *         的空 ArrayList。
     */
    ArrayList<User> getUsersByGroup(String userGroupId);

    /**
     * 对所有用户组为 userGroupId 的用户按 userId 排序，返回一个包含从第 beg
     * 个用户到第 end - 1 个用户的 ArrayList（从 0 开始计数）。
     * 如果参数 beg 大于等于该用户组的用户总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于该用户组的用户总数，将返回从第 beg 个用户到最后一个用户
     * 的 ArrayList。
     * @param userGroupId
     * @param beg
     * @param end
     * @return
     */
    ArrayList<User> getUsersByGroup(String userGroupId, int beg, int end);

    /**
     * 更新 userId 对应的用户的 userName
     * @param userId
     * @param userName
     * @return 成功返回 true，否则返回 false
     */
    boolean updateUserName(String userId, String userName);

    /**
     * 更新 userId 对应的用户的 password
     * @param userId
     * @param password
     * @return 成功返回 true，否则返回 false
     */
    boolean updatePassword(String userId, String password);

    /**
     * 更新 userId 对应的用户的 email
     * @param userId
     * @param email
     * @return 成功返回 true，否则返回 false
     */
    boolean updateEmail(String userId, String email);

    /**
     * 更新 userId 对应的用户的 userGroupId
     * @param userId
     * @param userGroupId
     * @return 成功返回 true，否则返回 false
     */
    boolean updateUserGroupId(String userId, String userGroupId);

    /**
     * 删除 userId 对应的用户
     * @param userId
     * @return 删除成功返回 true，否则（如 userId 不存在时）返回 false
     */
    boolean delUserById(String userId);

    /**
     * 删除指定用户组的所有用户
     * @param userGroupId 用户组的 userGroupId
     * @return 删除成功返回 true，否则（如 userGroup 不存在或没有用户
     *         时）返回 false
     */
    boolean delUserByGroup(String userGroupId);

    /**
     * 删除所有用户
     * @return 成功返回 true，否则返回 false
     */
    boolean delAllUsers();

    /**
     *
     * @param userId
     * @return 如果存在 ID 为指定的 userId 的用户，返回 true，否则返回 false
     */
    boolean hasId(String userId);

    /**
     *
     * @param userName
     * @return 如果存在用户名为指定的 userName 的用户，返回 true，否则返回 false
     */
    boolean hasName(String userName);
}
