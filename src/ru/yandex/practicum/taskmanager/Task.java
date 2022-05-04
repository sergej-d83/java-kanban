package ru.yandex.practicum.taskmanager;


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

    @Override
    public String toString() {
        return "Task {" +
                "taskName = '" + taskName + '\'' +
                ", taskDescription = '" + taskDescription + '\'' +
                ", taskStatus = '" + status + '\'' +
                '}';
    }

    public enum Status {
        NEW, IN_PROGRESS, DONE
    }
}
