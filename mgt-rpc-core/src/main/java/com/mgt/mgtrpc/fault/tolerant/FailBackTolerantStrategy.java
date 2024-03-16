package com.mgt.mgtrpc.fault.tolerant;

import com.mgt.mgtrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 降级到其他服务 - 容错策略
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // 获取降级的服务信息
        String serviceName = (String) context.get("serviceName");
        String methodName = (String) context.get("methodName");
        // 记录日志
        log.error("Fallback to alternative service due to error: {}", e.getMessage());

        // 调用备用服务或执行其他容错逻辑
        // TODO:在此处可以编写代码来调用备用服务或执行其他容错逻辑

        // 返回降级后的响应或执行结果
        return null;
    }
}
