package yandex.practicum.taskmanager.manager.server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import yandex.practicum.taskmanager.task.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(Task.formatter));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), Task.formatter);
    }
}
