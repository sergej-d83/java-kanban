package yandex.practicum.taskmanager.manager.taskmanager;

import yandex.practicum.taskmanager.task.Status;
import yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    int generateId();

    void setId(int id);

    int getId();

    Map<Integer, Task> getTaskMap();

    Map<Integer, Epic> getEpicMap();

    Map<Integer, SubTask> getSubTaskMap();

    void setTaskMap(int id, Task task);

    void setEpicMap(int id, Epic epic);

    void setSubTaskMap(int id, SubTask subTask);

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

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    // 6. Удаление по идентификатору

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    // 7. Получение списка всех подзадач определённого эпика

    List<SubTask> getSubTasksOfEpic(int epicId);

    Status checkStatus(int epicId);

    Set<Task> getPrioritizedTasks();

    void calculateEpicTimeAndDuration(Epic epic);

}
