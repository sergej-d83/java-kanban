package ru.yandex.practicum.taskmanager.manager;

import ru.yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import ru.yandex.practicum.taskmanager.manager.historymanager.InMemoryHistoryManager;
import ru.yandex.practicum.taskmanager.manager.taskmanager.InMemoryTaskManager;
import ru.yandex.practicum.taskmanager.manager.taskmanager.TaskManager;

public final class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
