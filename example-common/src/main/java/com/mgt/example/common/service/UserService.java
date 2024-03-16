package com.mgt.example.common.service;

import com.mgt.example.common.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 新方法 - 获取数字
     */
    default short getNumber() {
        return 1;
    }

    /**
     * 新方法 - 获取字符串
     */
    default String getString() {
        return "wbf";
    }
}