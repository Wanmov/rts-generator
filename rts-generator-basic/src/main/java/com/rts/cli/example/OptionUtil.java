package com.rts.cli.example;

import java.lang.reflect.Field;
import java.util.*;

import picocli.CommandLine.Option;

/**
 * @author Mona
 * @description 交互式参数处理工具类
 * @created in 2024/03/01
 */
public class OptionUtil {

    /**
     * 使用反射处理交互式参数 - 强交互,全都开启
     *
     * @param clazz clazz
     * @param args  输入参数
     * @return {@link String[]}
     */
    public static String[] processInteractiveOptions(Class<?> clazz, String[] args) {
        // 将传递过来的数组转成集合，方便添加
        Set<String> argSet = new LinkedHashSet<>(Arrays.asList(args));

        // 通过反射获取类中的所有字段
        for (Field field : clazz.getDeclaredFields()) {
            // 如果注解存在且其interactive属性为true，则执行以下操作
            Option option = field.getAnnotation(Option.class);
            // 如果用户没输入交互式参数，则添加到集合中
            if (option != null && option.interactive() && !argSet.contains(option.names()[0])) {
                argSet.add(option.names()[0]);
            }
        }
        return argSet.toArray(new String[0]);
    }
}