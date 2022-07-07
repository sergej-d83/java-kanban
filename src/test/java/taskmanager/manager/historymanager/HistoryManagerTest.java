package taskmanager.manager.historymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.task.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    public void createManager() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.add(null);

        assertEquals(0, historyManager.getHistory().size());

        Task task = new Task("Task", "Description");
        task.setId(0);

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void shouldNotAddDoubleTasks() {
        assertEquals(0, historyManager.getHistory().size());

        Task task1 = new Task("Task1", "Description");
        Task task2 = new Task("Task2", "Description");
        task1.setId(0);
        task2.setId(0);

        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        assertEquals(0, historyManager.getHistory().size());

        Task task = new Task("Task", "Description");
        task.setId(0);

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(0);
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void shouldRemoveBeginMiddleEndTask() {
        assertEquals(0, historyManager.getHistory().size());

        Task task1 = new Task("Task1", "Description");
        Task task2 = new Task("Task2", "Description");
        Task task3 = new Task("Task3", "Description");
        Task task4 = new Task("Task4", "Description");
        Task task5 = new Task("Task5", "Description");
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        task4.setId(3);
        task5.setId(4);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);

        assertEquals(5, historyManager.getHistory().size());

        historyManager.remove(0);
        historyManager.remove(2);
        historyManager.remove(4);

        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void shouldReturnHistoryList() {
        assertEquals(0, historyManager.getHistory().size());

        Task task1 = new Task("Task1", "Description");
        Task task2 = new Task("Task2", "Description");
        Task task3 = new Task("Task3", "Description");
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        assertEquals(3, historyManager.getHistory().size());

    }
}