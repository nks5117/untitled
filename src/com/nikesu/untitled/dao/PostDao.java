package com.nikesu.untitled.dao;

import com.nikesu.untitled.entity.Post;

import java.util.ArrayList;

/**
 * @author 倪可塑
 * @version 1.0
 */
public interface PostDao {
    /**
     * 向数据库中添加一个新帖子
     * @param post 要添加至数据库的 Post 对象，addPost() 方法不对 post
     *             各个字段的合法性进行检查，故为了确保添加成功，传入的参数
     *             post 必须至少已经设置好了合法的 forumId、userId、title
     *             和 content。
     * @return 成功返回 true，否则返回 false
     */
    boolean addPost(Post post);

    /**
     * 根据 postId 获取 Post 对象
     * @param postId
     * @return postId 对应的 Post 对象，如不存在返回 null
     */
    Post getPostById(String postId);

    /**
     * 获取所有帖子。
     * @return 按 top 和 postId 递增排序（即置顶帖在最前）的所有帖子的
     *         ArrayList，如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Post> getPosts();

    /**
     * 对所有帖子先按 top 排序，再按 postId 排序（即置顶帖在最前），返回一个包含
     * 从第 beg 个帖子到第 end - 1 个帖子的 ArrayList（从 0 开始计数）。
     * 如 getPosts(0,2) 将返回一个由 posts[0] 和 posts[1] 构成的 ArrayList。
     * 如果参数 beg 大于等于帖子总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于帖子总数，将返回从第 beg 个帖子到最后一个帖子的 ArrayList。
     * @param beg
     * @param end
     * @return
     */
    ArrayList<Post> getPosts(int beg, int end);

    /**
     * 获取指定版块的所有帖子。
     * @param forumId
     * @return 该板块下按 top 和 postId 递增排序（即置顶帖在最前）的所有帖子的
     *         ArrayList，如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Post> getPostsByForum(String forumId);

    /**
     * 对指定版块的所有帖子先按 top 排序，再按 postId 排序（即置顶帖在最前），
     * 返回一个包含从第 beg 个帖子到第 end - 1 个帖子的 ArrayList（从 0 开始计数）。
     * 如果参数 beg 大于等于该板块帖子总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于该板块帖子总数，将返回从第 beg 个帖子到最后一个帖子的 ArrayList。
     * @param forumId
     * @param beg
     * @param end
     * @return
     */
    ArrayList<Post> getPostsByForum(String forumId, int beg, int end);

    /**
     * 获取指定用户的所有帖子。
     * @param userId
     * @return 该用户发表的按 top 和 postId 递增排序（即置顶帖在最前）的所有帖子的
     *         ArrayList，如果数据库为空，返回一个长度为 0 的空 ArrayList。
     */
    ArrayList<Post> getPostsByUser(String userId);

    /**
     * 对指定用户的所有帖子先按 top 排序，再按 postId 排序（即置顶帖在最前），
     * 返回一个包含从第 beg 个帖子到第 end - 1 个帖子的 ArrayList（从 0 开始计数）。
     * 如果参数 beg 大于等于该用户发表的帖子总数，将返回一个长度为 0 的空 ArrayList；
     * 如果参数 end 大于该用户发表的帖子总数，将返回从第 beg 个帖子到最后一个帖子的 ArrayList。
     * @param userId
     * @param beg
     * @param end
     * @return
     */
    ArrayList<Post> getPostsByUser(String userId, int beg, int end);

    /**
     * 更新 postId 对应的帖子的 title
     * @param postId
     * @param title
     * @return 成功返回 true，否则返回 false
     */
    boolean updateTitle(String postId, String title);

    /**
     * 更新 postId 对应的帖子的 content
     * @param postId
     * @param content
     * @return 成功返回 true，否则返回 false
     */
    boolean updateContent(String postId, String content);

    /**
     * 更新 postId 对应的帖子的 top 属性
     * @param postId
     * @param top 只能是 "0" 或者 "1"
     * @return 成功返回 true，否则返回 false
     */
    boolean updateTop(String postId, String top);

    /**
     * 删除 postId 对应的帖子
     * @param postId
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delPostById(String postId);

    /**
     * 删除所有帖子
     * @return 删除成功返回 true，否则返回 false
     */
    boolean delAllPosts();

    /**
     *
     * @param postId
     * @return 如果存在 ID 为指定的 postId 的帖子，返回 true，否则返回 false
     */
    boolean hasId(String postId);
}
