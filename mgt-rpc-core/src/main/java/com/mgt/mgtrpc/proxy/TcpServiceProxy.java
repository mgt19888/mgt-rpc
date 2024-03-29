package com.mgt.mgtrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.mgt.mgtrpc.RpcApplication;
import com.mgt.mgtrpc.config.RpcConfig;
import com.mgt.mgtrpc.constant.RpcConstant;
import com.mgt.mgtrpc.fault.retry.RetryStrategy;
import com.mgt.mgtrpc.fault.retry.RetryStrategyFactory;
import com.mgt.mgtrpc.fault.tolerant.TolerantStrategy;
import com.mgt.mgtrpc.fault.tolerant.TolerantStrategyFactory;
import com.mgt.mgtrpc.loadbalancer.LoadBalancer;
import com.mgt.mgtrpc.loadbalancer.LoadBalancerFactory;
import com.mgt.mgtrpc.model.RpcRequest;
import com.mgt.mgtrpc.model.RpcResponse;
import com.mgt.mgtrpc.model.ServiceMetaInfo;
import com.mgt.mgtrpc.registry.Registry;
import com.mgt.mgtrpc.registry.RegistryFactory;
import com.mgt.mgtrpc.serializer.Serializer;
import com.mgt.mgtrpc.serializer.SerializerFactory;
import com.mgt.mgtrpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理（JDK 动态代理）
 */
public class TcpServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws UnknownHostException {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .ip(String.valueOf(InetAddress.getLocalHost()))
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        System.out.println("【rpcrequest】:" + rpcRequest.toString());
        try {
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("ip", rpcRequest.getIp());
            requestParams.put("methodName", rpcRequest.getMethodName());
            requestParams.put("serviceName", serviceName);
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

            // rpc 请求
            // 使用重试机制
            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                        VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
                );
            } catch (Exception e) {
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
//                requestParams.put("serviceName", serviceName);
                rpcResponse = tolerantStrategy.doTolerant(requestParams, e);
            }
            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException("调用失败");
        }
    }
}