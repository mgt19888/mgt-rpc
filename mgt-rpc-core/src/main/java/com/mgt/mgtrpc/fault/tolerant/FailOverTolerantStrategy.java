package com.mgt.mgtrpc.fault.tolerant;

import com.mgt.mgtrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 转移到其他服务节点 - 容错策略
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 获取其他服务节点信息
        String serviceName = (String) context.get("serviceName");

        // 记录日志
        log.error("Failover to alternative service node due to error: {}", e.getMessage());

        // 调用其他服务节点或执行其他容错逻辑
        // TODO:在此处可以编写代码来调用备用服务或执行其他容错逻辑


        // 返回容错后的响应或执行结果
        return null;
    }
}
