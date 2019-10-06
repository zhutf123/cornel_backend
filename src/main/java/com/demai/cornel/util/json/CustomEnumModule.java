/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.demai.cornel.dmEnum.IEmus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 支持IEnum类型的值进行序列化以及反序列号。
 * 序列化的值为getValue()
 * 反序列化支持toString方法以及getValue()的值
 * Create By zhutf  19-10-6  下午3:54
 */
public class CustomEnumModule extends Module {

    private static class CustomEnumDeserializer extends StdScalarDeserializer<IEmus> {

        private final IEmus[] constants;
        private final List<Integer> acceptedValues;

        protected CustomEnumDeserializer(Class<IEmus> clazz) {
            super(clazz);
            this.constants = clazz.getEnumConstants();
            this.acceptedValues = Lists.newArrayList();
            for (IEmus constant : this.constants) {
                acceptedValues.add(constant.getValue());
            }
        }

        @Override
        public IEmus deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            String text = jp.getText();
            if (StringUtils.isNumeric(text)) {
                int value = Integer.parseInt(text);

                for (IEmus constant : this.constants) {
                    if (constant.getValue() == value) {
                        return constant;
                    }
                }

                throw ctxt.mappingException(jp.getText() + " was not one of " + acceptedValues + ", type = "
                        + handledType());
            } else {
                for (IEmus constant : this.constants) {
                    if (StringUtils.equalsIgnoreCase(constant.toString(), text)) {
                        return constant;
                    }
                }
                throw ctxt.mappingException(jp.getText() + " was not one of " + Joiner.on(",").join(constants) + ", type = "
                        + handledType());
            }
        }
    }

    private static class CustomEnumDeserializers extends Deserializers.Base {
        @Override
        @SuppressWarnings("unchecked")
        public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
                                                        BeanDescription desc) throws JsonMappingException {
            // If the user configured to use `toString` method to deserialize enums
            if (config.hasDeserializationFeatures(DeserializationFeature.READ_ENUMS_USING_TO_STRING.getMask())) {
                return null;
            }

            // If there is a JsonCreator annotation we should use that instead of the CustomEnumDeserializer
            final Collection<AnnotatedMethod> factoryMethods = desc.getFactoryMethods();
            if (factoryMethods != null) {
                for (AnnotatedMethod am : factoryMethods) {
                    final JsonCreator creator = am.getAnnotation(JsonCreator.class);
                    if (creator != null) {
                        return EnumDeserializer.deserializerForCreator(config, type, am);
                    }
                }
            }

            if (IEmus.class.isAssignableFrom(type)) {
                return new CustomEnumDeserializer((Class<IEmus>) type);
            }
            return null;
        }
    }

    private static class CustomEnumSerializer extends StdScalarSerializer<IEmus> {

        protected CustomEnumSerializer() {
            super(IEmus.class);
        }

        @Override
        public void serialize(IEmus value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonGenerationException {
            jgen.writeNumber(value.getValue());
        }
    }

    private static class CustomEnumSerializers extends Serializers.Base {
        @Override
        public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
            //通过String序列化
            if (config.hasSerializationFeatures(SerializationFeature.WRITE_ENUMS_USING_TO_STRING.getMask())) {
                return null;
            }

            if (IEmus.class.isAssignableFrom(type.getRawClass())) {
                return new CustomEnumSerializer();
            }
            return super.findSerializer(config, type, beanDesc);
        }
    }

    @Override
    public String getModuleName() {
        return "custom-enums";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(final SetupContext context) {
        context.addDeserializers(new CustomEnumDeserializers());
        context.addSerializers(new CustomEnumSerializers());
    }
}

