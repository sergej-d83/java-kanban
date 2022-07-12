package yandex.practicum.taskmanager.manager;

import yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import yandex.practicum.taskmanager.manager.taskmanager.FileBackedTaskManager;
import yandex.practicum.taskmanager.manager.taskmanager.InMemoryTaskManager;
import yandex.practicum.taskmanager.manager.taskmanager.TaskManager;
import yandex.practicum.taskmanager.manager.historymanager.InMemoryHistoryManager;

import java.io.File;

public final class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
