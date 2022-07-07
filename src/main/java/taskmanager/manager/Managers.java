package taskmanager.manager;

import taskmanager.manager.historymanager.HistoryManager;
import taskmanager.manager.historymanager.InMemoryHistoryManager;
import taskmanager.manager.taskmanager.FileBackedTaskManager;
import taskmanager.manager.taskmanager.InMemoryTaskManager;
import taskmanager.manager.taskmanager.TaskManager;

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
