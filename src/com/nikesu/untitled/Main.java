package com.nikesu.untitled;

import com.nikesu.untitled.dao.BaseDao;
import com.nikesu.untitled.dao.impl.BaseDaoImpl;
import com.nikesu.untitled.entity.User;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        BaseDaoImpl baseDao = new BaseDaoImpl();
        ArrayList<User> userList = baseDao.executeQuery("SELECT * FROM bbs_user;", new User());
        for (User user : userList) {
            System.out.println(user.getUserId() + "\t" + user.getUserName());
        }
    }
}


