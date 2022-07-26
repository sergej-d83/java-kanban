package yandex.practicum.taskmanager;

import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.server.HttpTaskServer;
import yandex.practicum.taskmanager.manager.server.KVServer;
import yandex.practicum.taskmanager.manager.taskmanager.HttpTaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {

        KVServer cloudServer = new KVServer();
        cloudServer.start();

        HttpTaskManager manager = Managers.getHttpTaskManager("http://localhost:8078");

        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }
}
