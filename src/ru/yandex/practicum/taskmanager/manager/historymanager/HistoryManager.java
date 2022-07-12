package yandex.practicum.taskmanager.manager.historymanager;

import yandex.practicum.taskmanager.task.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
