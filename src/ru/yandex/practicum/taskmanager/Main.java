package yandex.practicum.taskmanager;

import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.server.HttpTaskServer;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getFileBackedManager(new File("tasks.csv")));

        server.start();

    }
}
