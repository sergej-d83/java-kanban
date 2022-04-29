package ru.yandex.practicum.taskmanager.tasks;

import ru.yandex.practicum.taskmanager.TaskManager;
import ru.yandex.practicum.taskmanager.TaskManager.Status;

public class Task {

    protected String taskName;
    protected String taskDescription;
    protected Status status;

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task {" +
                "taskName = '" + taskName + '\'' +
                ", taskDescription = '" + taskDescription + '\'' +
                ", taskStatus = '" + status + '\'' +
                '}';
    }
}
