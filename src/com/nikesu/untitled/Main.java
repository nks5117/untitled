package com.nikesu.untitled;

import com.mysql.cj.util.StringUtils;
import com.nikesu.untitled.biz.BbsBiz;
import com.nikesu.untitled.dao.impl.UserDaoImpl;

import java.util.Scanner;

public class Main {
    private static BbsBiz bbsBiz;
    private static Scanner scanner = new Scanner(System.in);

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
                    System.out.println("Bye.");
                    System.exit(0);
                    break;
                case 4:
                    showAbout();
                default:
            }

            if (bbsBiz != null) {
                logout:
                while (true) {
                    showSignedMenu();
                    int b = scanner.nextInt();
                    switch (b) {
                        case 1:
                            userPage();
                            break;
                        case 2:
                            break logout;
                        case 3:
                            break;
                        default:
                    }
                }
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("---欢迎来到 untitled BBS----");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
        System.out.println("4. 关于");
        System.out.println("----------------------------");
    }

    private static void showSignedMenu() {
        System.out.println("-----------Welcome----------");
        System.out.println("1. 我的主页");
        System.out.println("2. 登出");
        System.out.println("3. 板块 1");
        System.out.println("4. 板块 2");
        System.out.println("----------------------------");
    }

    private static void showAbout() {
        System.out.println("----------------------------");
        System.out.println("untitled BBS 管理系统");
        System.out.println("版本: 1.0.0");
        System.out.println("作者: 倪可塑、王馨怡、朱君鹏、卢琦、白雪婷");
        System.out.println("2019-7-9");
        System.out.println("----------------------------");
        System.out.println("输入任意字符来回到主菜单");
        scanner.next();
    }

    private static void signIn() {
        System.out.println("-----------Welcome----------");
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
            System.out.println("请输入用户名：");
            userName = scanner.next();
        }
        System.out.println("请输入密码：");
        String password = scanner.next();
        while (StringUtils.isNullOrEmpty(password) || !BbsBiz.isValidPassword(password)) {
            System.out.println("密码非法！一个合法的密码必须：");
            System.out.println("  1. 只能包含字母、数字和特殊字符（ `~!@#$%^&*()_+-={}|[]\\:\";'<>?,./）；");
            System.out.println("  2. 必须包含大写字母、小写字母、数字、特殊字符中的至少三种；");
            System.out.println("  3. 长度介于 6-20 之间。");
            System.out.println("请输入密码：");
            password = scanner.next();
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

    private static void userPage() {
        ;
    }
}