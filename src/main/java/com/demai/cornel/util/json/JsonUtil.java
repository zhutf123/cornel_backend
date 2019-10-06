/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * ref:https://github.com/dropwizard/dropwizard/blob/master/dropwizard-jackson%2Fsrc%2Fmain%2Fjava%2Fio%2Fdropwizard%2F
 * jackson%2FJackson.java
 *
 * Create By zhutf 19-10-6 下午3:30
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper DEFAULT_MAPPER = ObjectMapperBuilder.json().build();

    public static boolean versionsMatch(Version v1, Version v2) {
        return v1.getMajorVersion() == v2.getMajorVersion() && v1.getMinorVersion() == v2.getMinorVersion()
                && v1.getPatchLevel() == v2.getPatchLevel();
    }

    public static String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    public static String toJsonP(ObjectMapper mapper, String functionName, Object object) {
        return toJson(mapper, new JSONPObject(functionName, object));
    }

    public static String toJson(Object o) {
        return toJson(DEFAULT_MAPPER, o);
    }

    public static String toJson(ObjectMapper mapper, Object o) {
        if (o == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static String toJson(Object o, String... excludes) {
        return toJson(DEFAULT_MAPPER, o, excludes);
    }

    private static void removeNode(ObjectNode objectNode, String exclude) {
        List<String> nodeNames = Splitter.on('.').omitEmptyStrings().splitToList(exclude);
        ObjectNode parentNode = objectNode;
        if (CollectionUtils.isNotEmpty(nodeNames)) {
            for (int i = 0; i < nodeNames.size() - 1; i++) {
                JsonNode jsonNode = parentNode.get(nodeNames.get(i));
                if (jsonNode != null && jsonNode instanceof ObjectNode) {
                    parentNode = (ObjectNode) jsonNode;
                } else {
                    parentNode = null;
                    break;
                }
            }
            if (parentNode != null) {
                parentNode.remove(nodeNames.get(nodeNames.size() - 1));
            }
        }
    }

    /**
     * 支持序列化Json的时删除不必要的字段，其中通过.定义层级
     *
     * @param mapper
     * @param o
     * @param excludes
     * @return
     */
    public static String toJson(ObjectMapper mapper, Object o, String... excludes) {
        final ObjectNode jsonNode = mapper.valueToTree(o);
        for (String exclude : excludes) {
            if (StringUtils.isNotBlank(exclude)) {
                removeNode(jsonNode, exclude);
            }
        }
        return jsonNode.toString();
    }

    public static void writeJsonToOutputStream(Object o, OutputStream os) throws IOException {
        writeJsonToOutputStream(DEFAULT_MAPPER, o, os);
    }

    public static void writeJsonToOutputStream(ObjectMapper mapper, Object o, OutputStream os) throws IOException {
        mapper.writeValue(os, o);
    }

    /**
     * 将对象直接转为Tree model对象
     *
     * @param o
     * @return
     */
    public static JsonNode toNode(Object o) {
        return toNode(DEFAULT_MAPPER, o);
    }

    public static JsonNode toNode(ObjectMapper mapper, Object o) {
        return mapper.valueToTree(o);
    }

    public static <T> T fromNode(JsonNode node, Class<T> clazz) {
        return fromNode(DEFAULT_MAPPER, node, clazz);
    }

    public static <T> T fromNode(ObjectMapper mapper, JsonNode node, Class<T> clazz) {
        try {
            return mapper.treeToValue(node, clazz);
        } catch (Exception e) {
            LOGGER.error("fromNode error! node:{}, clazz:{}", node, clazz, e);
            return null;
        }
    }

    /**
     * 不转为POJO对象，转为Tree model对象
     *
     * @param jsonString
     * @return
     */
    public static JsonNode toNode(String jsonString) {
        return fromJson(jsonString, JsonNode.class);
    }

    public static JsonNode toNode(ObjectMapper mapper, String jsonString) {
        return fromJson(mapper, jsonString, JsonNode.class);
    }

    /**
     * 不转为POJO对象，转为Map对象
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> toMap(String jsonString) {
        return fromJson(jsonString, Map.class);
    }

    public static Map<String, Object> toMap(ObjectMapper mapper, String jsonString) {
        return fromJson(mapper, jsonString, Map.class);
    }

    /**
     * 不转为POJO对象，转为List对象
     *
     * @param jsonString
     * @return
     */
    public static List<Object> toList(String jsonString) {
        return fromJson(jsonString, List.class);
    }

    public static List<Object> toList(ObjectMapper mapper, String jsonString) {
        return fromJson(mapper, jsonString, List.class);
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>这种简单类型时使用如下语句,使用後面的函數.
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return fromJson(DEFAULT_MAPPER, jsonString, clazz);
    }

    public static <T> T fromJson(ObjectMapper mapper, String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            LOGGER.error("parse json error! json:{}, clazz:{}", jsonString, clazz, e);
            return null;
        }
    }

    public static <T> T fromJson(Reader reader, Class<T> clazz) {
        return fromJson(DEFAULT_MAPPER, reader, clazz);
    }

    public static <T> T fromJson(ObjectMapper mapper, Reader reader, Class<T> clazz) {
        if (null == reader) {
            return null;
        }

        try {
            return mapper.readValue(reader, clazz);
        } catch (Exception e) {
            LOGGER.error("parse json error! reader:{}, clazz:{}", reader, clazz, e);
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>時, 先用constructParametricType(List.class,MyBean.class)構造出JavaTeype,再調用本函數.
     */
    public static <T> T fromJson(String jsonString, JavaType javaType) {
        return fromJson(DEFAULT_MAPPER, jsonString, javaType);
    }

    public static <T> T fromJson(ObjectMapper mapper, String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            LOGGER.error("parse json error! json:{}, javaType:{}", jsonString, javaType, e);
            return null;
        }
    }

    /**
     *
     * @param reader
     * @param javaType
     * @param <T>
     * @return
     */
    public static <T> T fromJson(Reader reader, JavaType javaType) {
        return fromJson(DEFAULT_MAPPER, reader, javaType);
    }

    public static <T> T fromJson(ObjectMapper mapper, Reader reader, JavaType javaType) {
        if (null == reader) {
            return null;
        }
        try {
            return (T) mapper.readValue(reader, javaType);
        } catch (IOException e) {
            LOGGER.error("parse json error! reader:{}, javaType:{}", reader, javaType, e);
            return null;
        }
    }

    /**
     * 構造泛型的Type如List<MyBean>, Map<String,MyBean>
     */
    public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return TypeFactory.defaultInstance().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 構造泛型的Type如Mybean<List<OtherBean>>
     */
    public static JavaType constructParametricType(Class<?> parametrized, JavaType... parameterTypes) {
        return TypeFactory.defaultInstance().constructParametricType(parametrized, parameterTypes);
    }

    public static JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    /**
     * 获取默认的Mapper拷贝
     *
     * @return
     */
    public static final ObjectMapper getMapper() {
        return DEFAULT_MAPPER.copy();
    }

    /**
     * 新建Mapper，配置与默认的一致 使用ObjectMapperBuilder.json().build()替换
     * 
     * @return
     */
    @Deprecated
    public static ObjectMapper newObjectMapper() {
        return ObjectMapperBuilder.json().build();
    }

    /**
     * 序列化保留clazz值，便于进行反序列化 使用ObjectMapperBuilder.writeTypeAsClazz替换
     * 
     * @return
     */
    @Deprecated
    public static ObjectMapper newObjectMapperWithClazz() {
        return ObjectMapperBuilder.json().writeTypeAsClazz().build();
    }

    /**
     * 序列化保留null的key，value为null 使用ObjectMapperBuilder替换
     * 
     * @return
     */
    @Deprecated
    public static ObjectMapper newObjectMapperWithNullValue() {
        return ObjectMapperBuilder.json().writeFiledNonNull(false).build();
    }

    /**
     * 序列化保留null的key，value为空串 使用ObjectMapperBuilder.WriteValueNullToEmpty替换
     * 
     * @return
     */
    @Deprecated
    public static ObjectMapper newObjectMapperWithEmptyValue() {
        return ObjectMapperBuilder.json().writeValueNullToEmpty().build();
    }

    /**
     * 将propertiesName设置为snake模式 使用ObjectMapperBuilder替换
     * 
     * @param objectMapper
     * @return
     */
    @Deprecated
    public static ObjectMapper withPropertySnake(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper;
    }

    /**
     * 将propertiesName设置为首字母大写模式 使用ObjectMapperBuilder替换
     * 
     * @param objectMapper
     * @return
     */
    @Deprecated
    public static ObjectMapper withPropertyPascal(ObjectMapper objectMapper) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        return objectMapper;
    }

    /**
     * 将datetime类型处理为/Date(ms)/类型 使用ObjectMapperBuilder替换
     * 
     * @param objectMapper
     * @return
     */
    @Deprecated
    public static ObjectMapper withJsonDate(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JsonDateModule());
        return objectMapper;
    }

    /**
     * 使用ObjectMapperBuilder替换
     * 
     * @param objectMapper
     * @return
     */
    @Deprecated
    public static ObjectMapper withJsonDateTime(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JsonDateTimeModule());
        return objectMapper;
    }
}
