package yandex.practicum.taskmanager.manager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import yandex.practicum.taskmanager.manager.server.adapter.DurationAdapter;
import yandex.practicum.taskmanager.manager.server.adapter.LocalDateTimeAdapter;
import yandex.practicum.taskmanager.manager.server.handler.TasksHandler;
import yandex.practicum.taskmanager.manager.taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private TaskManager manager;
    private HttpServer server;
    private Gson gson;
    private final int PORT = 8080;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(this.manager, gson));
    }

    public void start() {
        server.start();
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("Server stopped");
    }
}
