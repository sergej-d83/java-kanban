package ru.yandex.practicum.taskmanager.manager.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;

import ru.yandex.practicum.taskmanager.manager.Managers;
import ru.yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;
import ru.yandex.practicum.taskmanager.task.Status;
import ru.yandex.practicum.taskmanager.task.Epic;

public class InMemoryTaskManager implements TaskManager {

    private int id;
    private final HashMap<Integer, Task> taskMap;
    private final HashMap<Integer, Epic> epicMap;
    private final HashMap<Integer, SubTask> subTaskMap;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
        subTaskMap = new HashMap<>();
        id = 0;
    }

    @Override
    public int generateId() {
        return id++;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // 1. Получение списка всех задач
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        if (taskMap.isEmpty()) {
            return null;
        }
        return taskMap;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        if (epicMap.isEmpty()) {
            return null;
        }
        return epicMap;
    }

    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        if (subTaskMap.isEmpty()) {
            return null;
        }
        return subTaskMap;
    }

    // 2. Удаление всех задач
    @Override
    public void clearAllTasks() {
        if (!taskMap.isEmpty()) {
            taskMap.clear();
        }
    }

    @Override
    public void clearAllEpics() {
        if (!epicMap.isEmpty()) {
            epicMap.clear();
        }
    }

    @Override
    public void clearAllSubTasks() {
        if (!subTaskMap.isEmpty()) {
            subTaskMap.clear();
        }
    }

    // 3. Получение по идентификатору
    @Override
    public Task gettingTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return null;
        }
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic gettingEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return null;
        }
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public SubTask gettingSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return null;
        }
        historyManager.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    // 4. Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void createTask(Task task) {
        taskMap.put(generateId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epicMap.put(generateId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        int id = generateId();
        int epicId = subTask.getEpicId();

        subTaskMap.put(id, subTask);
        epicMap.get(epicId).setSubTaskIdList(id);
    }

    // 5. Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра
    @Override
    public void updateTask(Task task, int id, Status status) {
        if (taskMap.containsKey(id)) {
            task.setStatus(status);
            taskMap.put(id, task);
        }
    }

    @Override
    public void updateEpic(Epic epic, int id) {
        if (!epicMap.get(id).getSubTaskIdList().isEmpty()) {
            ArrayList<Integer> subTasks = epicMap.get(id).getSubTaskIdList();

            for (Integer subId : subTasks) {
                epic.setSubTaskIdList(subId);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask, int id, Status status) {
        int epicId = subTask.getEpicId();

        if (subTaskMap.containsKey(id)) {
            subTask.setStatus(status);
            subTaskMap.put(id, subTask);
            epicMap.get(epicId).setStatus(checkStatus(epicId));
        }
    }

    // 6. Удаление по идентификатору
    @Override
    public void removeTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return;
        }
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }

        if (!epicMap.get(id).getSubTaskIdList().isEmpty()) {
            ArrayList<Integer> subTasks = epicMap.get(id).getSubTaskIdList();

            for (Integer subTaskId : subTasks) {
                subTaskMap.remove(subTaskId);
            }
        }
        epicMap.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return;
        }

        int epicId = subTaskMap.get(id).getEpicId();
        ArrayList<Integer> subTasks = epicMap.get(epicId).getSubTaskIdList();

        for (Integer subTaskId : subTasks) {
            if (subTaskId == id) {
                subTasks.remove(subTaskId);
            }
        }
        subTaskMap.remove(id);
        checkStatus(epicId);
    }

    // 7. Получение списка всех подзадач определённого эпика
    @Override
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

    @Override
    public Status checkStatus(int epicId) {
        if (!epicMap.containsKey(epicId)) {
            return null;
        } else if (epicMap.get(epicId).getSubTaskIdList().isEmpty()) {
            return Status.NEW;
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
