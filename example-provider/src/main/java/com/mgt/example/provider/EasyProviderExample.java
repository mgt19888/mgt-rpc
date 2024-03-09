package com.mgt.example.provider;

import com.mgt.example.common.service.UserService;
import com.mgt.mgtrpc.RpcApplication;
import com.mgt.mgtrpc.config.RegistryConfig;
import com.mgt.mgtrpc.config.RpcConfig;
import com.mgt.mgtrpc.model.ServiceMetaInfo;
import com.mgt.mgtrpc.registry.LocalRegistry;
import com.mgt.mgtrpc.registry.Registry;
import com.mgt.mgtrpc.registry.RegistryFactory;
import com.mgt.mgtrpc.server.HttpServer;
import com.mgt.mgtrpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
