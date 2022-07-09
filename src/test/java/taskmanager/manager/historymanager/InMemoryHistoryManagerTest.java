package taskmanager.manager.historymanager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @Override
    public InMemoryHistoryManager createHistoryManager() {
        return new InMemoryHistoryManager();
    }
}