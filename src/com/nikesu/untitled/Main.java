package com.nikesu.untitled;


import com.nikesu.untitled.dao.UserDao;
import com.nikesu.untitled.dao.impl.UserDaoImpl;
import com.nikesu.untitled.entity.User;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        ArrayList<User> list = new ArrayList<>();




        System.out.println("userDao.getUsers()");
        list = userDao.getUsers();
        for (User u : list) {
            System.out.println(u.getUserId() + "\t" + u.getUserName() + "\t" + u.getEmail() + "\t" + u.getPassword() +
                    "\t" + u.getRegDate() + "\t" + u.getUserGroupId());
        }

        System.out.println("");
        System.out.println("userDao.delUserById(\"11\") : " + (userDao.delUserById("11") ? "true" : "false"));

        System.out.println("");

        System.out.println("userDao.getUsers(2,3)");
        list = userDao.getUsers(2, 3);
        for (User u : list) {
            System.out.println(u.getUserId() + "\t" + u.getUserName() + "\t" + u.getEmail() + "\t" + u.getPassword() +
                    "\t" + u.getRegDate() + "\t" + u.getUserGroupId());
        }

        System.out.println("");

        System.out.println("userDao.getUsersByGroup(\"2\")");
        list = userDao.getUsersByGroup("2");
        for (User u : list) {
            System.out.println(u.getUserId() + "\t" + u.getUserName() + "\t" + u.getEmail() + "\t" + u.getPassword() +
                    "\t" + u.getRegDate() + "\t" + u.getUserGroupId());
        }

        System.out.println("");

        System.out.println("userDao.getUsersByGroup(\"2\", 1, 2)");
        list = userDao.getUsersByGroup("2", 1, 2);
        for (User u : list) {
            System.out.println(u.getUserId() + "\t" + u.getUserName() + "\t" + u.getEmail() + "\t" + u.getPassword() +
                    "\t" + u.getRegDate() + "\t" + u.getUserGroupId());
        }

        System.out.println("");

        System.out.println("userDao.addUser(user)\t(userName = \"testuser\", userGroupId = \"2\", password = \"123\")");
        User user = new User();
        user.setUserName("testuser");
        user.setUserGroupId("2");
        user.setPassword("123");
        userDao.addUser(user);

        System.out.println("user = userDao.getUserByName(\"testuser\")");
        user = userDao.getUserByName("testuser");
        System.out.println(user.getUserId() + "\t" + user.getUserName() + "\t" + user.getEmail() + "\t" + user.getPassword() +
                "\t" + user.getRegDate() + "\t" + user.getUserGroupId());

        System.out.println("userDao.updateEmail(\"\" + user.getUserId(),\"testemail@email.com\")");
        userDao.updateEmail("" + user.getUserId(),"testemail@email.com");
        user = userDao.getUserByName("testuser");
        System.out.println(user.getUserId() + "\t" + user.getUserName() + "\t" + user.getEmail() + "\t" + user.getPassword() +
                "\t" + user.getRegDate() + "\t" + user.getUserGroupId());

        System.out.println("");

        System.out.println((userDao.hasName("testuser") ? "true" : "false"));
    }
}