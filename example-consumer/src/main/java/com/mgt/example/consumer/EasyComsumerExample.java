package com.mgt.example.consumer;

import com.mgt.example.common.model.User;
import com.mgt.example.common.service.UserService;
import com.mgt.mgtrpc.config.RpcConfig;
import com.mgt.mgtrpc.proxy.ServiceProxyFactory;
import com.mgt.mgtrpc.utils.ConfigUtils;

public class EasyComsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("mgt");
        // 调用
        User newUser = userService.getUser(user);

        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }
}
