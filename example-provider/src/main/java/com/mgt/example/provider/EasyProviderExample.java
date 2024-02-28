package com.mgt.example.provider;

import com.mgt.mgtrpc.server.HttpServer;
import com.mgt.mgtrpc.server.VertxHttpServer;

/**
 * 简易服务提供者
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
