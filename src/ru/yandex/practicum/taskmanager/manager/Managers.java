package ru.yandex.practicum.taskmanager.manager;

public final class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
