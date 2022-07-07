package taskmanager.manager.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.task.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    public abstract T createTaskManager();

    public T taskManager;

    @BeforeEach
    public void updateTaskManager() {
        taskManager = createTaskManager();
    }

    @Test
    public void generateIdTest() {
        assertEquals(0, taskManager.generateId());
        assertEquals(1, taskManager.generateId());
    }

    @Test
    public void getTaskMapTest() {
        assertEquals(0, taskManager.getTaskMap().size());
        taskManager.createTask(new Task("Task8", "Description1"));
        assertEquals(1, taskManager.getTaskMap().size());
        taskManager.removeTaskById(0);
    }

    @Test
    public void getEpicMapTest() {
        assertEquals(0, taskManager.getEpicMap().size());
        taskManager.createEpic(new Epic("Task1", "Description1"));
        assertEquals(1, taskManager.getEpicMap().size());
    }

    @Test
    public void getSubTaskMapTest() {
        assertEquals(0, taskManager.getSubTaskMap().size());

        taskManager.createEpic(new Epic("",""));
        taskManager.createSubTask(new SubTask("Task1", "Description1", 0));

        assertEquals(1, taskManager.getSubTaskMap().size());
    }

    @Test
    public void shouldClearAllTasks() {
        taskManager.createTask(new Task("Task1", "Description1"));
        taskManager.createTask(new Task("Task2", "Description2"));
        assertEquals(2, taskManager.getTaskMap().size());
        taskManager.clearAllTasks();
        assertEquals(0, taskManager.getTaskMap().size());

    }

    @Test
    public void shouldClearAllEpics() {
        taskManager.createEpic(new Epic("Epic1", "Description1"));
        taskManager.createEpic(new Epic("Epic2", "Description2"));
        assertEquals(2, taskManager.getEpicMap().size());
        taskManager.clearAllEpics();
        assertEquals(0, taskManager.getEpicMap().size());

    }

    @Test
    public void shouldClearAllSubTasks() {
        assertEquals(0, taskManager.getSubTaskMap().size());

        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.createSubTask(new SubTask("Task1", "Description1", 0));
        taskManager.createSubTask(new SubTask("Task2", "Description2", 0));

        assertEquals(2, taskManager.getSubTaskMap().size());
        taskManager.clearAllSubTasks();
        assertEquals(0, taskManager.getSubTaskMap().size());

    }

    @Test
    public void shouldReturnTaskById() {
        assertEquals(0, taskManager.getTaskMap().size());
        taskManager.createTask(new Task("Task1", "Description1"));

        Task task = new Task("Task1", "Description1");
        task.setId(0);
        task.setType(TaskType.TASK);

        assertEquals(task, taskManager.getTaskById(0));
    }

    @Test
    public void shouldReturnEpicById() {
        taskManager.createEpic(new Epic("Epic1", "Description1"));
        assertEquals(new Epic("Epic1", "Description1"), taskManager.getEpicById(0));
    }

    @Test
    public void shouldReturnSubTaskById() {
        taskManager.createEpic(new Epic("Epic1", "Description1"));
        taskManager.createSubTask(new SubTask("SubTask1", "Description1", 0));
        assertEquals(new SubTask("SubTask1", "Description1", 0), taskManager.getSubTaskById(1));
    }

    @Test
    public void shouldCreateNewTask() {
        assertEquals(0, taskManager.getTaskMap().size());
        taskManager.createTask(new Task("Task1", "Description1"));

        Task task = new Task("Task1", "Description1");
        task.setId(0);
        task.setType(TaskType.TASK);

        assertEquals(task, taskManager.getTaskById(0));
    }

    @Test
    public void shouldCreateNewEpic() {
        assertEquals(0, taskManager.getEpicMap().size());
        taskManager.createEpic(new Epic("Epic1", "Description1"));
        assertEquals(new Epic("Epic1", "Description1"), taskManager.getEpicById(0));
    }

    @Test
    public void shouldCreateNewSubTask() {
        assertEquals(0, taskManager.getSubTaskMap().size());

        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.createSubTask(new SubTask("Task1", "Description1", 0));

        assertEquals(new SubTask("Task1", "Description1", 0),
                taskManager.getSubTaskById(1));
        assertEquals(1, taskManager.getSubTaskMap().size());
    }

    @Test
    public void shouldUpdateTask() {
        taskManager.createTask(new Task("Task1", "Description"));
        taskManager.updateTask(new Task("Task2", "Description"), 0, Status.NEW);
        assertEquals(new Task("Task2", "Description"), taskManager.getTaskById(0));
    }

    @Test
    public void shouldUpdateEpic() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.updateEpic(new Epic("Epic2", "Description"), 0);
        assertEquals(new Epic("Epic2", "Description"), taskManager.getEpicById(0));
    }

    @Test
    public void shouldUpdateSubTask() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.createSubTask(new SubTask("SubTask1", "Description", 0));
        taskManager.updateSubTask(new SubTask("SubTask2", "Description", 0), 1,
                Status.NEW);

        assertEquals(new SubTask("SubTask2", "Description", 0),
                taskManager.getSubTaskById(1));
    }

    @Test
    public void shouldRemoveTaskById() {
        taskManager.createTask(new Task("Task1", "Description"));
        assertEquals(1, taskManager.getTaskMap().size());
        taskManager.removeTaskById(0);
        assertEquals(0, taskManager.getTaskMap().size());
    }

    @Test
    public void shouldRemoveEpicById() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        assertEquals(1, taskManager.getEpicMap().size());
        taskManager.removeEpicById(0);
        assertEquals(0, taskManager.getEpicMap().size());
    }

    @Test
    public void shouldRemoveSubTaskById() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.createSubTask(new SubTask("SubTask1", "Description", 0));
        assertEquals(1, taskManager.getSubTaskMap().size());
        assertEquals(1, taskManager.getEpicById(0).getSubTaskIdList().size());
        taskManager.removeSubTaskById(1);
        assertEquals(0, taskManager.getSubTaskMap().size());
        assertEquals(0, taskManager.getEpicById(0).getSubTaskIdList().size());
    }

    @Test
    public void shouldReturnSubTasksListOfEpic() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        assertNull(taskManager.getSubTasksOfEpic(0));

        taskManager.createSubTask(new SubTask("SubTask1", "Description", 0));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", 0));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", 0));

        assertEquals(3, taskManager.getSubTasksOfEpic(0).size());

    }

    @Test
    public void shouldCheckAndSetEpicStatus() {
        taskManager.createEpic(new Epic("Epic1", "Description"));
        taskManager.createSubTask(new SubTask("SubTask1", "Description", 0));
        taskManager.createSubTask(new SubTask("SubTask2", "Description", 0));
        taskManager.createSubTask(new SubTask("SubTask3", "Description", 0));

        assertEquals(Status.NEW, taskManager.getEpicById(0).getStatus());

        taskManager.getSubTaskById(1).setStatus(Status.DONE);
        taskManager.getSubTaskById(2).setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(new Epic("Epic1", "Description"), 0);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(0).getStatus());
    }

}