package ru.yandex.practicum.taskmanager.task;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subTaskIdList;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subTaskIdList = new ArrayList<>();
    }

    public Epic(int id, TaskType type, String taskName, Status status, String taskDescription) {
        super(id, type, taskName, status, taskDescription);
        subTaskIdList = new ArrayList<>();
    }

    public void setSubTaskIdList(int id) {
        subTaskIdList.add(id);
    }

    public List<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    @Override
    public String toString() {
        return "Epic {" +
                "subTaskId = " + subTaskIdList +
                ", taskName = '" + taskName + '\'' +
                ", taskDescription = '" + taskDescription + '\'' +
                ", status= '" + status + '\'' +
                '}';
    }
}
