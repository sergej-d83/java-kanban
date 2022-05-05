package ru.yandex.practicum.taskmanager.task;


public class Task {

    protected String taskName;
    protected String taskDescription;
    protected Status status;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = Status.NEW;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
