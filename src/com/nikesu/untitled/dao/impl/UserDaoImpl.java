package com.nikesu.untitled.dao.impl;

import com.nikesu.untitled.dao.UserDao;
import com.nikesu.untitled.entity.User;

import java.util.ArrayList;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    static final String table = "bbs_user";
    @Override
    public boolean addUser(User user) {
        return insertToTable(table, user) == 1;
    }

    @Override
    public User getUserById(String userId) {
        String sql =  "SELECT * FROM " + table + " WHERE userid = '" + userId + "';";
        ArrayList<User> r = executeQuery(sql, User.class);
        if (r.size() == 0) {
            return  null;
        }
        else {
            return  r.get(0);
        }
    }

    @Override
    public User getUserByName(String userName) {
        String sql =  "SELECT * FROM " + table + " WHERE username = '" + userName + "';";
        ArrayList<User> r = executeQuery(sql, User.class);
        if (r.size() == 0) {
            return  null;
        }
        else {
            return  r.get(0);
        }
    }

    @Override
    public ArrayList<User> getUsers() {
        String sql =  "SELECT * FROM " + table + ";";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r;
    }

    @Override
    public ArrayList<User> getUsers(int beg, int cnt) {
        String sql =  "SELECT * FROM " + table + " LIMIT " + beg + "," + cnt + ";";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r;
    }

    @Override
    public ArrayList<User> getUsersByGroup(String userGroupId) {
        String sql =  "SELECT * FROM " + table + " WHERE usergroupid = '" + userGroupId + "';";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r;
    }

    @Override
    public ArrayList<User> getUsersByGroup(String userGroupId, int beg, int cnt) {
        String sql =  "SELECT * FROM " + table +
                " WHERE usergroupid = '" +
                userGroupId + "' LIMIT " + beg + "," + cnt + ";";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r;
    }

    @Override
    public boolean updateUserName(String userId, String userName) {
        String sql = "UPDATE " + table + " SET username = '" + userName + "' WHERE userid = '" + userId + "';";

        // 如果用户的名字原本就是 userName，则 executeUpdate() 会返回 0，这种情况下 updateUserName() 也会返回 false
        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        String sql = "UPDATE " + table + " SET password = '" + password + "' WHERE userid = '" + userId + "';";

        // 如果用户的密码原本就是 password，则 executeUpdate() 会返回 0，这种情况下 updatePassword() 也会返回 false
        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean updateEmail(String userId, String email) {
        String sql = "UPDATE " + table + " SET email = '" + email + "' WHERE userid = '" + userId + "';";

        // 如果用户的邮箱原本就是 email，则 executeUpdate() 会返回 0，这种情况下 updateEmail() 也会返回 false
        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean updateUserGroupId(String userId, String userGroupId) {
        String sql = "UPDATE " + table + " SET usergroupid = '" + userGroupId + "' WHERE userid = '" + userId + "';";

        // 如果用户的用户组原本就是 userGroupId，则 executeUpdate() 会返回 0，这种情况下 updateUserGroupId() 也会返回 false
        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean delUserById(String userId) {
        String sql = "DELETE FROM " + table + " WHERE userid = '" + userId + "';";

        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean delUserByGroup(String userGroupId) {
        String sql = "DELETE FROM " + table + " WHERE usergroupid = '" + userGroupId + "';";

        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean delAllUsers() {
        String sql = "DELETE FROM " + table + ";";

        return executeUpdate(sql) > 0;
    }

    @Override
    public boolean hasId(String userId) {
        String sql =  "SELECT * FROM " + table + " WHERE userid = '" + userId + "';";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r.size() > 0;
    }

    @Override
    public boolean hasName(String userName) {
        String sql =  "SELECT * FROM " + table + " WHERE username = '" + userName + "';";
        ArrayList<User> r = executeQuery(sql, User.class);
        return r.size() > 0;
    }
}
