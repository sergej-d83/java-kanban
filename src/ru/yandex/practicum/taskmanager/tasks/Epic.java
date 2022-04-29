package ru.yandex.practicum.taskmanager.tasks;

import ru.yandex.practicum.taskmanager.TaskManager.Status;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTaskId;

    public Epic(String taskName, String taskDescription, Status status) {
        super(taskName, taskDescription, status);
        subTaskId = new ArrayList<>();
    }

    public void setSubTaskId(int id) {
        subTaskId.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
