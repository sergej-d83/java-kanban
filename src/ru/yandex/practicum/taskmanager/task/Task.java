package ru.yandex.practicum.taskmanager.task;


public class Task {

    protected final String taskName;
    protected final String taskDescription;
    protected Status status;
    protected int id;
    protected TaskType type;

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = Status.NEW;
    }

    public Task(int id, TaskType type, String taskName, Status status, String taskDescription) {
        this.id = id;
        this.type = type;
        this.taskName = taskName;
        this.status = status;
        this.taskDescription = taskDescription;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
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
