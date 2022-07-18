package yandex.practicum.taskmanager.manager.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.taskmanager.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final Gson gson;
    private final FileBackedTaskManager manager;
    private final Charset DEFAULT_CHARSETS = StandardCharsets.UTF_8;

    public TaskHandler() {
        this.gson = new Gson();
        this.manager = Managers.getFileBackedManager(new File("tasks.csv"));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method) {
            case "GET":
                handleGetRequest(path, exchange);
                break;
            case "POST":
                break;
            case "DELETE":
                break;
            default:

        }

//        try (OutputStream outputStream = exchange.getResponseBody()){
//            outputStream.write(response.getBytes());
//        }
    }

    private void handleGetRequest(String path, HttpExchange exchange) throws IOException {

        try (OutputStream outputStream = exchange.getResponseBody()) {
            String taskToJson = "";
            if (path.endsWith("/tasks")) {
                getAllTasks(outputStream);
            } else if (path.endsWith("/tasks/task")) {
                System.out.println("Handle GET request...");
                exchange.sendResponseHeaders(200, 0);
                taskToJson = gson.toJson(manager.getTaskMap());
            } else if (path.endsWith("/tasks/epic")) {
                System.out.println("Handle GET request...");
                exchange.sendResponseHeaders(200, 0);
                taskToJson = gson.toJson(manager.getEpicMap());
            } else if (path.endsWith("/tasks/subtask")) {
                System.out.println("Handle GET request...");
                exchange.sendResponseHeaders(200, 0);
                taskToJson = gson.toJson(manager.getSubTaskMap());
            } else if (path.endsWith("/tasks/history")) {
                System.out.println("Handle GET request...");
                exchange.sendResponseHeaders(200, 0);
                taskToJson = gson.toJson(manager.getHistoryManager().getHistory());
            } else {
                System.out.println("Resource not found...");
                exchange.sendResponseHeaders(404, 0);
            }
            outputStream.write(taskToJson.getBytes(DEFAULT_CHARSETS));
        }
    }

    private void getAllTasks(OutputStream outputStream) throws IOException {

        List<String> tasks = new ArrayList<>();
        tasks.add(gson.toJson(manager.getTaskMap()));
        tasks.add(gson.toJson(manager.getEpicMap()));
        tasks.add(gson.toJson(manager.getSubTaskMap()));

        try (outputStream) {
            for (String task : tasks) {
                outputStream.write(task.getBytes(DEFAULT_CHARSETS));
            }
        }
    }
}
