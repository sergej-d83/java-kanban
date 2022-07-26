package taskmanager.manager.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;
import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.server.HttpTaskServer;
import yandex.practicum.taskmanager.manager.server.KVServer;
import yandex.practicum.taskmanager.manager.server.adapter.DurationAdapter;
import yandex.practicum.taskmanager.manager.server.adapter.LocalDateTimeAdapter;
import yandex.practicum.taskmanager.manager.taskmanager.HttpTaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    static HttpTaskManager taskManager;
    static KVServer cloudServer;
    static HttpTaskServer taskServer;
    static final String taskServerAddress = "http://localhost:8080";
    static final String cloudServerAddress = "http://localhost:8078";
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public LocalDateTime time1 = LocalDateTime.of(2022, 7, 26, 10, 0);
    public LocalDateTime time2 = LocalDateTime.of(2022, 7, 26, 12, 0);
    public LocalDateTime time3 = LocalDateTime.of(2022, 7, 26, 14, 0);
    public Duration duration = Duration.ofMinutes(30);

    @BeforeAll
    static void startingServer() {
        try {
            cloudServer = new KVServer();
            cloudServer.start();
            taskManager = Managers.getHttpTaskManager(cloudServerAddress);
            taskServer = new HttpTaskServer(taskManager);
            taskServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterAll
    static void stoppingServer() {
        taskServer.stop();
        cloudServer.stop();
    }

    @Test
    public void shouldReturnEmptyPrioritizedTasksList() {
        String response = getRequest("/tasks");
        assertEquals(response, "[]");
    }

    @Test
    public void shouldReturnAllTasksEmptyList() {
        String response = getRequest("/tasks/task");
        String json = gson.toJson(taskManager.getTaskMap());
        assertEquals(response, json);
    }

    @Test
    public void getTaskByIdIncorrectId() {
        String response = getRequest("/tasks/task/?id=0");
        assertEquals(response, "null");
    }

    @Test
    public void deleteTaskByIdNoTask() {
        assertAll(() -> deleteRequest("/tasks/task?id=0"));
    }

    @Test
    public void shouldReturnAllEpicsEmptyList() {
        String response = getRequest("/tasks/epic");
        String json = gson.toJson(taskManager.getEpicMap());
        assertEquals(response, json);
    }

    @Test
    public void getEpicByIdIncorrectId() {
        String response = getRequest("/tasks/epic/?id=0");
        assertEquals(response, "null");
    }

    @Test
    public void deleteEpicByIdNoEpics() {
        assertAll(() -> deleteRequest("/tasks/epic?id=0"));
    }

    @Test
    public void shouldReturnAllSubTasksEmptyList() {
        String response = getRequest("/tasks/subtask");
        String json = gson.toJson(taskManager.getSubTaskMap());
        assertEquals(response, json);
    }

    @Test
    public void getSubTaskByIdIncorrectId() {
        String response = getRequest("/tasks/subtask/?id=0");
        assertEquals(response, "null");
    }

    @Test
    public void deleteSubTaskByIdNoTask() {
        assertAll(() -> deleteRequest("/tasks/subtask?id=0"));
    }

    @Test
    public void shouldCreateAndGetTask() {
        String response = getRequest("/tasks/task");
        assertEquals(response, gson.toJson(taskManager.getTaskMap()));
        Task task = new Task(0, "task", "task1", time1, duration);
        postRequest("/tasks/task", task);
        response = getRequest("/tasks/task");
        String jsonTask = gson.toJson(taskManager.getTaskMap());
        assertEquals(response, jsonTask);
    }

    @Test
    public void shouldCreateAndGetEpic() {
        String response = getRequest("/tasks/epic");
        String json = gson.toJson(taskManager.getEpicMap());
        assertEquals(response, json);
        Epic epic = new Epic(1, "epic", "epic1");
        postRequest("/tasks/epic", epic);
        response = getRequest("/tasks/epic");
        String jsonTask = gson.toJson(taskManager.getEpicMap());
        assertEquals(response, jsonTask);
    }

    @Test
    public void shouldCreateAndGetSubTask() {
        String response = getRequest("/tasks/subtask");
        String json = gson.toJson(taskManager.getSubTaskMap());
        assertEquals(response, json);
        Epic epic = new Epic(2, "epic", "epic1");
        SubTask subTask = new SubTask(3, "subtask", "subtask1", time2, duration, 2);
        postRequest("/tasks/epic", epic);
        postRequest("/tasks/subtask", subTask);
        response = getRequest("/tasks/subtask");
        String jsonTask = gson.toJson(taskManager.getSubTaskMap());
        assertEquals(response, jsonTask);
    }

    @Test
    public void shouldReturnSubTasksOfEpic() {
        Epic epic = new Epic(5, "epic", "epic1");
        SubTask subTask = new SubTask(6, "subtask", "subtask1", time3, duration, 5);
        postRequest("/tasks/epic", epic);
        postRequest("/tasks/subtask", subTask);
        String json = gson.toJson(taskManager.getSubTasksOfEpic(5));
        String response = getRequest("/tasks/subtask/epic?id=5");
        assertEquals(response, json);
    }

    @Test
    public void shouldReturnAllCreatedTasksNotEmptyList() {
        Task task = new Task(10, "task", "task10",
                LocalDateTime.of(2022, 7, 26, 16, 0),
                Duration.ofMinutes(30));
        postRequest("/tasks/task", task);
        String json = gson.toJson(taskManager.getPrioritizedTasks());
        String response = getRequest("/tasks");
        assertEquals(response, json);
    }


    @Test
    public void shouldReturnAllTasks() {
        Task task = new Task(11, "task", "task11",
                LocalDateTime.of(2022, 7, 26, 17, 30),
                Duration.ofMinutes(30));
        taskManager.createTask(task);
        String json = gson.toJson(taskManager.getTaskMap());
        String response = getRequest("/tasks/task");
        assertEquals(response, json);
    }

    @Test
    public void shouldReturnAllEpics() {
        Epic epic = new Epic(12, "epic", "epic12");
        taskManager.createEpic(epic);
        String json = gson.toJson(taskManager.getEpicMap());
        String response = getRequest("/tasks/epic");
        assertEquals(response, json);
    }

    @Test
    public void shouldReturnAllSubTasks() {
        Epic epic = new Epic(13, "epic", "epic13");
        SubTask subTask = new SubTask(14, "subtask", "subtask14",
                LocalDateTime.of(2022, 7, 27, 19, 30),
                Duration.ofMinutes(30), 13);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        String json = gson.toJson(taskManager.getSubTaskMap());
        String response = getRequest("/tasks/subtask");
        assertEquals(response, json);
    }

    @Test
    public void shouldReturnHistory() {
        assertEquals(gson.toJson(taskManager.getHistoryManager().getHistory()), "[]");
        Task task1 = new Task(15, "task", "task15",
                LocalDateTime.of(2022, 7, 26, 20, 30),
                Duration.ofMinutes(30));
        Task task2 = new Task(16, "task", "task15",
                LocalDateTime.of(2022, 7, 26, 22, 30),
                Duration.ofMinutes(30));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        getRequest("/tasks/task?id=15");
        getRequest("/tasks/task?id=16");

        String json = gson.toJson(taskManager.getHistoryManager().getHistory());
        String response = getRequest("/tasks/history");

        assertEquals(response, json);
    }

    @Test
    public void shouldSaveTasks() {
        deleteRequest("/tasks/task");
        assertEquals(getRequest("/tasks/task"), gson.toJson(taskManager.getTaskMap()));
        Task task1 = new Task(15, "task", "task15",
                LocalDateTime.of(2022, 7, 26, 20, 30),
                Duration.ofMinutes(30));
        taskManager.getTaskMap().put(task1.getId(), task1);
        taskManager.save();
        assertEquals(getRequest("/tasks/task"), gson.toJson(taskManager.getTaskMap()));
    }

    @Test
    public void shouldLoadDataFromServer() {
        Task task = new Task(20, "task", "task15",
                LocalDateTime.of(2022, 7, 28, 20, 30),
                Duration.ofMinutes(30));
        taskManager.createTask(task);
        assertEquals(getRequest("/tasks/task"), gson.toJson(taskManager.getTaskMap()));
        HttpTaskManager newTaskManager = new HttpTaskManager(cloudServerAddress);
        assertTrue(newTaskManager.getTaskMap().isEmpty());
        newTaskManager.load();
        assertEquals(getRequest("/tasks/task"), gson.toJson(newTaskManager.getTaskMap()));
    }

    public String getRequest(String path) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(taskServerAddress + path))
                .build();
        String response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    public void postRequest(String path, Task task) {

        String jsonTask = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .uri(URI.create(taskServerAddress + path))
                .header("Content-Type", "application/json")
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRequest(String path) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(taskServerAddress + path))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}