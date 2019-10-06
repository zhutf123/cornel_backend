/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Create By zhutf  19-10-6  下午4:35
 */
public class ReflectUtil {
    /**
     * 判断类型是否为基本类型或者字符串。为null返回true
     *
     * @param obj
     * @return
     */
    public static boolean isPrimitiveOrWrapperOrString(Object obj) {
        if (obj == null) {
            return true;
        }
        return isPrimitiveOrWrapperOrString(obj.getClass());
    }

    /**
     * 判断类型是否为基本类型或者字符串
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        return ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.equals(String.class);
    }

    /**
     * 判断是否为集合类
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否为简单类型
     *
     * @param clazz
     * @return
     */
    public static boolean isPojoClazz(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        return !isPrimitiveOrWrapperOrString(clazz) && !isCollection(clazz);
    }

    /**
     * 判断是否为简单对象。其中null返回true
     *
     * @param obj
     * @return
     */
    public static boolean isPojoClazz(Object obj) {
        if (obj == null) {
            return true;
        }
        return isPojoClazz(obj.getClass());
    }

    public static Optional<Field> getField(Class clazz, String fieldName) {
        try {
            return Optional.fromNullable(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            return Optional.absent();
        }
    }

    public static Object getFieldValueRecursion(Object target, String fieldName) {
        try {

            if (target == null) {
                return null;
            }

            Class superClass = target.getClass();
            while(superClass != null) {
                Optional<Field> field = getField(superClass, fieldName);
                if (field.isPresent()) {
                    field.get().setAccessible(true);
                    return field.get().get(target);
                } else {
                    superClass = superClass.getSuperclass();
                }
            }
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}

