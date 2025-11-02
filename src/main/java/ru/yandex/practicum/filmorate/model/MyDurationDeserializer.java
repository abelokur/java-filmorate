package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class MyDurationDeserializer extends JsonDeserializer<Duration> {
    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        System.out.println("JsonParser jsonParser:: " + jsonParser);
        long minutes = jsonParser.getLongValue();
        System.out.println("deserialize(: " + minutes);
        System.out.println("Duration.ofMinutes(minutes): " + Duration.ofMinutes(minutes));
        //return Duration.ofSeconds(minutes);
        return Duration.ofMinutes(minutes);
    }
}
