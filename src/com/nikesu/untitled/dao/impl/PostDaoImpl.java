package com.nikesu.untitled.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.nikesu.untitled.dao.*;
import com.nikesu.untitled.entity.Post;
import com.nikesu.untitled.entity.User;

/**
 * @author 卢琦
 */
public class PostDaoImpl extends BaseDaoImpl implements PostDao{
	static final String table = "bbs_post";
	@Override
	public boolean addPost(Post post) {
		return insertToTable(table, post) == 1;
	}

	@Override
	public Post getPostById(String postId) {
		String sql =  "SELECT * FROM " + table + " WHERE postid = '" + postId + "';";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        if (r.size() == 0) {
            return  null;
        }
        else {
            return  r.get(0);
        }
	}

	@Override
	public ArrayList<Post> getPosts() {
		String sql =  "SELECT * FROM " + table + " ORDER BY top,postId;";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public ArrayList<Post> getPosts(int beg, int cnt) {
		String sql =  "SELECT * FROM " + table + " LIMIT " + beg + "," + cnt + ";";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public ArrayList<Post> getPostsByForum(String forumId) {

		String sql =  "SELECT * FROM " + table + " WHERE forumid = '" + forumId + "';";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public ArrayList<Post> getPostsByForum(String forumId, int beg, int cnt) {
		String sql =  "SELECT * FROM " + table +
                " WHERE forumid = '" +
                forumId + "' LIMIT " + beg + "," + cnt + ";";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public ArrayList<Post> getPostsByUser(String userId) {
		String sql =  "SELECT * FROM " + table + " WHERE forumid = '" + userId + "';";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public ArrayList<Post> getPostsByUser(String userId, int beg, int cnt) {
		String sql =  "SELECT * FROM " + table +
                " WHERE forumid = '" +
                userId + "' LIMIT " + beg + "," + cnt + ";";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r;
	}

	@Override
	public boolean updateTitle(String postId, String title) {
		String sql = "UPDATE " + table + " SET postid = '" + postId + "' WHERE title = '" + title + "';";

        // 如果用户的名字原本就是 userName，则 executeUpdate() 会返回 0，这种情况下 updateUserName() 也会返回 false
        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean updateContent(String postId, String content) {
		String sql = "UPDATE " + table + " SET postId = '" + postId + "' WHERE content = '" + content + "';";

        // 如果用户的名字原本就是 userName，则 executeUpdate() 会返回 0，这种情况下 updateUserName() 也会返回 false
        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean updateTop(String postId, String top) {
		String sql = "UPDATE " + table + " SET postId = '" + postId + "' WHERE top = '" + top + "';";

        // 如果用户的名字原本就是 userName，则 executeUpdate() 会返回 0，这种情况下 updateUserName() 也会返回 false
        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean delPostById(String postId) {
		String sql = "DELETE FROM " + table + " WHERE postid = '" + postId + "';";

        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean delAllPosts() {
		String sql = "DELETE FROM " + table + ";";

        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean hasId(String postId) {
		String sql =  "SELECT * FROM " + table + " WHERE postid = '" + postId + "';";
        ArrayList<Post> r = executeQuery(sql, Post.class);
        return r.size() > 0;
	}


}
