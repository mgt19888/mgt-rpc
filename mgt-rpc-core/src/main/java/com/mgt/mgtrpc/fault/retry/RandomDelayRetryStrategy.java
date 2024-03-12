package com.mgt.mgtrpc.fault.retry;

import com.github.rholder.retry.*;
import com.mgt.mgtrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 随机延迟 - 重试策略
 */
@Slf4j
public class RandomDelayRetryStrategy implements RetryStrategy {

    /**
     * 重试
     *
     * @param callable
     * @return
     * @throws ExecutionException
     * @throws RetryException
     */
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws ExecutionException, RetryException {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.randomWait(1, TimeUnit.SECONDS, 5, TimeUnit.SECONDS)) // 设置随机等待时间范围
                .withStopStrategy(StopStrategies.stopAfterAttempt(5))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {}，当前时间：{}", attempt.getAttemptNumber(), LocalTime.now());
                    }
                })
                .build();
        return retryer.call(callable);
    }

}
