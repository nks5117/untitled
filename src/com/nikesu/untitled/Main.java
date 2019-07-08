package com.nikesu.untitled;

import com.nikesu.untitled.biz.BbsBiz;

import java.util.Scanner;

public class Main {
    private static BbsBiz userBiz;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
                default:
            }

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

    private static void showMainMenu() {
        System.out.println("-----------Welcome----------");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
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

    private static void signIn() {
        ;
    }

    private static void signUp() {
        ;
    }

    private static void userPage() {
        ;
    }
}