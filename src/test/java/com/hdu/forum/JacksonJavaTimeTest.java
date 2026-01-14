package com.hdu.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonJavaTimeTest {
    @Test
    public void testSerializeDeserializeLocalDateTime() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 注册JavaTimeModule支持LocalDateTime
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        LocalDateTime now = LocalDateTime.now();
        String json = objectMapper.writeValueAsString(now);
        System.out.println("Serialized JSON: " + json);
        
        LocalDateTime deserialized = objectMapper.readValue(json, LocalDateTime.class);
        assertEquals(now, deserialized);
    }
}