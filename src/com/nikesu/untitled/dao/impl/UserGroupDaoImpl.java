package com.nikesu.untitled.dao.impl;

import com.nikesu.untitled.dao.UserGroupDao;
import com.nikesu.untitled.entity.UserGroup;

import java.util.ArrayList;

/**
 * UserGroupDaoImpl
 * @author 王馨怡
 */
public class UserGroupDaoImpl extends BaseDaoImpl implements UserGroupDao{
    static final String table = "bbs_usergroup";
    static UserGroupDaoImpl instance = new UserGroupDaoImpl();

    private UserGroupDaoImpl() {};

    public static UserGroupDaoImpl getInstance() {
        return instance;
    }

    /**
     * 将用户组添加进表中，如果受影响行数为1则添加成功，否则添加失败
     * @param userGroup 要添加至数据库的 UserGroup 对象，addUserGroup()
     *                  方法不对 userGroup 各个字段的合法性进行检查，故为
     *                  了确保添加成功，传入的参数 userGroup 必须至少已经
     *                  设置好了合法的 userGroupName。
     * @return 成功返回true,否则返回false
     */
    @Override
    public boolean addUserGroup(UserGroup userGroup) {
        return insertToTable(table,userGroup)==1;
    }

    /**
     * 在表中根据userGroupId执行查询语句，找到对应的用户组，并返回
     * @param userGroupId
     * @return 如果没有对应用户组，返回null，否则返回用户组对象，也即结果数组的第一个元素
     */
    @Override
    public UserGroup getUserGroupById(String userGroupId) {
        String sql = "SELECT * FROM "+ table + " WHERE usergroupid = '"+ userGroupId +"';";
        ArrayList<UserGroup> r = executeQuery(sql,UserGroup.class);
        if(r.size()==0) {
            return null;
        }
        else {
            return r.get(0);
        }
    }

    @Override
    public UserGroup getUserGroupByName(String userGroupName) {
        String sql = "SELECT * FROM "+table+" WHERE usergroupname='"+userGroupName+"';";
        ArrayList<UserGroup> r = executeQuery(sql,UserGroup.class);
        if(r.size()==0) {
            return null;
        }
        else {
            return r.get(0);
        }
    }

    /**
     * 执行查询语句得到表中所有信息，并按UserGroupId递增排序，返回所有用户组
     * @return 除非表中为空，否则返回所有用户组对象，也即结果数组中所有元素
     */
    @Override
    public ArrayList<UserGroup> getUserGroups() {
        String sql = "SELECT * FROM "+table+" ORDER BY usergroupid;";//按UserGroupId递增
        ArrayList<UserGroup> r = executeQuery(sql,UserGroup.class);
        return r;
    }

    /**
     * 在表中根据userGroupId找到用户组，更新这些用户组的userGroupName,表受影响则更新成功，否则更新失败
     * @param userGroupId
     * @param userGroupName
     * @return 成功返回true,失败返回false
     */
    @Override
    public boolean updateUserGroupName(String userGroupId, String userGroupName) {
        String sql = "UPDATE "+table+" SET usergroupname = '"+userGroupName+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    /**
     * 在表中根据userGroupId找到对应的用户组，更新他们的权限，表受影响则更新成功，否则更新失败
     * @param userGroupId
     * @param allowPost 只能是 "0" 或者 "1"
     * @return 成功返回true,失败返回false
     */
    @Override
    public boolean updateAllowPost(String userGroupId, String allowPost) {
        String sql = "UPDATE "+table+" SET allowpost = '"+allowPost+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowReply(String userGroupId, String allowReply) {
        String sql = "UPDATE "+table+" SET allowreply = '"+allowReply+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowEditPost(String userGroupId, String allowEditPost) {
        String sql = "UPDATE "+table+" SET alloweditpost = '"+allowEditPost+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowTopPost(String userGroupId, String allowTopPost) {
        String sql = "UPDATE "+table+" SET allowtoppost = '"+allowTopPost+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowDelPost(String userGroupId, String allowDelPost) {
        String sql = "UPDATE "+table+" SET allowdelpost = '"+allowDelPost+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowEditUser(String userGroupId, String allowEditUser) {
        String sql = "UPDATE "+table+" SET allowedituser = '"+allowEditUser+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowBanUser(String userGroupId, String allowBanUser) {
        String sql = "UPDATE "+table+" SET allowbanuser = '"+allowBanUser+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean updateAllowEditForum(String userGroupId, String allowEditForum) {
        String sql = "UPDATE "+table+" SET alloweditforum = '"+allowEditForum+"' WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    /**
     * 在表中找到userGroupId对应的用户组，删除；如果表受影响则删除成功，否则删除失败
     * @param userGroupId
     * @return 成功返回true,失败返回false
     */
    @Override
    public boolean delUserGroupById(String userGroupId) {
        String sql = "DELETE FROM "+table+" WHERE usergroupid = '"+userGroupId+"';";
        return executeUpdate(sql)>0;
    }

    @Override
    public boolean delAllUserGroups() {
        String sql = "DELETE FROM "+table+";";
        return executeUpdate(sql)>0;
    }

    /**
     * 在表中查找参数userGroupId，如果有对应的记录则查找成功，否则查找失败
     * @param userGroupId
     * @return 成功返回true,否则返回false
     */
    @Override
    public boolean hasId(String userGroupId) {
        String sql = "SELECT * FROM "+table+" WHERE usergroupid = '"+userGroupId+"';";
        ArrayList<UserGroup> r = executeQuery(sql, UserGroup.class);
        return r.size()>0;
    }

    @Override
    public boolean hasName(String userGroupName) {
        String sql = "SELECT * FROM "+table+" WHERE usergroupname = '"+userGroupName+"';";
        ArrayList<UserGroup> r = executeQuery(sql, UserGroup.class);
        return r.size()>0;
    }
}
