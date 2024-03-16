package com.mgt.mgtrpc.proxy;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Mock 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    private static final Faker faker = new Faker();

    private static final Map<Class<?>, Supplier<Object>> defaultValues = new HashMap<>();

    static {
        // 添加基本类型的默认值生成逻辑
        defaultValues.put(boolean.class, () -> false);
        defaultValues.put(char.class, () -> '\u0000');
        defaultValues.put(byte.class, () -> (byte) 0);
        defaultValues.put(short.class, () -> (short) 0);
        defaultValues.put(int.class, () -> 0);
        defaultValues.put(long.class, () -> 0L);
        defaultValues.put(float.class, () -> 0.0f);
        defaultValues.put(double.class, () -> 0.0d);

        // 添加包装类型的默认值生成逻辑
        defaultValues.put(Boolean.class, () -> false);
        defaultValues.put(Character.class, () -> '\u0000');
        defaultValues.put(Byte.class, () -> (byte) 0);
        defaultValues.put(Short.class, () -> (short) 0);
        defaultValues.put(Integer.class, () -> 0);
        defaultValues.put(Long.class, () -> 0L);
        defaultValues.put(Float.class, () -> 0.0f);
        defaultValues.put(Double.class, () -> 0.0d);

        // 添加字符串类型的默认值生成逻辑
        defaultValues.put(String.class, () -> faker.lorem().word());

        // 添加日期时间类型的默认值生成逻辑
        defaultValues.put(java.util.Date.class, () -> faker.date().birthday());
        defaultValues.put(java.time.LocalDate.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        defaultValues.put(java.time.LocalDateTime.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        defaultValues.put(java.time.LocalTime.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        defaultValues.put(java.time.ZonedDateTime.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()));
        defaultValues.put(java.time.OffsetDateTime.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime());
        defaultValues.put(java.time.Duration.class, () -> faker.number().numberBetween(0, Integer.MAX_VALUE));
        defaultValues.put(java.time.Period.class, () -> faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        defaultValues.put(java.time.Instant.class, () -> faker.date().birthday());

        // 添加其他常见类型的默认值生成逻辑
        defaultValues.put(UUID.class, UUID::randomUUID);
        defaultValues.put(URL.class, () -> {
            try {
                return new URL("https://example.com");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        });
        defaultValues.put(URI.class, () -> {
            try {
                return new URI("https://example.com");
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        });
        defaultValues.put(Path.class, () -> Paths.get("/path/to/file"));


        // 可继续补充其他类型的默认值生成逻辑

    }

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     *
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        Supplier<Object> defaultValueSupplier = defaultValues.get(type);
        if (defaultValueSupplier != null) {
            return defaultValueSupplier.get();
        } else {
            return null; // 如果没有匹配的类型，默认返回null
        }
    }
}
