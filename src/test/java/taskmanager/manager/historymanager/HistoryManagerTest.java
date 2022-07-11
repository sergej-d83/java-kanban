package taskmanager.manager.historymanager;

import org.junit.jupiter.api.BeforeEach;

public abstract class HistoryManagerTest<T extends HistoryManager> {

    public abstract T createHistoryManager();
    public T historyManager;

    @BeforeEach
    public void updateTaskManager() {
        historyManager = createHistoryManager();
    }
}