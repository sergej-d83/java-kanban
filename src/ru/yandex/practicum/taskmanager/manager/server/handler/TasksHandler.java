package yandex.practicum.taskmanager.manager.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import yandex.practicum.taskmanager.manager.taskmanager.TaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TasksHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    public void handle(HttpExchange exchange) {

        String response;
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();

        try (exchange) {
            switch (method) {
                case "GET" -> {
                    response = handleGetRequest(exchange, uri);
                    writeResponse(exchange, response);
                }
                case "POST" -> handlePostRequest(exchange);
                case "DELETE" -> handleDeleteRequest(exchange, uri);
                default -> throw new IllegalArgumentException("Unexpected value. " + method);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String handleGetRequest(HttpExchange exchange, URI uri) throws IOException {

        System.out.println("Handle GET request...");

        int id = getIdFromUri(uri);
        String type = getRequestedTaskType(uri.getPath());
        String response;

        switch (type) {
            case "tasks":
                return gson.toJson(manager.getPrioritizedTasks());
            case "task":
                if (id < 0) {
                    return gson.toJson(manager.getAllTasks());
                } else {
                    Task task = manager.getTaskById(id);
                    response = gson.toJson(task);
                }
                return response;
            case "epic":
                if (id < 0) {
                    return gson.toJson(manager.getAllEpics());
                } else {
                    Epic epic = manager.getEpicById(id);
                    response = gson.toJson(epic);
                }
                return response;
            case "subtask":
                if (id < 0) {
                    return gson.toJson(manager.getAllSubTasks());
                } else {
                    SubTask subTask = manager.getSubTaskById(id);
                    response = gson.toJson(subTask);
                }
                return response;
            case "subtaskepic":
                if (id >= 0) {
                    return gson.toJson(manager.getSubTasksOfEpic(id));
                } else {
                    throw new IllegalArgumentException("Incorrect ID");
                }
            case "history":
                return gson.toJson(manager.getHistoryManager().getHistory());
            default:
                exchange.sendResponseHeaders(404, 0);
                throw new IOException("Invalid url.");
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {

        try {
            System.out.println("Handle POST request...");

            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), UTF_8);
            Task task = parseJsonData(body, gson);

            if (task instanceof Epic) {
                Epic epic;
                epic = (Epic) parseJsonData(body, gson);

                if (manager.getEpicMap().containsKey(epic.getId())) {
                    manager.updateEpic(epic);
                } else {
                    manager.createEpic(epic);
                }
            } else if (task instanceof SubTask) {
                SubTask subTask;
                subTask = (SubTask) parseJsonData(body, gson);

                if (manager.getSubTaskMap().containsKey(subTask.getId())) {
                    manager.updateSubTask(subTask);
                } else {
                    manager.createSubTask(subTask);
                }
            } else {

                if (manager.getTaskMap().containsKey(task.getId())) {
                    manager.updateTask(task);
                } else {
                    manager.createTask(task);
                }
            }
            exchange.sendResponseHeaders(201, 0);
        } catch (IOException e) {
            exchange.sendResponseHeaders(400, 0);
            System.out.println("Error. " + e.getMessage());
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, URI uri) throws IOException {

        int id = getIdFromUri(uri);
        String type = getRequestedTaskType(uri.getPath());

        System.out.println("Handle DELETE request...");

        try {
            switch (type) {
                case "task":
                    if (id >= 0) {
                        manager.removeTaskById(id);
                    } else {
                        manager.clearAllTasks();
                    }
                    break;
                case "epic":
                    if (id >= 0) {
                        manager.removeEpicById(id);
                    } else {
                        manager.clearAllEpics();
                    }
                    break;
                case "subtask":
                    if (id >= 0) {
                        manager.removeSubTaskById(id);
                    } else {
                        manager.clearAllSubTasks();
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            exchange.sendResponseHeaders(400, 0);
            System.out.println("Error. " + e.getMessage());
        }
        exchange.sendResponseHeaders(205, 0);
    }

    private String getRequestedTaskType(String path) {

        String[] types = path.split("/");

        if (types.length < 3) {
            return types[1];
        } else if (types.length > 3 && types[3].equals("epic")) {
            return types[2] + types[3];
        } else {
            return types[2];
        }
    }

    private int getIdFromUri(URI uri) {

        String query = uri.getQuery();

        if (query == null) {
            return -1;
        } else {
            return Integer.parseInt(query.split("=")[1]);
        }
    }

    private void writeResponse(HttpExchange exchange, String response) {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders((response.isEmpty()) ? 400 : 200, 0);
            os.write(response.getBytes(UTF_8));
        } catch (IOException e) {
            System.out.println("Error. " + e.getMessage());
        }
    }

    private Task parseJsonData(String body, Gson gson) throws InvalidObjectException {

        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            throw new InvalidObjectException("Invalid JSON.");
        }

        String type = "";
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("type")) {
            type = jsonObject.get("type").getAsString();
        }

        if (jsonObject.size() < 7 && !type.equals("EPIC")) {
            throw new InvalidObjectException("Invalid JSON.");
        }

        return switch (type) {
            case "TASK" -> gson.fromJson(body, Task.class);
            case "EPIC" -> gson.fromJson(body, Epic.class);
            case "SUBTASK" -> gson.fromJson(body, SubTask.class);
            default -> throw new IllegalArgumentException("Invalid Task type.");
        };
    }


}

