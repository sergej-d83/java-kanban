package ru.yandex.practicum.taskmanager.tasks;

import ru.yandex.practicum.taskmanager.TaskManager.Status;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
