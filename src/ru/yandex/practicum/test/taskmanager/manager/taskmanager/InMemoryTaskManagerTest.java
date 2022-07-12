package taskmanager.manager.taskmanager;

import org.junit.jupiter.api.Test;
import yandex.practicum.taskmanager.manager.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    public InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void managerIsNotNullAndHashMapsAreEmpty() {
        assertNotNull(manager);
        assertTrue(manager.getTaskMap().isEmpty());
        assertTrue(manager.getEpicMap().isEmpty());
        assertTrue(manager.getSubTaskMap().isEmpty());
    }
}