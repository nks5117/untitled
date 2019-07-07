package com.nikesu.untitled.dao.impl;

import java.util.ArrayList;

import com.nikesu.untitled.dao.ReplyDao;
import com.nikesu.untitled.entity.Reply;
/**
 * 
 * @author 白雪婷
 *
 */
public class ReplyDaoImpl extends BaseDaoImpl implements ReplyDao{
	static final String table = "bbs_reply";

	@Override
	public boolean addReply(Reply reply) {
		  return insertToTable(table, reply) == 1;
	}

	@Override 
	public Reply getReplyById(String replyId) {	
		 String sql =  "SELECT * FROM " + table + " WHERE replyid = '" + replyId + "';";
	     ArrayList<Reply> r = executeQuery(sql, Reply.class);
	     if (r.size() == 0) {
	    	 return  null;
	     }
	     else {
	         return  r.get(0);
	     }
	}

	@Override
	public ArrayList<Reply> getRepliesByPost(String postId) {
	
		String sql =  "SELECT * FROM " + table + " WHERE postid = '" + postId + "' OEDER BY replyid;";
        ArrayList<Reply> r = executeQuery(sql, Reply.class);
        return  r;
 	}

	@Override
	public ArrayList<Reply> getRepliesByPost(String postId, int beg, int cnt) {
		 String sql =  "SELECT * FROM " + table +
	                " WHERE postid = '" +
	                postId + "' ORDER BY replyid LIMIT " + beg + "," + cnt + ";";

	        ArrayList<Reply> r = executeQuery(sql, Reply.class);
	        return r;
	}

	@Override
	public ArrayList<Reply> getRepliesByUser(String userId) {
		String sql =  "SELECT * FROM " + table + " WHERE userid = '" + userId + "' OEDER BY replyid;";
        ArrayList<Reply> r = executeQuery(sql, Reply.class);
        return  r;
	}

	@Override
	public ArrayList<Reply> getRepliesByUser(String userId, int beg, int cnt) {
		String sql =  "SELECT * FROM " + table +
                " WHERE userid = '" +
                userId + "' ORDER BY replyid LIMIT " + beg + "," + cnt + ";";
        ArrayList<Reply> r = executeQuery(sql, Reply.class);
        return r;
	}

	@Override
	public boolean updateContent(String replyId, String content) {
		 String sql = "UPDATE " + table + " SET content = '" + content + "' WHERE replyid = '" + replyId + "';";
	        // 如果回复的内容原本就是 content，则 executeUpdate() 会返回 0，这种情况下 updateContent() 也会返回 false
	    return executeUpdate(sql) > 0;
		
	}

	@Override
	public boolean delReplyById(String replyId) {
		String sql = "DELETE FROM " + table + " WHERE replyid = '" + replyId + "';";

        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean delAllReplies() {
		String sql = "DELETE FROM " + table + ";";
        return executeUpdate(sql) > 0;
	}

	@Override
	public boolean hasId(String replyId) {
		String sql =  "SELECT * FROM " + table + " WHERE replyid = '" + replyId + "';";
        ArrayList<Reply> r = executeQuery(sql, Reply.class);
        return r.size() > 0;
		
	}

}
