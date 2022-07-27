package yandex.practicum.taskmanager.manager;

import yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import yandex.practicum.taskmanager.manager.taskmanager.HttpTaskManager;
import yandex.practicum.taskmanager.manager.historymanager.InMemoryHistoryManager;

public final class Managers {

    public static HttpTaskManager getHttpTaskManager(String serverAddress) {
        return new HttpTaskManager(serverAddress);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
