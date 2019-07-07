package com.nikesu.untitled.dao.impl;

import com.nikesu.untitled.dao.ForumDao;
import com.nikesu.untitled.dao.UserGroupDao;
import com.nikesu.untitled.entity.Forum;
import com.nikesu.untitled.entity.UserGroup;

import java.util.ArrayList;
/**
*ForumDaoimpl.java
*@author 朱君鹏
*/
public class ForumDaoImpl extends BaseDaoImpl implements ForumDao {
	static final String table = "bbs_ForumDao";
	
	
	
	@Override
    public boolean addForum(Forum forum) {
        return insertToTable(table,forum) == 1;
    }

	
	
	@Override
    public Forum getForumById(String forumId) {
        String sql = "SELECT * FROM "+ table + " WHERE forumid = '"+ forumId +"';";
        ArrayList<Forum> r = executeQuery(sql,Forum.class);
        if(r.size()==0) {
            return null;
        }
        else {
            return r.get(0);
        }
   
    }

	
    @Override
    public Forum getForumByName(String forumName) {
        String sql = "SELECT * FROM " + table + " WHERE forumname='"+forumName+"';";
        ArrayList<Forum> r = executeQuery(sql,Forum.class);
        if(r.size()==0) {
            return null;
        }
        else {
            return r.get(0);
        }
    }



	@Override
	public ArrayList<Forum> getForums() {
		
		String sql = "SELECT * FROM " + table + " ORDER BY forumid;";//按forumId递增
        ArrayList<Forum> r = executeQuery(sql,Forum.class);
        return r;
	}



	@Override
	public boolean updateName(String forumId, String forumName) {
	
		String sql = "UPDATE " + table + " SET forumname = '"+forumName+"' WHERE usergroupid = '"+forumId+"';";
        return executeUpdate(sql)>0;
	}



	@Override
	public boolean delForumById(String forumId) {
		
		  String sql = "DELETE FROM "+table+" WHERE forumid = '"+forumId+"';";
	        return executeUpdate(sql)>0;
	}



	@Override
	public boolean delAllForums() {
		
		String sql = "DELETE FROM "+table+";";
        return executeUpdate(sql)>0;
	}



	@Override
	public boolean hasId(String forumId) {
		
		String sql = "SELECT * FROM " + table + " WHERE forumid = '"+forumId+"';";
        ArrayList<Forum> r = executeQuery(sql, Forum.class);
        return r.size()>0;
	}



	@Override
	public boolean hasName(String forumName) {
		
		String sql = "SELECT * FROM "+table+" WHERE forumname = '"+forumName+"';";
        ArrayList<Forum> r = executeQuery(sql, Forum.class);
        return r.size()>0;
	}
}