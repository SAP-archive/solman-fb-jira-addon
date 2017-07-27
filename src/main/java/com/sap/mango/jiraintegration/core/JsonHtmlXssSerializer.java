package com.sap.mango.jiraintegration.core;

import com.atlassian.html.encode.HtmlEncoder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Jackson Serializer, executes xss checks & html encoding
 */
public class JsonHtmlXssSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (value != null) {
            try {
                gen.writeStartObject();
                for (Field field : value.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getGenericType().getTypeName().equals("java.lang.String")) {
                        String fieldValue = (String) field.get(value);
                        field.set(value, encodeHtml(fieldValue));
                    }
                    gen.writeObjectField(field.getName(), field.get(value));
                }
                gen.writeEndObject();
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(JsonHtmlXssSerializer.class.getName()).log(Level.ERROR, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(JsonHtmlXssSerializer.class.getName()).log(Level.ERROR, null, ex);
            }
        }
    }

    private String encodeHtml(String value) {
        return HtmlEncoder.encode(value);
    }

}
