package com.nikesu.untitled;

import com.mysql.cj.util.StringUtils;
import com.nikesu.untitled.biz.BbsBiz;
import com.nikesu.untitled.dao.impl.UserGroupDaoImpl;
import com.nikesu.untitled.entity.*;
import com.nikesu.untitled.util.SensitiveWordFilter;
import com.nikesu.untitled.util.StringFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static BbsBiz bbsBiz;
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Forum> forums;
    private static ArrayList<Post> posts;
    private static final int MENULINEWIDTH = 40;
    private static final int FORUMLINEWIDTH = 100;
    private static final int POSTLINEWIDTH = 100;
    private static final int REPLYLINEWIDTH = 30;

    public static void main(String[] args) {

        while (true) {
            showMainMenu();
            int a = scanner.nextInt();
            switch (a) {
                case 1:
                    signIn();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    showAbout();
                    break;
                case 0:
                    System.out.println("Bye.");
                    System.exit(0);
                    break;
                default:
            }

            if (bbsBiz != null) {
                forums = bbsBiz.getForums();
                logout:
                while (true) {
                    showSignedMenu();
                    int b = scanner.nextInt();
                    switch (b) {
                        case 1:
                            userPage();
                            break;
                        case 0:
                            bbsBiz = null;
                            break logout;
                        default:
                            visitForum(b - 2);
                    }
                }
            }
        }
    }

    private static void showMainMenu() {
        System.out.println(StringFormatter.alignCenter("欢迎来到 untitled BBS", '-', MENULINEWIDTH));
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 关于");
        System.out.println("0. 退出");
        System.out.println(StringFormatter
                .alignCenter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        '-', MENULINEWIDTH));
    }

    private static void showSignedMenu() {
        System.out.println(StringFormatter.alignCenter("欢迎: " + bbsBiz.getUser().getUserName(), '-', MENULINEWIDTH));
        System.out.println("1. 我的主页");
        int i = 2;
        for (Forum forum : forums) {
            System.out.println("" + i +". " + forum.getForumName());
            i++;
        }
        System.out.println("0. 登出");

        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
    }

    private static void showUserMenu() {
        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
        System.out.println("用户名：" + bbsBiz.getUser().getUserName());
        System.out.println("用户组：" + bbsBiz.getUserGroup().getUserGroupName());
        System.out.println("邮箱：" + bbsBiz.getUser().getEmail());
        System.out.println("注册时间：" + bbsBiz.getUser().getRegDate());
        System.out.println("1. 修改信息");
        if ((bbsBiz.getUserGroup().getAllowEditForum().equals("1")
                || bbsBiz.getUserGroup().getAllowBanUser().equals("1")
                || bbsBiz.getUserGroup().getAllowEditUser().equals("1"))) {
            System.out.println("2. 论坛管理");
        }
        System.out.println("0. 返回");
        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
    }

    private static void showBbsManageMenu() {
        System.out.println(StringFormatter.alignCenter("论坛管理", '-', MENULINEWIDTH));
        if (bbsBiz.getUserGroup().getAllowEditForum().equals("1")) {
            System.out.println("1. 编辑版块");
        }
        if (bbsBiz.getUserGroup().getAllowBanUser().equals("1") || bbsBiz.getUserGroup().getAllowEditUser().equals("1")) {
            System.out.println("2. 编辑用户");
        }
        System.out.println("0. 返回");
        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
    }

    private static void showForums() {
        System.out.println("+--------+-----------------------------+");
        System.out.println("|" + StringFormatter.alignCenter("板块ID", ' ', 8) + "|"
                + StringFormatter.alignCenter("板块名", ' ', 29) + "|");
        for (Forum forum : forums) {
            System.out.println("+--------+-----------------------------+");
            System.out.println("|" + StringFormatter.alignLeft(forum.getForumId(), ' ', 8) + "|"
                    + StringFormatter.alignLeft(forum.getForumName(), ' ', 29) + "|");
        }
        System.out.println("+--------+-----------------------------+");
    }

    private static void showPosts() {
        for (int i = 0; i < posts.size(); i++) {
            String title = posts.get(i).getTitle();
            if (StringFormatter.strWidth(title) > FORUMLINEWIDTH - 10) {
                title = StringFormatter.alignCenter(title, ' ', FORUMLINEWIDTH - 10) + "...";
            }
            System.out.println(StringFormatter.alignLeft(
                    (i+1) + ". "
                            + (posts.get(i).getTop().equals("1") ? "[顶]" : "")
                            + title, ' ', FORUMLINEWIDTH));
            System.out.println(StringFormatter.alignLeft(posts.get(i).getEditTime().substring(0, 10), ' ', FORUMLINEWIDTH, 3, ' '));
        }
    }

    private static void reply(Post post) {
        while (true) {
            System.out.println(StringFormatter.alignCenter("回复列表", '-', REPLYLINEWIDTH));
            ArrayList<Reply> list = bbsBiz.getReplies(post.getPostId());
            if (list.size() == 0) {
                System.out.println("没有回复");
                System.out.println(StringFormatter.splitLine("-", REPLYLINEWIDTH));
            }
            for (int i = 0; i < list.size(); i++) {
                System.out.println(StringFormatter
                        .alignLeft(bbsBiz.getUserNameById(list.get(i).getUserId()),
                                '-', REPLYLINEWIDTH / 2)
                        + StringFormatter.alignRight((i + 1) + " 楼",
                        '-', REPLYLINEWIDTH / 2));
                String[] lines = list.get(i).getContent().split("\n");
                for (String line : lines) {
                    System.out.println(StringFormatter.wrapText(line, REPLYLINEWIDTH));
                }
                System.out.println(StringFormatter.alignRight(list.get(i).getEditTime(), '-', REPLYLINEWIDTH));
            }

            if (bbsBiz.getUserGroup().getAllowReply().equals("1")) {
                System.out.print("1. 发表回复\t");
            }
            System.out.println("0. 返回");
            System.out.println(StringFormatter.splitLine("-", REPLYLINEWIDTH));
            int a = scanner.nextInt();
            switch (a) {
                case 1:
                    if (bbsBiz.getUserGroup().getAllowReply().equals("0")) {
                        System.out.println("没有回复权限！");
                    } else {
                        System.out.println(StringFormatter.alignCenter("发表回复", '-', REPLYLINEWIDTH));
                        System.out.println("请输入回复内容（输入\"\\n.\\n\"来结束）");
                        String s;
                        StringBuilder sb = new StringBuilder();
                        scanner.nextLine();
                        while (!(s = scanner.nextLine()).equals(".")) {
                            sb.append(s).append("\n");
                        }
                        Reply reply = new Reply();
                        reply.setPostId(post.getPostId());
                        reply.setUserId(bbsBiz.getUser().getUserId());
                        reply.setContent(SensitiveWordFilter.replaceSensitiveWords(sb.toString()));
                        reply.setEditTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        bbsBiz.addReply(reply);
                        System.out.println("回复成功！");
                        break;
                    }
                case 0:
                    return;
                default:
            }
        }
    }

    private static void visitPost(int i) {
        if (i < 0 || i >= posts.size()) {
            System.out.println("PAGE NOT FOUND");
            return;
        }

        else {
            while (true) {
                String title = posts.get(i).getTitle();
                System.out.println(StringFormatter.splitLine("-", POSTLINEWIDTH));
                System.out.println(StringFormatter.wrapText(title, POSTLINEWIDTH));
                System.out.println("作者：" + bbsBiz.getUserNameById(posts.get(i).getUserId()));
                System.out.println("发表时间：" + posts.get(i).getEditTime());
                System.out.println(StringFormatter.splitLine("-", POSTLINEWIDTH));
                String[] lines = posts.get(i).getContent().split("\n");
                for (String line : lines) {
                    System.out.println(StringFormatter.wrapText(line, POSTLINEWIDTH));
                }
                System.out.println(StringFormatter.splitLine("-", POSTLINEWIDTH));
                System.out.print("1. 回复\t");
                if (bbsBiz.getUserGroup().getAllowTopPost().equals("1")) {
                    if (posts.get(i).getTop().equals("0")) {
                        System.out.print("2. 置顶\t");
                    }
                    else {
                        System.out.print("2. 取消置顶\t");
                    }
                }
                if (bbsBiz.getUserGroup().getAllowEditPost().equals("1")) {
                    System.out.print("3. 编辑\t");
                }
                if (bbsBiz.getUserGroup().getAllowDelPost().equals("1")) {
                    System.out.print("4. 删除\t");
                }
                System.out.println("0. 返回");
                System.out.println(StringFormatter.splitLine("-", POSTLINEWIDTH));
                int a = scanner.nextInt();
                switch (a) {
                    case 1:
                        reply(posts.get(i));
                        break;
                    case 2:
                        if (bbsBiz.getUserGroup().getAllowTopPost().equals("0")) {
                            System.out.println("指令错误！");
                        } else {
                            if (posts.get(i).getTop().equals("0")) {
                                posts.get(i).setTop("1");
                            }
                            else {
                                posts.get(i).setTop("0");
                            }
                            bbsBiz.topPost(posts.get(i));
                            System.out.println("成功！");
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 3:
                        if (bbsBiz.getUserGroup().getAllowEditPost().equals("0")) {
                            System.out.println("指令错误！");
                        } else {
                            System.out.println("请输入新正文（输入\"\\n.\\n\"来结束）");
                            String s;
                            StringBuilder sb = new StringBuilder();
                            scanner.nextLine();
                            while (!(s = scanner.nextLine()).equals(".")) {
                                sb.append(s).append("\n");
                            }
                            posts.get(i).setContent(SensitiveWordFilter.replaceSensitiveWords(sb.toString()));
                            bbsBiz.updatePostContent(posts.get(i));
                            System.out.println("编辑成功！");
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 4:
                        if (bbsBiz.getUserGroup().getAllowDelPost().equals("0")) {
                            System.out.println("指令错误！");
                        } else {
                            bbsBiz.delPost(posts.get(i));
                            System.out.println("删除成功！");
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return;
                    case 0:
                        return;
                    default:
                }
            }
        }
    }

    private static void visitForum(int i) {
        Forum forum = forums.get(i);
        bbsBiz.visitForum(forum);
        int page = 1;
        boolean hasNextPage;
        boolean hasPrevPage;
        while (true) {
            System.out.println(StringFormatter.alignCenter(forum.getForumName(), '-', FORUMLINEWIDTH));
            hasNextPage = true;
            hasPrevPage = (page > 1);
            posts = bbsBiz.getPosts(page);
            if (posts == null || posts.size() == 0) {
                System.out.println("没有更多帖子了");
                hasNextPage = false;
            }
            else {
                showPosts();
            }
            System.out.println(StringFormatter.alignCenter("第 " + page + " 页", '-', FORUMLINEWIDTH));
            System.out.print("0: 返回\t");
            if (bbsBiz.getUserGroup().getAllowPost().equals("1")) {
                System.out.print("w. 发帖\t");
            }
            if (hasNextPage && hasPrevPage) {
                System.out.println("p: 上一页\tn: 下一页");
            } else if (hasNextPage && !hasPrevPage) {
                System.out.println("n: 下一页");
            } else if (!hasNextPage && hasPrevPage) {
                System.out.println("p: 上一页");
            } else {
                System.out.println("");
            }
            System.out.println(StringFormatter.splitLine("-", FORUMLINEWIDTH));

            String s = scanner.next();
            if (s.equals("0")) {
                return;
            } else if (s.equals("n")) {
                if (hasNextPage) {
                    page++;
                }
                else {
                    System.out.println("已经是最后一页");
                }
            } else if (s.equals("p")) {
                if (hasPrevPage) {
                    page--;
                }
                else {
                    System.out.println("已经是第一页");
                }
            } else if (s.equals("w")) {
                if (bbsBiz.getUserGroup().getAllowPost().equals("0")) {
                    System.out.println("您没有发帖权限！");
                } else {
                    writePost();
                    page = 1;
                }
            } else {
                visitPost(Integer.parseInt(s)-1);
            }
        }
    }

    private static void writePost() {
        System.out.println(StringFormatter.alignCenter("发表帖子", '-', POSTLINEWIDTH));
        Post post = new Post();
        scanner.nextLine();
        System.out.println("请输入标题：");
        post.setTitle(SensitiveWordFilter.replaceSensitiveWords(scanner.nextLine()));
        System.out.println("请输入正文（输入\"\\n.\\n\"来结束）");
        String s;
        StringBuilder sb = new StringBuilder();
        while (!(s = scanner.nextLine()).equals(".")) {
            sb.append(s).append("\n");
        }
        post.setContent(SensitiveWordFilter.replaceSensitiveWords(sb.toString()));
        post.setForumId(bbsBiz.getForum().getForumId());
        post.setTop("0");
        post.setUserId(bbsBiz.getUser().getUserId());
        post.setEditTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        bbsBiz.addPost(post);
    }

    private static void showUsers() {
        ArrayList<User> list = bbsBiz.getUsers();
        showUsers(list);
    }

    private static void showUsers(int page) {
        ArrayList<User> list = bbsBiz.getUsers(page);
        showUsers(list);
    }

    private static void showUsers(ArrayList<User> list) {
        System.out.println("+--------+---------------+--------------------+----------+----------+");
        System.out.println("|" + StringFormatter.alignCenter("用户ID", ' ', 8) + "|"
                + StringFormatter.alignCenter("用户名", ' ', 15) + "|"
                + StringFormatter.alignCenter("邮箱", ' ', 20) + "|"
                + StringFormatter.alignCenter("注册时间", ' ', 10) + "|"
                + StringFormatter.alignCenter("用户组", ' ', 10) + "|");
        for (User user : list) {
            System.out.println("+--------+---------------+--------------------+----------+----------+");
            System.out.println("|" + StringFormatter.alignLeft(user.getUserId(), ' ', 8) + "|"
                    + StringFormatter.alignLeft(user.getUserName(), ' ', 15) + "|"
                    + StringFormatter.alignLeft(user.getEmail(), ' ', 20) + "|"
                    + StringFormatter.alignLeft(user.getRegDate(), ' ', 10) + "|"
                    + StringFormatter.alignLeft(bbsBiz.getUserGroupNameById(user.getUserGroupId()),
                    ' ', 10) + "|");
        }
        System.out.println("+--------+---------------+--------------------+----------+----------+");
    }

    private static void showUserGroups() {
        ArrayList<UserGroup> list = bbsBiz.getUserGroups();
        System.out.println("+----+--------+----+----+----+----+----+----+----+----+");
        System.out.println("|" + StringFormatter.alignCenter("ID", ' ', 4) + "|"
                + StringFormatter.alignCenter("用户组名", ' ', 8) + "|"
                + StringFormatter.alignCenter("发表", ' ', 4) + "|"
                + StringFormatter.alignCenter("回复", ' ', 4) + "|"
                + StringFormatter.alignCenter("编辑", ' ', 4) + "|"
                + StringFormatter.alignCenter("置顶", ' ', 4) + "|"
                + StringFormatter.alignCenter("删除", ' ', 4) + "|"
                + StringFormatter.alignCenter("管理", ' ', 4) + "|"
                + StringFormatter.alignCenter("禁言", ' ', 4) + "|"
                + StringFormatter.alignCenter("管理", ' ', 4) + "|");
        System.out.println("|" + StringFormatter.alignCenter("  ", ' ', 4) + "|"
                + StringFormatter.alignCenter("  ", ' ', 8) + "|"
                + StringFormatter.alignCenter("帖子", ' ', 4) + "|"
                + StringFormatter.alignCenter("帖子", ' ', 4) + "|"
                + StringFormatter.alignCenter("帖子", ' ', 4) + "|"
                + StringFormatter.alignCenter("帖子", ' ', 4) + "|"
                + StringFormatter.alignCenter("帖子", ' ', 4) + "|"
                + StringFormatter.alignCenter("用户", ' ', 4) + "|"
                + StringFormatter.alignCenter("用户", ' ', 4) + "|"
                + StringFormatter.alignCenter("板块", ' ', 4) + "|");

        for (UserGroup userGroup : list) {
            System.out.println("+----+--------+----+----+----+----+----+----+----+----+");
            System.out.println("|" + StringFormatter.alignLeft(userGroup.getUserGroupId(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getUserGroupName(), ' ', 8) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowPost(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowReply(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowEditPost(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowTopPost(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowDelPost(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowEditUser(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowBanUser(), ' ', 4) + "|"
                    + StringFormatter.alignLeft(userGroup.getAllowEditForum(), ' ', 4) + "|");
        }
        System.out.println("+----+--------+----+----+----+----+----+----+----+----+");
    }

    private static void userPage() {
        while (true) {
            showUserMenu();
            int a = scanner.nextInt();
            switch (a) {
                case 0:
                    return;
                case 1:
                    editUserInfo();
                    break;
                case 2:
                    if ((bbsBiz.getUserGroup().getAllowEditForum().equals("1")
                            || bbsBiz.getUserGroup().getAllowBanUser().equals("1")
                            || bbsBiz.getUserGroup().getAllowEditUser().equals("1"))) {
                        bbsManage();
                    }
                    break;
                default:
            }
        }
    }

    private static void showEditUserInfoMenu() {
        System.out.println(StringFormatter.alignCenter("修改信息", '-', MENULINEWIDTH));
        System.out.println("1. 修改邮箱");
        System.out.println("2. 修改密码");
        System.out.println("0. 返回");
        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
    }

    private static void editUserInfo() {
        while (true) {
            showEditUserInfoMenu();
            int a = scanner.nextInt();
            switch (a) {
                case 1:
                    System.out.println("请输入新邮箱：");
                    bbsBiz.changeEmail(scanner.next());
                    break;
                case 2:
                    System.out.println("请输入新密码：");
                    bbsBiz.changePassword(scanner.next());
                    break;
                case 0:
                    return;
                default:
            }
        }
    }

    private static void bbsManage() {
        while (true) {
            showBbsManageMenu();
            int a = scanner.nextInt();
            switch (a) {
                case 1:
                    editForum();
                    forums = bbsBiz.getForums();
                    break;
                case 2:
                    editUser();
                    break;
                case 0:
                    return;
                default:
            }
        }
    }

    private static void editForum() {
        System.out.println(StringFormatter.alignCenter("编辑板块", '-', MENULINEWIDTH));
        System.out.println("全部板块信息如下：");
        showForums();
        System.out.println("1. 添加板块");
        System.out.println("2. 删除板块");
        System.out.println("0. 返回");
        int a = scanner.nextInt();
        switch (a) {
            case 1:
                System.out.println("输入新板块名字：");
                String forumName = scanner.next();
                bbsBiz.addForum(forumName);
                System.out.println("添加成功！");
                break;
            case 2:
                System.out.println("输入板块ID：");
                String forumId = scanner.next();
                while (!bbsBiz.hasForumId(forumId)) {
                    System.out.println("板块不存在，请重新输入！");
                    System.out.println("输入板块ID（输入-1来返回）：");
                    forumId = scanner.next();
                    if (forumId.equals("-1")) {
                        return;
                    }
                }
                bbsBiz.delForum(forumId);
                System.out.println("删除成功！");
                break;
            case 0:
                return;
            default:
        }
    }

    private static void editUser() {
        System.out.println(StringFormatter.alignCenter("编辑用户", '-', MENULINEWIDTH));
        System.out.println("全部用户信息如下：");
        showUsers();
        System.out.print("请输入要编辑的用户ID（输入-1来返回）：");
        String userId = scanner.next();
        if (userId.equals("-1")) {
            return;
        }
        while (!bbsBiz.hasUserId(userId)) {
            System.out.println("用户不存在，请重新输入！");
            System.out.print("请输入要编辑的用户ID（输入-1来返回）：");
            userId = scanner.next();
            if (userId.equals("-1")) {
                return;
            }
        }
        System.out.println("请选择操作：1. 删除用户； 2. 禁言用户； 3. 更改用户组");
        int b = scanner.nextInt();
        switch (b) {
            case 1:
                bbsBiz.delUser(userId);
                System.out.println("删除成功！");
                break;
            case 2:
                bbsBiz.banUser(userId);
                System.out.println("禁言成功！");
                break;
            case 3:
                System.out.println("当前用户组（1 表示允许，0 表示不允许）：");
                showUserGroups();
                System.out.println("请输入用户组 ID");
                String userGroupId = scanner.next();
                while (!bbsBiz.hasUserGroupId(userGroupId)) {
                    System.out.println("用户组不存在，请重新输入！");
                    System.out.print("请输入用户组 ID（输入-1来返回）：");
                    userGroupId = scanner.next();
                    if (userGroupId.equals("-1")) {
                        return;
                    }
                }
                bbsBiz.moveUserto(userId, userGroupId);
                System.out.println("修改成功！");
                break;
            default:
        }
    }

    private static void showAbout() {
        System.out.println(StringFormatter.alignCenter("About", '-', MENULINEWIDTH));
        System.out.println("untitled BBS 管理系统");
        System.out.println("版本: 1.0.0");
        System.out.println("作者: 倪可塑、王馨怡、朱君鹏、卢琦、白雪婷");
        System.out.println("2019-7-9");
        System.out.println(StringFormatter.splitLine("-", MENULINEWIDTH));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void signIn() {
        System.out.println(StringFormatter.alignCenter("用户登录", '-', MENULINEWIDTH));
        System.out.println("请输入用户名：");
        String userName = scanner.next();
        System.out.println("请输入密码：");
        String password = scanner.next();
        bbsBiz = BbsBiz.signIn(userName, password);
        while (bbsBiz == null) {
            System.out.println("用户名或密码错误！请重新输入。");
            System.out.println("请输入用户名（输入 . 来退出）：");
            userName = scanner.next();
            if (userName.equals(".")) {
                break;
            }
            System.out.println("请输入密码：");
            password = scanner.next();
            bbsBiz = BbsBiz.signIn(userName, password);
        }
        System.out.println("登录成功！");
    }

    private static void signUp() {
        System.out.println("-----------Welcome----------");
        System.out.println("请输入用户名：");
        String userName = scanner.next();
        while (StringUtils.isNullOrEmpty(userName) || !BbsBiz.isValidUserName(userName)) {
            System.out.println("用户名非法！一个合法的用户名必须：");
            System.out.println("  1. 没有被占用；");
            System.out.println("  2. 只能包含字母、数字和下划线（_）；");
            System.out.println("  3. 只能以字母开头；");
            System.out.println("  4. 长度介于 3-20 之间。");
            System.out.println("请输入用户名（输入 . 来退出）：");
            userName = scanner.next();
            if (userName.equals(".")) {
                return;
            }
        }
        System.out.println("请输入密码：");
        String password = scanner.next();
        while (StringUtils.isNullOrEmpty(password) || !BbsBiz.isValidPassword(password)) {
            System.out.println("密码非法！一个合法的密码必须：");
            System.out.println("  1. 只能包含字母、数字和特殊字符（ `~!@#$%^&*()_+-={}|[]\\:\";'<>?,./）；");
            System.out.println("  2. 必须包含大写字母、小写字母、数字、特殊字符中的至少三种；");
            System.out.println("  3. 长度介于 6-20 之间。");
            System.out.println("请输入密码（输入 . 来退出）：");
            password = scanner.next();
            if (password.equals(".")) {
                return;
            }
        }
        System.out.println("请输入邮箱：");
        String email = scanner.next();
        if (BbsBiz.signUp(userName, password, email, "1")) {
            System.out.println("注册成功！请返回主界面登录：");
        }
        else {
            System.out.println("注册失败！请联系管理员获得支持。");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}