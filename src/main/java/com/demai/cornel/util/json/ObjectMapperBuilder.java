/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 默认不输出字段为null以及map里value为null的key值。通过writeFiledNonNull进行调整
 * 默认添加java8相关的module
 * 默认不失败empty beans以及unknown properties,以及将enum序列化为String
 * 日期默认序列化为timestamp.
 * 日期反序列化的格式为StdDateFormat,支持ISO-8601 compliant format (format String "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
 * Create By zhutf  19-10-6  下午3:32
 */
public class ObjectMapperBuilder {

    private DateFormat dateFormat;

    private boolean createXmlMapper = false;

    private boolean typeResolver = false;

    private boolean defaultModules = true;

    private boolean defaultFeatures = true;

    private boolean defaultWriteFieldNotNull = true;

    private Locale locale;

    private TimeZone timeZone;

    private Boolean defaultUseWrapper;

    private PropertyNamingStrategy propertyNamingStrategy = new AnnotationSensitivePropertyNamingStrategy();

    private TypeResolverBuilder<?> defaultTyping;

    private JsonInclude.Include serializationInclusion;

    private final List<Module> modules = Lists.newArrayList();

    private final Map<Object, Boolean> features = Maps.newHashMap();

    private final Map<Class<?>, JsonSerializer<?>> serializers = Maps.newLinkedHashMap();

    private final Map<Class<?>, JsonDeserializer<?>> deserializers = Maps.newLinkedHashMap();

    public ObjectMapperBuilder createXmlMapper(boolean createXmlMapper) {
        this.createXmlMapper = createXmlMapper;
        return this;
    }

    public ObjectMapperBuilder dateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public ObjectMapperBuilder locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public ObjectMapperBuilder timeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public ObjectMapperBuilder propertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        this.propertyNamingStrategy = propertyNamingStrategy;
        return this;
    }

    /**
     * 将propertiesName设置为snake模式 ShortCut for propertyNamingStrategy
     *
     * @return
     */
    public ObjectMapperBuilder withPropertySnake() {
        this.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE;
        return this;
    }

    /**
     * 将propertiesName设置为首字母大写模式 ShortCut for propertyNamingStrategy
     *
     * @return
     */
    public ObjectMapperBuilder withPropertyPascal() {
        this.propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE;
        return this;
    }

    public ObjectMapperBuilder defaultFeatures(boolean defaultFeatures) {
        this.defaultFeatures = defaultFeatures;
        return this;
    }

    public ObjectMapperBuilder defaultModules(boolean defaultModules) {
        this.defaultModules = defaultModules;
        return this;
    }

    public ObjectMapperBuilder defaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
        this.defaultTyping = typeResolverBuilder;
        return this;
    }

    /**
     * Configure custom serializers. Each serializer is registered for the type returned by
     * {@link JsonSerializer#handledType()}, which must not be {@code null}.
     *
     * @see #serializersByType(Map)
     */
    public ObjectMapperBuilder serializers(JsonSerializer<?>... serializers) {
        if (serializers != null) {
            for (JsonSerializer<?> serializer : serializers) {
                Class<?> handledType = serializer.handledType();
                if (handledType == null || handledType == Object.class) {
                    throw new IllegalArgumentException("Unknown handled type in " + serializer.getClass().getName());
                }
                this.serializers.put(serializer.handledType(), serializer);
            }
        }
        return this;
    }

    /**
     * Configure a custom serializer for the given type.
     *
     * @see #serializers(JsonSerializer...)
     * @since 4.1.2
     */
    public ObjectMapperBuilder serializerByType(Class<?> type, JsonSerializer<?> serializer) {
        if (serializer != null) {
            this.serializers.put(type, serializer);
        }
        return this;
    }

    /**
     * Configure custom serializers for the given types.
     *
     * @see #serializers(JsonSerializer...)
     */
    public ObjectMapperBuilder serializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
        if (serializers != null) {
            this.serializers.putAll(serializers);
        }
        return this;
    }

    /**
     * Configure custom deserializers. Each deserializer is registered for the type returned by
     * {@link JsonDeserializer#handledType()}, which must not be {@code null}.
     *
     * @since 4.3
     * @see #deserializersByType(Map)
     */
    public ObjectMapperBuilder deserializers(JsonDeserializer<?>... deserializers) {
        if (deserializers != null) {
            for (JsonDeserializer<?> deserializer : deserializers) {
                Class<?> handledType = deserializer.handledType();
                if (handledType == null || handledType == Object.class) {
                    throw new IllegalArgumentException("Unknown handled type in " + deserializer.getClass().getName());
                }
                this.deserializers.put(deserializer.handledType(), deserializer);
            }
        }
        return this;
    }

    /**
     * Configure a custom deserializer for the given type.
     *
     * @since 4.1.2
     */
    public ObjectMapperBuilder deserializerByType(Class<?> type, JsonDeserializer<?> deserializer) {
        if (deserializer != null) {
            this.deserializers.put(type, deserializer);
        }
        return this;
    }

    /**
     * Configure custom deserializers for the given types.
     */
    public ObjectMapperBuilder deserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
        if (deserializers != null) {
            this.deserializers.putAll(deserializers);
        }
        return this;
    }

    public ObjectMapperBuilder serializationInclusion(JsonInclude.Include serializationInclusion) {
        this.serializationInclusion = serializationInclusion;
        return this;
    }

    /**
     *
     * Define if a wrapper will be used for indexed (List, array) properties or not by default to XmlMapper
     *
     * @param defaultUseWrapper
     * @return
     */
    public ObjectMapperBuilder defaultUseWrapper(boolean defaultUseWrapper) {
        this.defaultUseWrapper = defaultUseWrapper;
        return this;
    }

    public ObjectMapperBuilder autoDetectFields(boolean autoDetectFields) {
        this.features.put(MapperFeature.AUTO_DETECT_FIELDS, autoDetectFields);
        return this;
    }

    public ObjectMapperBuilder autoDetectGettersSetters(boolean autoDetectGettersSetters) {
        this.features.put(MapperFeature.AUTO_DETECT_GETTERS, autoDetectGettersSetters);
        this.features.put(MapperFeature.AUTO_DETECT_SETTERS, autoDetectGettersSetters);
        this.features.put(MapperFeature.AUTO_DETECT_IS_GETTERS, autoDetectGettersSetters);
        return this;
    }

    public ObjectMapperBuilder defaultViewInclusion(boolean defaultViewInclusion) {
        this.features.put(MapperFeature.DEFAULT_VIEW_INCLUSION, defaultViewInclusion);
        return this;
    }

    public ObjectMapperBuilder failOnUnknownProperties(boolean failOnUnknownProperties) {
        this.features.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
        return this;
    }

    public ObjectMapperBuilder writeEnumsUsingToString(boolean writeEnumsUsingToString) {
        this.features.put(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, writeEnumsUsingToString);
        return this;
    }

    public ObjectMapperBuilder failOnEmptyBeans(boolean failOnEmptyBeans) {
        this.features.put(SerializationFeature.FAIL_ON_EMPTY_BEANS, failOnEmptyBeans);
        return this;
    }

    public ObjectMapperBuilder indentOutput(boolean indentOutput) {
        this.features.put(SerializationFeature.INDENT_OUTPUT, indentOutput);
        return this;
    }

    /**
     * 只写非NULL的字段，对于map里相同处理
     * 默认为true
     * @return
     */
    public ObjectMapperBuilder writeFiledNonNull(boolean writeFieldNull) {
        this.defaultWriteFieldNotNull = writeFieldNull;
        return this;
    }

    /**
     * 将空值写为空串
     *
     * @return
     */
    public ObjectMapperBuilder writeValueNullToEmpty() {
        writeFiledNonNull(false);
        serializerByType(Object.class, new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                    JsonProcessingException {
                jgen.writeString("");
            }
        });
        return this;
    }

    /**
     * 将类型信息写入到clazz字段
     *
     * @return
     */
    public ObjectMapperBuilder writeTypeAsClazz() {
        writeFiledNonNull(false);
        this.typeResolver = true;
        return this;
    }

    /**
     * 将joda.date类型处理为/Date(ms)/类型
     *
     * @return
     */
    public ObjectMapperBuilder withJsonDate() {
        this.modules.add(new JsonDateModule());
        return this;
    }

    /**
     * 将joda.datetime类型处理为/Date(ms)/类型
     *
     * @return
     */
    public ObjectMapperBuilder withJsonDateTime() {
        this.modules.add(new JsonDateTimeModule());
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> void addSerializers(SimpleModule module) {
        for (Class<?> type : this.serializers.keySet()) {
            module.addSerializer((Class<? extends T>) type, (JsonSerializer<T>) this.serializers.get(type));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void addDeserializers(SimpleModule module) {
        for (Class<?> type : this.deserializers.keySet()) {
            module.addDeserializer((Class<T>) type, (JsonDeserializer<? extends T>) this.deserializers.get(type));
        }
    }

    private void configureFeature(ObjectMapper objectMapper, Object feature, boolean enabled) {
        if (feature instanceof JsonParser.Feature) {
            objectMapper.configure((JsonParser.Feature) feature, enabled);
        } else if (feature instanceof JsonGenerator.Feature) {
            objectMapper.configure((JsonGenerator.Feature) feature, enabled);
        } else if (feature instanceof SerializationFeature) {
            objectMapper.configure((SerializationFeature) feature, enabled);
        } else if (feature instanceof DeserializationFeature) {
            objectMapper.configure((DeserializationFeature) feature, enabled);
        } else if (feature instanceof MapperFeature) {
            objectMapper.configure((MapperFeature) feature, enabled);
        } else {
            throw new IllegalArgumentException("Unknown feature class: " + feature.getClass().getName());
        }
    }

    private void customizeDefaultFeatures(ObjectMapper objectMapper) {
        if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
            configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        }
        if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
            configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
    }

    public void configure(ObjectMapper objectMapper) {
        if (this.defaultFeatures) {
            this.failOnEmptyBeans(false).failOnUnknownProperties(false).writeEnumsUsingToString(true);
        }

        if (this.defaultWriteFieldNotNull) {
            this.serializationInclusion = JsonInclude.Include.NON_NULL;
            this.features.put(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        }

        if (this.dateFormat != null) {
            objectMapper.setDateFormat(this.dateFormat);
        }
        if (this.locale != null) {
            objectMapper.setLocale(this.locale);
        }
        if (this.timeZone != null) {
            objectMapper.setTimeZone(this.timeZone);
        }

        if (this.propertyNamingStrategy != null) {
            objectMapper.setPropertyNamingStrategy(this.propertyNamingStrategy);
        }
        if (this.defaultTyping != null) {
            objectMapper.setDefaultTyping(this.defaultTyping);
        } else if (this.typeResolver) {
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
        if (this.serializationInclusion != null) {
            objectMapper.setSerializationInclusion(this.serializationInclusion);
        }

        // 添加默认的模块
        if (this.defaultModules) {
            objectMapper.registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule()).registerModule(new CustomEnumModule());
        }

        objectMapper.registerModules(this.modules);

        if (!this.serializers.isEmpty() || !this.deserializers.isEmpty()) {
            SimpleModule module = new SimpleModule();
            addSerializers(module);
            addDeserializers(module);
            objectMapper.registerModule(module);
        }

        customizeDefaultFeatures(objectMapper);
        for (Object feature : this.features.keySet()) {
            configureFeature(objectMapper, feature, this.features.get(feature));
        }
    }

    public <T extends ObjectMapper> T build() {
        ObjectMapper mapper;
        if (this.createXmlMapper) {
            mapper = (this.defaultUseWrapper != null ? new XmlObjectMapperInitializer().create(this.defaultUseWrapper)
                    : new XmlObjectMapperInitializer().create());
        } else {
            mapper = new ObjectMapper();
        }
        configure(mapper);
        return (T) mapper;
    }

    public static ObjectMapperBuilder json() {
        return new ObjectMapperBuilder();
    }

    public static ObjectMapperBuilder xml() {
        return new ObjectMapperBuilder().createXmlMapper(true);
    }

    private static class XmlObjectMapperInitializer {

        public ObjectMapper create() {
            return new XmlMapper(xmlInputFactory());
        }

        public ObjectMapper create(boolean defaultUseWrapper) {
            JacksonXmlModule module = new JacksonXmlModule();
            module.setDefaultUseWrapper(defaultUseWrapper);
            return new XmlMapper(new XmlFactory(xmlInputFactory()), module);
        }

        private static XMLInputFactory xmlInputFactory() {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
            return inputFactory;
        }

        private static final byte[] EMPTY_CONTENT = new byte[0];

        private static final XMLResolver NO_OP_XML_RESOLVER = new XMLResolver() {
            @Override
            public Object resolveEntity(String publicID, String systemID, String base, String ns) {
                return new ByteArrayInputStream(EMPTY_CONTENT);
            }
        };
    }

}

