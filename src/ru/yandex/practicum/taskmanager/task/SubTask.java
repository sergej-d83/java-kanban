package ru.yandex.practicum.taskmanager.task;


public class SubTask extends Task {

    private final int epicId;

    public SubTask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask {" +
                "epicId = " + epicId +
                ", taskName = '" + taskName + '\'' +
                ", taskDescription = '" + taskDescription + '\'' +
                ", status = '" + status + '\'' +
                '}';
    }
}
