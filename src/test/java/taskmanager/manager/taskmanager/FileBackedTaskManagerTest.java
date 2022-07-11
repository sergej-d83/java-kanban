package taskmanager.manager.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.task.Epic;
import taskmanager.task.SubTask;
import taskmanager.task.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    @Override
    public FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("tasks.csv"));
    }

    FileBackedTaskManager manager1;
    FileBackedTaskManager manager2;

    @BeforeEach
    public void updateManager() {
        manager1 = createTaskManager();
    }

    @Test
    public void epicWithoutSubTasks(){
        manager1.createTask(new Task(0, "Task", "Description", time1, duration));
        manager1.createEpic(new Epic(1, "Epic", "Description"));

        assertEquals(1, manager1.getTaskMap().size());
        assertEquals(1, manager1.getEpicMap().size());
        assertEquals(0, manager1.getSubTaskMap().size());

        manager2 = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        assertEquals(1, manager2.getTaskMap().size());
        assertEquals(1, manager2.getEpicMap().size());
        assertEquals(0, manager2.getSubTaskMap().size());
    }

    @Test
    public  void emptyHistoryTest() {
        manager1.createTask(new Task(0, "Task", "Description", time1, duration));
        manager1.createEpic(new Epic(1, "Epic", "Description"));
        manager1.createSubTask(new SubTask(2, "SubTask", "Description",
                time2, duration, 1));

        assertEquals(1, manager1.getTaskMap().size());
        assertEquals(1, manager1.getEpicMap().size());
        assertEquals(1, manager1.getSubTaskMap().size());
        assertEquals(0, manager1.getHistoryManager().getHistory().size());

        manager2 = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        assertEquals(1, manager2.getTaskMap().size());
        assertEquals(1, manager2.getEpicMap().size());
        assertEquals(1, manager2.getSubTaskMap().size());
        assertEquals(0, manager2.getHistoryManager().getHistory().size());

    }

    @Test
    public void noTasksTest() {
        manager1.clearAllTasks();
        manager1.clearAllEpics();
        manager1.clearAllSubTasks();

        assertEquals(0, manager1.getTaskMap().size());
        assertEquals(0, manager1.getEpicMap().size());
        assertEquals(0, manager1.getSubTaskMap().size());

        manager2 = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        assertEquals(0, manager2.getTaskMap().size());
        assertEquals(0, manager2.getEpicMap().size());
        assertEquals(0, manager2.getSubTaskMap().size());
    }

}