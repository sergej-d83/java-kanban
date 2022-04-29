package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.tasks.Epic;
import ru.yandex.practicum.taskmanager.tasks.SubTask;
import ru.yandex.practicum.taskmanager.TaskManager.Status;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        manager.createEpic(new Epic("name", "bla", Status.NEW));
        manager.createSubTask(new SubTask("name", "blabla", Status.NEW, 0));
        manager.createSubTask(new SubTask("name", "blabla", Status.NEW, 0));

    }
}
