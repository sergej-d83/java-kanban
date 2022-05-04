package ru.yandex.practicum.taskmanager;


import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subTaskIdList;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subTaskIdList = new ArrayList<>();
    }

    public void setSubTaskIdList(int id) {
        subTaskIdList.add(id);
    }

    public ArrayList<Integer> getSubTaskIdList() {
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
