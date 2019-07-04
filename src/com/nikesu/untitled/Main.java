package com.nikesu.untitled;

import com.nikesu.untitled.dao.BaseDao;
import com.nikesu.untitled.dao.impl.BaseDaoImpl;
import com.nikesu.untitled.entity.User;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BaseDaoImpl baseDao = new BaseDaoImpl();

        User user = new User();
        user.setEmail("test@test.com");
        user.setUserName("test23");
        user.setUserGroupId("2");
        user.setPassword("abc");
        baseDao.insertToTable("bbs_user", user);

        ArrayList<User> userList = baseDao.executeQuery("SELECT * FROM bbs_user;", User.class);
        for (User user1 : userList) {
            System.out.println(user1.getUserId() + "\t" + user1.getUserName());
        }
    }
}