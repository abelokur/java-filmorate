package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Duration;

public class MyDurationSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //long seconds = value.toDays();// .toSeconds();
        long seconds = value.toMinutes();// .toSeconds();
        System.out.println("valie: " + value);
        //long seconds = value.getSeconds();// .toSeconds();
        System.out.println("long seconds = value.toDays(); " + seconds);
        gen.writeNumber(seconds);
        //long seconds = value.toDays();// .toSeconds();
        //System.out.println("long seconds = value.toDays(); " + seconds);
        //gen.writeNumber(seconds);
    }
}
