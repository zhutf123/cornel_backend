/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.demai.cornel.util.DateFormatUtils;
import com.demai.cornel.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * Create By zhutf  19-10-6  下午3:53
 */
public class JsonDateTimeModule extends SimpleModule {

    private static class JsonDateTimeDeserializer extends JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                JsonProcessingException {
            String text = jp.getValueAsString();
            if (StringUtils.isBlank(text)) {
                return null;
            }

            try {
                DateTime dt = JsonDateTimeUtil.parseJsonDate(text);
                return dt;
            } catch (Exception e) {
                throw ctxt.mappingException(e.getMessage());
            }
        }
    }

    private static class JsonDateTimeSerializer extends JsonSerializer<DateTime> {

        @Override public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            if (value != null) {
                jgen.writeString(DateFormatUtils.format(value, DateUtils.TZ_CST, DateFormatUtils.ISO_DATETIME_PATTERN));
            }
        }
    }

    @Override
    public String getModuleName() {
        return "json-datetime";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(Module.SetupContext context) {
        addSerializer(DateTime.class, new JsonDateTimeSerializer());
        addDeserializer(DateTime.class, new JsonDateTimeDeserializer());
        super.setupModule(context);
    }
}
