package com.cmc.meeron.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class JacksonTimeFormatConfig {

    private static final String timeFormat = "hh:mm a";
    private static final String yearMonthFormat = "yyyy/M";
    private static final String dateFormat = "yyyy/M/d";
    private static final String dateTimeFormat = "yyyy/M/d hh:mm:ss a";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Locale.setDefault(Locale.US);
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.simpleDateFormat(dateTimeFormat);
            jacksonObjectMapperBuilder.serializers(new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)));
            jacksonObjectMapperBuilder.deserializers(new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormat)));

            jacksonObjectMapperBuilder.serializers(new YearMonthSerializer(DateTimeFormatter.ofPattern(yearMonthFormat)));
            jacksonObjectMapperBuilder.deserializers(new YearMonthDeserializer(DateTimeFormatter.ofPattern(yearMonthFormat)));

            jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            jacksonObjectMapperBuilder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
        };
    }
}
