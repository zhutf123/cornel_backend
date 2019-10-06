/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util.json;

import com.demai.cornel.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.util.Date;

/**
 * Create By zhutf  19-10-6  下午3:31
 */
public class JsonDateModule extends SimpleModule {

    private static class JsonDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = jp.getValueAsString();
            if (StringUtils.isBlank(text)) {
                return null;
            }

            try {
                DateTime dt = JsonDateTimeUtil.parseJsonDate(text);
                return new LocalDateTime(dt.getMillis()).toDate();
            } catch (Exception e) {
                throw ctxt.mappingException(e.getMessage());
            }
        }
    }

    private static class JsonDateSerializer extends JsonSerializer<Date> {

        private String getJsonDateString(Date date) {
            //return String.format("/Date(%d)/", date.getTime());
            return String.format("/Date(%d+0800)/", date.getTime() - DateUtils.TZ_CST_MS);
        }

        @Override
        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            value = value == null ? DateUtils.AD_ONE_DATE : value;
            jgen.writeString(getJsonDateString(value));
        }
    }

    @Override
    public String getModuleName() {
        return "json-date";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        addSerializer(Date.class, new JsonDateSerializer());
        addDeserializer(Date.class, new JsonDateDeserializer());
        super.setupModule(context);
    }
}
