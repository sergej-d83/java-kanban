package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.tasks.Epic;
import ru.yandex.practicum.taskmanager.tasks.SubTask;
import ru.yandex.practicum.taskmanager.tasks.Task;

import java.util.HashMap;
import java.util.Map;

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

    public void getAllTasks() {
        for (Map.Entry<Integer, Task> task : taskMap.entrySet()) {
            System.out.print("ID: " + task.getKey() + ", ");
            System.out.println(task.getValue());
        }
    }

    public void getAllEpics() {
        for (Map.Entry<Integer, Epic> epic : epicMap.entrySet()) {
            System.out.print("ID: " + epic.getKey() + ", ");
            System.out.println(epic.getValue());
        }
    }

    public void getAllSubTasks() {
        for (Map.Entry<Integer, SubTask> subTask : subTaskMap.entrySet()) {
            System.out.print("ID: " + subTask.getKey() + ", ");
            System.out.println(subTask.getValue());
        }
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

        //Putting SubTask in the Map
        subTaskMap.put(id, subTask);
        //Adding ID of the SubTask to the ArrayList in Epic
        epicMap.get(epicId).setSubTaskId(id);
    }

    public void clearAllTasks() {
        taskMap.clear();
    }

    public void clearAllEpics() {
        epicMap.clear();
    }

    public void clearAllSubTasks() {
        subTaskMap.clear();
    }

    public void removeTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Задача под номером " + id + " не существует.");
        } else {
            taskMap.remove(id);
        }
    }

    public void removeEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            System.out.println("Задача под номером " + id + " не существует.");
        } else {
            epicMap.remove(id);
        }
    }

    public void removeSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            System.out.println("Задача под номером " + id + " не существует.");
        } else {
            subTaskMap.remove(id);
        }
    }

    public void gettingTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return;
        }
        System.out.println(taskMap.get(id));
    }

    public void gettingEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        System.out.println(epicMap.get(id));
    }

    public void gettingSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return;
        }
        System.out.println(subTaskMap.get(id));
    }


    public enum Status {
        NEW, IN_PROGRESS, DONE
    }
}
