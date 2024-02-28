package com.mgt.example.consumer;

import com.mgt.example.common.model.User;
import com.mgt.example.common.service.UserService;

public class EasyComsumerExample {

    public static void main(String[] args) {
        UserService userService = null;
        User user = new User();
        user.setName("mgt");
        //调用
        userService.getUser(user);
    }
}
