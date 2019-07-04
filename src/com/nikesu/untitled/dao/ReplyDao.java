package com.nikesu.untitled.dao;

import com.nikesu.untitled.entity.Reply;

import java.util.ArrayList;

/**
 * @author 倪可塑
 * @version 1.0
 */
public interface ReplyDao {
    /**
     * 向数据库中添加一个新回复
     * @param reply 要添加至数据库的 Reply 对象，addReply() 方法不对 reply
     *             各个字段的合法性进行检查，故为了确保添加成功，传入的参数
     *             reply 必须至少已经设置好了合法的 postId、userId 和 content。
     * @return 成功返回 true，否则返回 false
     */
    boolean addReply(Reply reply);

    /**
     * 根据 replyId 获取 Reply 对象
     * @param replyId
     * @return replyId 对应的 Reply 对象，如不存在返回 null
     */
    Reply getReplyById(String replyId);

    /**
     * 获取指定帖子的所有回复。
     * @param postId
     * @return 该帖子下按 replyId 递增排序的所有回复的 ArrayList，
     *         如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Reply> getRepliesByPost(String postId);

    /**
     * 对指定帖子的所有回复按 replyId 排序，返回一个包含从第 beg 个回复到第
     * end - 1 个回复的 ArrayList（从 0 开始计数）。
     * 如果参数 beg 大于等于该帖子回复总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于该帖子回复总数，将返回从第 beg 个回复到最后一个回复的 ArrayList。
     * @param postId
     * @param beg
     * @param end
     * @return
     */
    ArrayList<Reply> getRepliesByPost(String postId, int beg, int end);

    /**
     * 获取指定用户发表的所有回复。
     * @param userId
     * @return 该用户发表的按 replyId 递增排序的所有回复的 ArrayList，
     *         如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Reply> getRepliesByUser(String userId);

    /**
     * 对指定用户发表的所有回复按 replyId 排序，返回一个包含从第 beg 个回复到第
     * end - 1 个回复的 ArrayList（从 0 开始计数）。
     * 如果参数 beg 大于等于该用户发表的回复总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于该用户发表的回复总数，将返回从第 beg 个回复到最后一个回复的 ArrayList。
     * @param userId
     * @param beg
     * @param end
     * @return
     */
    ArrayList<Reply> getRepliesByUser(String userId, int beg, int end);

    /**
     * 更新 replyId 对应的回复的 content
     * @param replyId
     * @param content
     * @return 成功返回 true，否则返回 false
     */
    boolean updateContent(String replyId, String content);

    /**
     * 删除 replyId 对应的回复
     * @param replyId
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delReplyById(String replyId);

    /**
     * 删除所有回复
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delAllReplies();

    /**
     *
     * @param replyId
     * @return 如果存在 ID 为指定的 replyId 的回复，返回 true，否则返回 false
     */
    boolean hasId(String replyId);
}
