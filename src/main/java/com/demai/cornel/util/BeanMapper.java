/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import com.demai.cornel.util.json.JsonUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 根据ObjectMapper将Bean进行处理为Map。 注意处理后Value将丢失类型，例如Long变为Integer。
 * Create By zhutf  19-10-6  下午4:27
 */
public class BeanMapper {

    private Map<String, Object> map;
    private Object bean;
    private ObjectMapper objectMapper;

    public static final String CLAZZ_KEY_NAME = "@clazz";

    public BeanMapper(final Object obj) {
        this(obj, JsonUtil.getMapper());
    }

    public BeanMapper(final Object obj, ObjectMapper objectMapper) {
        Preconditions.checkNotNull(obj);
        Preconditions.checkArgument(ReflectUtil.isPojoClazz(obj), "bean不能为基本类型,String以及集合类型!");
        this.bean = obj;
        this.objectMapper = objectMapper;
        this.map = JsonUtil.toMap(objectMapper, JsonUtil.toJson(objectMapper, obj));
    }

    public BeanMapper(final String json) {
        this(json, JsonUtil.getMapper());
    }

    public BeanMapper(final String json, ObjectMapper objectMapper) {
        Preconditions.checkNotNull(json);
        this.bean = JsonUtil.fromJson(objectMapper, json, Object.class);
        Preconditions.checkNotNull(this.bean);
        Preconditions.checkArgument(ReflectUtil.isPojoClazz(this.bean), "bean不能为基本类型,String以及集合类型!");
        this.objectMapper = objectMapper;
        this.map = JsonUtil.toMap(objectMapper, json);
    }

    public boolean containKey(String key) {
        return this.map.containsKey(key);
    }

    public Boolean getBoolean(String key) {
        return MapUtils.getBoolean(map, key);
    }

    public Integer getInteger(String key) {
        return MapUtils.getInteger(map, key);
    }

    public Long getLong(String key) {
        return MapUtils.getLong(map, key);
    }

    public Double getDouble(String key) {
        return MapUtils.getDouble(map, key);
    }

    public String getString(String key) {
        return MapUtils.getString(map, key);
    }

    public Object get(String key) {
        return MapUtils.getObject(map, key);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object obj = MapUtils.getObject(map, key);
        if (obj != null) {
            return JsonUtil.fromJson(objectMapper, JsonUtil.toJson(objectMapper, obj), clazz);
        }
        return null;
    }

    public <T> T get(String key, JavaType javaType) {
        Object obj = MapUtils.getObject(map, key);
        if (obj != null) {
            return JsonUtil.fromJson(objectMapper, JsonUtil.toJson(objectMapper, obj), javaType);
        }
        return null;
    }

    public Object getBean() {
        return this.bean;
    }

    /**
     * 获取反序列化的原始Map
     * 其中类型信息以及丢失
     *
     * @return
     */
    public Map<String, Object> getMap() {
        return this.map;
    }

    /**
     * 获取反序列化而且Value处理为Json的Map对象
     *
     * @return
     */
    public Map<String, Object> getMapWithBeanToJson() {
        return Maps.transformValues(this.map, new Function<Object, Object>() {
            @Nullable
            @Override
            public Object apply(@Nullable Object input) {
                if (!ReflectUtil.isPrimitiveOrWrapperOrString(input)) {
                    return JsonUtil.toJson(objectMapper, input);
                }
                return input;
            }
        });
    }

    /**
     * 获取反序列化Map对象
     * Value不是原生类型或者String类型的字段进行忽略
     * @return
     */
    public Map<String, Object> getMapWithoutBean() {
        return Maps.filterValues(this.map, new Predicate<Object>() {
            @Nullable
            @Override
            public boolean apply(@Nullable Object input) {
                return ReflectUtil.isPrimitiveOrWrapperOrString(input);
            }
        });
    }

    /**
     * 获取反序列化的Map对象。其中保存对象的clazz信息
     * Value不是原生类型或者String类型的字段进行忽略
     * @return
     */
    public Map<String, Object> getMapWithClazzNoneBean() {
        Map<String, Object> data = getMapWithoutBean();
        data.put(CLAZZ_KEY_NAME, this.bean.getClass().getName());
        return data;
    }

    @Override
    public String toString() {
        return StringUtil.toString(this.map);
    }
}
