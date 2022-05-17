package ru.yandex.practicum.taskmanager.manager.taskmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;
import ru.yandex.practicum.taskmanager.task.Status;
import ru.yandex.practicum.taskmanager.task.Epic;

public interface TaskManager {

    int generateId();

    HistoryManager getHistoryManager();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // 1. Получение списка всех задач

    Map<Integer, Task> getAllTasks();

    Map<Integer, Epic> getAllEpics();

    Map<Integer, SubTask> getAllSubTasks();

    // 2. Удаление всех задач

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubTasks();

    // 3. Получение по идентификатору

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    // 4. Создание. Сам объект должен передаваться в качестве параметра

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubTask(SubTask subTask);

    // 5. Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра

    void updateTask(Task task, int id, Status status);

    void updateEpic(Epic epic, int id);

    void updateSubTask(SubTask subTask, int id, Status status);

    // 6. Удаление по идентификатору

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    // 7. Получение списка всех подзадач определённого эпика

    List<SubTask> getSubTasksOfEpic(int epicId);

    Status checkStatus(int epicId);
}
