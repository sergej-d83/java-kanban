package ru.yandex.practicum.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;

import ru.yandex.practicum.taskmanager.Task.Status;

public class TaskManager {

    private int id;
    private final HashMap<Integer, Task> taskMap;
    private final HashMap<Integer, Epic> epicMap;
    private final HashMap<Integer, SubTask> subTaskMap;

    public TaskManager() {
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
        subTaskMap = new HashMap<>();
        id = 0;
    }


    private int generateId() {
        return id++;
    }

    public HashMap<Integer, Task> getAllTasks() {
        if (taskMap.isEmpty()) {
            return null;
        }
        return taskMap;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        if (epicMap.isEmpty()) {
            return null;
        }
        return epicMap;
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        if (subTaskMap.isEmpty()) {
            return null;
        }
        return subTaskMap;
    }

    public void createTask(Task task) {
        taskMap.put(generateId(), task);
    }

    public void createEpic(Epic epic) {
        epicMap.put(generateId(), epic);
    }

    public void createSubTask(SubTask subTask) {
        int id = generateId();
        int epicId = subTask.getEpicId();

        subTaskMap.put(id, subTask);
        epicMap.get(epicId).setSubTaskIdList(id);
    }

    public void clearAllTasks() {
        if (!taskMap.isEmpty()) {
            taskMap.clear();
        }
    }

    public void clearAllEpics() {
        if (!epicMap.isEmpty()) {
            epicMap.clear();
        }
    }

    public void clearAllSubTasks() {
        if (!subTaskMap.isEmpty()) {
            subTaskMap.clear();
        }
    }

    public void removeTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return;
        }
        taskMap.remove(id);
    }

    public void removeEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        epicMap.remove(id);
    }

    public void removeSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return;
        }
        subTaskMap.remove(id);
    }

    public Task gettingTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return null;
        }
        return taskMap.get(id);
    }

    public Epic gettingEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return null;
        }
        return epicMap.get(id);
    }

    public SubTask gettingSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return null;
        }
        return subTaskMap.get(id);
    }

    public ArrayList<SubTask> gettingSubTasksOfEpic(int epicId) {
        if (epicMap.get(epicId).getSubTaskIdList().isEmpty()) {
            return null;
        }

        ArrayList<SubTask> subTasks = new ArrayList<>();
        ArrayList<Integer> subTaskIds = epicMap.get(epicId).getSubTaskIdList();

        for (Integer subTask : subTaskIds) {
            subTasks.add(subTaskMap.get(subTask));
        }
        return subTasks;
    }

    public void updateTask(Task task, int id, Status status) {
        if (taskMap.containsKey(id)) {
            task.status = status;
            taskMap.put(id, task);
        }
    }

    public void updateEpic(Epic epic, int id) {
        if (!epicMap.get(id).getSubTaskIdList().isEmpty()) {
            ArrayList<Integer> subTasks = epicMap.get(id).getSubTaskIdList();

            for (Integer subId : subTasks) {
                epic.setSubTaskIdList(subId);
            }
        }
    }

    public void updateSubTask(SubTask subTask, int id, Status status) {
        if (subTaskMap.containsKey(id)) {
            subTask.status = status;
            subTaskMap.put(id, subTask);
            epicMap.get(subTask.getEpicId()).status = checkStatus(subTask.getEpicId());
        }
    }

    public Status checkStatus(int epicId) {
        if (!epicMap.containsKey(epicId) || epicMap.get(epicId).getSubTaskIdList().isEmpty()) {
            return null;
        }

        Epic tempEpic = epicMap.get(epicId);
        ArrayList<Integer> subTaskIdList = tempEpic.getSubTaskIdList();

        int countNew = 0;
        int countDone = 0;

        for (Integer subTaskId : subTaskIdList) {
            if (subTaskMap.get(subTaskId).getStatus().equals(Status.NEW)) {
                countNew++;
            } else if (subTaskMap.get(subTaskId).getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        if (countNew == subTaskIdList.size()) {
            return Status.NEW;
        } else if (countDone == subTaskIdList.size()) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }
}
