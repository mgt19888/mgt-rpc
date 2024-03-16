package com.mgt.example.provider;

import com.mgt.example.common.service.UserService;
import com.mgt.mgtrpc.RpcApplication;
import com.mgt.mgtrpc.bootstrap.ProviderBootstrap;
import com.mgt.mgtrpc.config.RegistryConfig;
import com.mgt.mgtrpc.config.RpcConfig;
import com.mgt.mgtrpc.model.ServiceMetaInfo;
import com.mgt.mgtrpc.model.ServiceRegisterInfo;
import com.mgt.mgtrpc.registry.LocalRegistry;
import com.mgt.mgtrpc.registry.Registry;
import com.mgt.mgtrpc.registry.RegistryFactory;
import com.mgt.mgtrpc.server.HttpServer;
import com.mgt.mgtrpc.server.http.VertxHttpServer;
import com.mgt.mgtrpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
