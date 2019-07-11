package com.nikesu.untitled.biz;

import com.mysql.cj.util.StringUtils;
import com.nikesu.untitled.dao.impl.*;
import com.nikesu.untitled.entity.*;
import com.nikesu.untitled.util.Sha1Encoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 倪可塑
 */
public class BbsBiz {
    private User user;
    private UserGroup userGroup;
    private Forum forum;
    private Post post;

    private static final int POST_PER_PAGE = 5;
    private static final int REPLY_PER_PAGE = 5;
    private static final int USER_PER_PAGE = 5;

    private static final boolean DEBUG = false;

    private static Map<String, String> idToNameMap = new HashMap<>();

    /**
     * 不允许通过 new 得到 BbsBiz 对象，因此将构造函数设为私有。
     * 应通过 BbsBiz 的静态方法 signIn() 得到 BbsBiz 对象。
     */
    private BbsBiz() {}

    /**
     * 检测 userName 是否合法，一个合法的 userName 必须满足以下所有条件：
     * 1. 不能与现有用户的 userName 重复；
     * 2. 只能包含英文字母、数字和下划线；
     * 3. 只能以字母开头；
     * 4. 长度必须大于等于 3，小于等于 20。
     * 注意调用者需保证 userName 不为 null
     * @author 白雪婷
     * @param userName
     * @return 合法返回 true，否则返回 false
     */
    public static boolean isValidUserName(String userName) {
        assert !StringUtils.isNullOrEmpty(userName);
        if (UserDaoImpl.getInstance().hasName(userName)) {
            return false;
        }
        if(!isletter(userName.charAt(0))) {//只能以字母开头
            return false;
        }
        if(userName.length()<3 || userName.length()>20) {//长度必须大于等于 3，小于等于 20
            return false;
        }
        for (int i = 0; i < userName.length(); i++) {

            if (!isletter(userName.charAt(i)) //只能包含英文字母
                    && !Character.isDigit(userName.charAt(i)) //数字
                    && userName.charAt(i)!='_') {//下划线
                return false;
            }
        }
        return true;
    }

    /**
     * 检测 password 是否合法，一个合法的密码必须满足以下所有条件：
     * 1. 只能包含字母、数字和特殊字符（ `~!@#$%^&*()_+-={}|[]\:";'<>?,./）；
     * 2. 必须包含大写字母、小写字母、数字、特殊字符中的至少三种；
     * 3. 长度必须大于等于 6，小于等于 20。
     * 注意调用者需保证 password 不为 null
     * @author 白雪婷
     * @param password
     * @return 合法返回 true，否则返回 false
     */
    public static boolean isValidPassword(String password) {
        assert !StringUtils.isNullOrEmpty(password);
        if (DEBUG) {
            return true;
        }
        if(password.length()<6 || password.length()>20) {//长度必须大于等于 6，小于等于 20
            //	System.out.println("长度必须大于等于 6，小于等于 20");
            return false;
        }
        int c=0;
        int special=0;//特殊字符
        int digit=0;//数字
        int lower=0;//小写字母
        int upper=0;//大写字母
        for (int i = 0; i < password.length(); i++) {
            c=password.charAt(i);
            if (0x20>c || c>0x7E) {//只能包含字母、数字和特殊字符
                //	System.out.println("只能包含字母、数字和特殊字符" +c);
                return false;
            }
            if (Character.isLowerCase(c)) {
                lower=1;
            }else if(Character. isUpperCase(c)){
                upper=1;
            }else if (Character.isDigit(c)) {
                digit=1;
            }else {
                special=1;
            }
        }
        if (lower+upper+digit+special<3) {
            //	System.out.println("至少包含字母、数字和特殊字符中的三种");
            return false;
        }
        return true;
    }

    private static boolean isletter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 检查 userGroupId 是否合法，一个合法的 userGroupId 必须已经
     * 存在于数据库内
     * @param userGroupId 用户组 ID，不能为 null
     * @return 合法返回 true，否则返回 false
     */
    public static boolean isValidUserGroupId(String userGroupId) {
        assert !StringUtils.isNullOrEmpty(userGroupId);
        return UserGroupDaoImpl.getInstance().hasId("userGroupId");
    }

    /**
     * 用户注册，根据传入的用户名、密码、邮箱、用户组 ID 生成一个新的 User 对象，
     * 并将其添加到数据库中。
     * 注意：
     * 1. signUp() 方法不对传入参数的合法性进行检查，应由调用者保证用户名、密码和
     * 用户组 ID 的合法性；
     * 2. signUp() 方法应该自动设置新 User 对象的 regDate 为当前的日期时间
     * （格式为 "yyyy-MM-dd HH:mm:ss" 字符串，如 "1970-1-1 12:00"）；
     * 3. 用户的密码应经过两次 SHA-1 加密后存储，以保证即使不同用户使用同样的密码，
     * 加密后的密码也不会相同。使用 getSha1(getSha1(password) + userName)
     * 来得到加密后的密码。
     * @param userName 用户名，不能为 null
     * @param password 密码，不能为 null
     * @param email 邮箱，可以是空字符串或 null，在此情况下数据库中用户的邮箱字段
     *              会被设置为默认值 ""
     * @param userGroupId 用户组 ID，可以是空字符串或 null，在此情况下数据库中
     *                    用户的用户组会被设置为 "1"（即普通用户）
     * @return 成功返回 true，否则返回 false
     */
    public static boolean signUp(String userName, String password, String email, String userGroupId) {
        assert isValidUserName(userName);
        assert isValidPassword(password);
        assert isValidUserGroupId(userGroupId);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(Sha1Encoder.getSha1(Sha1Encoder.getSha1(password) + userName));
        user.setEmail(email);
        user.setUserGroupId(userGroupId);
        user.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return UserDaoImpl.getInstance().addUser(user);
    }

    /**
     * 用户登录，根据传入的用户名和密码在数据库中查找对应的 User，如果用户名和密码
     * 正确，生成一个 BbsBiz 对象，并将其 user 和 userGroup 字段设置为相应的
     * User 对象和 UserGroup 对象。
     * 注意，应先使用 getSha1(getSha1(password) + userName)得到加密后的
     * 密码，然后再将其和数据库内存储的内容进行比较。
     * @param userName
     * @param password
     * @return 成功返回一个 BbsBiz 对象，否则返回 null
     */
    public static BbsBiz signIn(String userName, String password) {
        assert !StringUtils.isNullOrEmpty(userName);
        assert !StringUtils.isNullOrEmpty(password);

        User user = UserDaoImpl.getInstance().getUserByName(userName);

        if (user == null) {
            return null;
        }

        if (Sha1Encoder.getSha1(Sha1Encoder.getSha1(password) + userName).equals(user.getPassword())) {
            BbsBiz bbsBiz = new BbsBiz();
            bbsBiz.user = user;
            bbsBiz.userGroup = UserGroupDaoImpl.getInstance().getUserGroupById(user.getUserGroupId());
            return bbsBiz;
        }

        return null;
    }

    /**
     * 修改用户 this.user 的密码为 newPassword。
     * 注意：应先使用 getSha1(getSha1(newPassword) + userName)得到加密后的
     * 密码，然后再存储到数据库。
     * @param newPassword
     * @return 成功返回 true，否则返回 false
     */
    public boolean changePassword(String newPassword) {
        assert !StringUtils.isNullOrEmpty(newPassword);
        assert this.user != null;
        assert !StringUtils.isNullOrEmpty(this.user.getUserId());

        return UserDaoImpl.getInstance().updatePassword(this.user.getUserId(),
                Sha1Encoder.getSha1(Sha1Encoder.getSha1(newPassword) + this.user.getUserName()));
    }

    /**
     * 修改用户 this.user 的邮箱为 newEmail
     * @param newEmail
     * @return 成功返回 true，否则返回 false
     */
    public boolean changeEmail(String newEmail) {
        assert !StringUtils.isNullOrEmpty(newEmail);
        assert this.user != null;
        assert !StringUtils.isNullOrEmpty(this.user.getUserId());

        return UserDaoImpl.getInstance().updateEmail(this.user.getUserId(), newEmail);
    }

    /**
     * 禁言 userName 指定的用户（将其移到“已禁言”用户组，即 ID 为 3 的用户组）
     *
     * @param userId
     * @return 成功返回 true，不成功（如 user 没有禁言权限时）返回 false
     */
    public boolean banUser(String userId) {
        if (this.userGroup.getAllowBanUser().equals("0")) {
            return false;
        }
        return UserDaoImpl.getInstance().updateUserGroupId(userId, "3");
    }

    public boolean moveUserto(String userId, String userGroupId) {
        if (this.userGroup.getAllowEditUser().equals("0")) {
            return false;
        }
        if (UserGroupDaoImpl.getInstance().getUserGroupById(userGroupId) == null) {
            return false;
        }
        return UserDaoImpl.getInstance().updateUserGroupId(userId, userGroupId);
    }

    /**
     * 删除帖子 post
     * @param post
     * @return 成功返回 true，不成功（如 user 没有权限时）返回 false
     */
    public boolean delPost(Post post) {
        if (this.userGroup.getAllowDelPost().equals("0")) {
            return false;
        }
        return PostDaoImpl.getInstance().delPostById(post.getPostId());
    }

    /**
     * 添加一个新版块 forum
     * @param forum
     * @return 成功返回 true，不成功（如 user 没有权限时）返回 false
     */
    public boolean addForum(Forum forum) {
        if (this.userGroup.getAllowEditForum().equals("0")) {
            return false;
        }
        return ForumDaoImpl.getInstance().addForum(forum);
    }

    public boolean addForum(String forumName) {
        Forum forum = new Forum();
        forum.setForumName(forumName);
        return ForumDaoImpl.getInstance().addForum(forum);
    }

    /**
     * 删除版块 forum
     * @param forum
     * @return 成功返回 true，不成功（如 user 没有权限时）返回 false
     */
    public boolean delForum(Forum forum) {
        assert this.userGroup.getAllowEditForum().equals("1");
        return ForumDaoImpl.getInstance().delForumById(forum.getForumId());
    }

    public boolean delForum(String forumId) {
        return ForumDaoImpl.getInstance().delForumById(forumId);
    }

    /**
     * 删除用户 user
     * @param user
     * @return
     */
    public boolean delUser(User user) {
        if (this.userGroup.getAllowEditUser().equals("0")) {
            return false;
        }
        return UserDaoImpl.getInstance().delUserById(user.getUserId());
    }

    public boolean delUser(String userId) {
        return UserDaoImpl.getInstance().delUserById(userId);
    }

    /**
     * 返回一个所有板块的列表
     * @return
     */
    public ArrayList<Forum> getForums() {
        return ForumDaoImpl.getInstance().getForums();
    }

    /**
     * 返回一个所有用户的列表
     * @return
     */
    public ArrayList<User> getUsers() {
        return UserDaoImpl.getInstance().getUsers();
    }

    public ArrayList<User> getUsers(int page) {
        return UserDaoImpl.getInstance().getUsers(USER_PER_PAGE * (page - 1), USER_PER_PAGE);
    }

    public ArrayList<UserGroup> getUserGroups() {
        return UserGroupDaoImpl.getInstance().getUserGroups();
    }

    /**
     * 访问指定的 forum 板块
     * （把 this.forum 变量设为参数 forum）
     * @param forum
     * @return
     */
    public boolean visitForum(Forum forum) {
        this.forum = forum;
        return true;
    }

    /**
     * 返回 forum 板块下的第 page 页的帖子列表。
     * 注意：每页显示的帖子数量定义在变量 POST_PER_PAGE 中。
     * @param page
     * @return
     */
    public ArrayList<Post> getPosts(int page) {
        return PostDaoImpl.getInstance().getPostsByForum(this.forum.getForumId(),
                POST_PER_PAGE * (page - 1), POST_PER_PAGE);
    }

    /**
     * 访问指定的 post 帖子
     * （把 this.post 变量设为参数 post）
     * @param post
     * @return
     */
    public boolean visitPost(Post post) {
        this.post = post;
        return true;
    }

    /**
     * 返回 post 帖子下的第 page 页回复的列表。
     * 注意：每页显示的回复数量定义在变量 REPLY_PER_PAGE 中。
     * @param page
     * @return
     */
    public ArrayList<Reply> getReplies(int page) {
        return ReplyDaoImpl.getInstance().getRepliesByPost(this.post.getPostId(),
                REPLY_PER_PAGE * (page - 1), REPLY_PER_PAGE);
    }

    public ArrayList<Reply> getReplies() {
        return ReplyDaoImpl.getInstance().getRepliesByPost(this.post.getPostId());
    }

    public ArrayList<Reply> getReplies(String postId) {
        return ReplyDaoImpl.getInstance().getRepliesByPost(postId);
    }

    public String getUserNameById(String userId) {
        if (!idToNameMap.containsKey(userId)) {
            idToNameMap.put(userId, UserDaoImpl.getInstance().getUserById(userId).getUserName());
        }
        return idToNameMap.get(userId);
    }

    public String getUserGroupNameById(String userGroupId) {
        return UserGroupDaoImpl.getInstance().getUserGroupById(userGroupId).getUserGroupName();
    }

    /**
     * 发表帖子
     * 注意：必须已经访问了某个板块（this.forum）才能发表帖子
     * @param post
     * @return 成功返回 true，否则返回 false
     */
    public boolean addPost(Post post) {
        return PostDaoImpl.getInstance().addPost(post);
    }

    public boolean topPost(Post post) {
        return PostDaoImpl.getInstance().updateTop(post.getPostId(), post.getTop());
    }

    public boolean updatePostContent(Post post) {
        return PostDaoImpl.getInstance().updateContent(post.getPostId(), post.getContent());
    }

    /**
     * 发表回复
     * 注意：必须已经访问了某个帖子（this.post）才能发表回复
     * @param reply
     * @return
     */
    public boolean addReply(Reply reply) {
        return ReplyDaoImpl.getInstance().addReply(reply);
    }

    public boolean hasUserId(String userId) {
        return UserDaoImpl.getInstance().hasId(userId);
    }

    public boolean hasUserGroupId(String userGroupId) {
        return UserGroupDaoImpl.getInstance().hasId(userGroupId);
    }

    public boolean hasForumId(String forumId) {
        return ForumDaoImpl.getInstance().hasId(forumId);
    }

    public User getUser() {
        return user;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public Forum getForum() {
        return forum;
    }

    public Post getPost() {
        return post;
    }
}