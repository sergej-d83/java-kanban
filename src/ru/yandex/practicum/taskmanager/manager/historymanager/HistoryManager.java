package ru.yandex.practicum.taskmanager.manager.historymanager;

import ru.yandex.practicum.taskmanager.task.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
