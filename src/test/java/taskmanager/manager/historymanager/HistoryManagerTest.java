package taskmanager.manager.historymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.task.Task;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {

    public abstract T createHistoryManager();
    public T historyManager;

    @BeforeEach
    public void updateTaskManager() {
        historyManager = createHistoryManager();
    }

    @Test
    void shouldAddTaskToHistory() {
        historyManager.add(null);

        assertEquals(0, historyManager.getHistory().size());
        Task task = new Task(0, "Task", "Description");
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void shouldNotAddDoubleTasks() {
        assertEquals(0, historyManager.getHistory().size());

        Task task1 = new Task(0,"Task1", "Description");
        Task task2 = new Task(0,"Task2", "Description");
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        assertEquals(0, historyManager.getHistory().size());

        Task task = new Task(0, "Task", "Description");
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(0);
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void shouldRemoveBeginMiddleEndTask() {
        assertEquals(0, historyManager.getHistory().size());

        Task task1 = new Task(0, "Task1", "Description");
        Task task2 = new Task(1, "Task2", "Description");
        Task task3 = new Task(2, "Task3", "Description");
        Task task4 = new Task(3, "Task4", "Description");
        Task task5 = new Task(4, "Task5", "Description");

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

        Task task1 = new Task(0, "Task1", "Description");
        Task task2 = new Task(1, "Task2", "Description");
        Task task3 = new Task(2, "Task3", "Description");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        assertEquals(3, historyManager.getHistory().size());

    }
}