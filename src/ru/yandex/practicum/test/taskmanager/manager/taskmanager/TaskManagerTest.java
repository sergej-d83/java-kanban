package taskmanager.manager.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yandex.practicum.taskmanager.manager.taskmanager.TaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.Status;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    public abstract T createTaskManager();

    public T manager;
    public LocalDateTime time1 = LocalDateTime.of(2022,7,7,10,0);
    public LocalDateTime time2 = LocalDateTime.of(2022,7,8,12,0);
    public LocalDateTime time3 = LocalDateTime.of(2022,7,9,14,0);
    public Duration duration = Duration.ofMinutes(30);

    @BeforeEach
    public void updateTaskManager() {
        manager = createTaskManager();
    }

    @Test
    public void shouldReturnTaskMap() {
        manager.createTask(new Task(manager.generateId(), "Task1", "Description",
                time1, duration));
        assertNotNull(manager.getTaskMap());
    }

    @Test
    public void shouldReturnEmptyTaskMap() {
        Map<Integer, Task> taskMap = new HashMap<>();
        assertEquals(taskMap, manager.getTaskMap());
    }

    @Test
    public void shouldReturnEpicMap() {
        manager.createEpic(new Epic(manager.generateId(), "Task1", "Description"));
        assertNotNull(manager.getTaskMap());
    }

    @Test
    public void shouldReturnEmptyEpicMap() {
        Map<Integer, Epic> epicMap = new HashMap<>();
        assertEquals(epicMap, manager.getEpicMap());
    }

    @Test
    public void shouldReturnSubTaskMap() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "Task1", "Description",
                time1, duration, 0));
        Map<Integer, SubTask> subTaskMap = new HashMap<>();
        subTaskMap.put(1, new SubTask(1, "Task1", "Description",
                time1, duration, 0));
        assertEquals(subTaskMap, manager.getSubTaskMap());
    }

    @Test
    public void shouldReturnEmptySubTaskMap() {
        Map<Integer, SubTask> subTaskMap = new HashMap<>();
        assertEquals(subTaskMap, manager.getSubTaskMap());
    }

    @Test
    public void shouldDeleteAllTasks() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        manager.createTask(new Task(1, "Task2", "Description", time2, duration));
        manager.createTask(new Task(2, "Task3", "Description", time3, duration));

        assertEquals(3, manager.getTaskMap().size());

        manager.clearAllTasks();

        assertEquals(0, manager.getTaskMap().size());
    }

    @Test
    public void shouldDeleteAllEpics() {
        manager.createEpic(new Epic(0, "Task1", "Description"));
        manager.createEpic(new Epic(1, "Task2", "Description"));
        manager.createEpic(new Epic(2, "Task3", "Description"));

        assertEquals(3, manager.getEpicMap().size());

        manager.clearAllEpics();

        assertEquals(0, manager.getEpicMap().size());
    }

    @Test
    public void shouldDeleteAllSubTasks() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        manager.createSubTask(new SubTask(2, "SubTask2", "Description",
                time2, duration, 0));

        assertEquals(2, manager.getSubTaskMap().size());

        manager.clearAllSubTasks();

        assertEquals(0, manager.getSubTaskMap().size());
    }

    @Test
    public void shouldReturnTaskById() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        assertNotNull(manager.getTaskById(0));
    }

    @Test
    public void shouldReturnNullByIncorrectTaskId() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void shouldReturnEpicById() {
        manager.createEpic(new Epic(0, "Task1", "Description"));
        assertNotNull(manager.getEpicById(0));
    }

    @Test
    public void shouldReturnNullByIncorrectEpicId() {
        manager.createEpic(new Epic(0, "Task1", "Description"));
        assertNull(manager.getEpicById(1));
    }

    @Test
    public void shouldReturnSubTaskById() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        assertNotNull(manager.getSubTaskById(1));
    }

    @Test
    public void shouldReturnNullByIncorrectSubTaskId() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "Task1", "Description", time1, duration, 0));
        assertNull(manager.getTaskById(2));
    }

    @Test
    public void shouldCreateNewTask() {
        assertEquals(0, manager.getTaskMap().size());
        manager.createTask(new Task(0, "Task1", "Description1", time1, duration));
        assertNotNull(manager.getTaskById(0));
        assertEquals(1, manager.getTaskMap().size());
    }

    @Test
    public void shouldCreateNewEpic() {
        assertEquals(0, manager.getEpicMap().size());
        manager.createEpic(new Epic(0, "Task1", "Description1"));
        assertNotNull(manager.getEpicById(0));
        assertEquals(1, manager.getEpicMap().size());
    }

    @Test
    public void shouldCreateNewSubTask() {
        assertEquals(0, manager.getSubTaskMap().size());
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "Task1", "Description1",
                time1, duration, 0));

        assertEquals(1, manager.getSubTaskMap().size());
        assertNotNull(manager.getSubTaskById(1));
    }

    @Test
    public void shouldUpdateTask() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        assertEquals(Status.NEW, manager.getTaskById(0).getStatus());

        Task task = new Task(0, "Task1", "Description", time2, duration);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskById(0).getStatus());
    }

    @Test
    public void shouldThrowExceptionUpdateTaskWithIncorrectId() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        assertEquals(Status.NEW, manager.getTaskById(0).getStatus());

        Task task = new Task(1, "Task1", "Description", time2, duration);
        task.setStatus(Status.DONE);

        assertThrows(IllegalArgumentException.class, () -> manager.updateTask(task));
        assertNotEquals(Status.DONE, manager.getTaskById(0).getStatus());
    }

    @Test
    public void shouldUpdateEpic() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertEquals("Epic1", manager.getEpicById(0).getTaskName());

        Epic epic = new Epic(0, "Epic2", "Description");
        manager.updateEpic(epic);
        assertEquals("Epic2", manager.getEpicById(0).getTaskName());
    }

    @Test
    public void shouldThrowExceptionUpdateEpicWithIncorrectId() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertEquals("Epic1", manager.getEpicById(0).getTaskName());

        Epic epic = new Epic(1, "Epic2", "Description");

        assertThrows(IllegalArgumentException.class, () -> manager.updateTask(epic));
        assertNotEquals("Epic2", manager.getEpicById(0).getTaskName());
    }

    @Test
    public void shouldUpdateSubTask() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        assertEquals(Status.NEW, manager.getSubTaskById(1).getStatus());

        SubTask subTask = new SubTask(1, "SubTask1", "Description",
                time2, duration, 0);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask);

        assertEquals(Status.IN_PROGRESS, manager.getSubTaskById(1).getStatus());
    }

    @Test
    public void shouldThrowExceptionUpdateSubTaskWithIncorrectId() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        assertEquals(Status.NEW, manager.getSubTaskById(1).getStatus());

        SubTask subTask = new SubTask(2, "SubTask1", "Description", time2, duration, 0);
        subTask.setStatus(Status.IN_PROGRESS);

        assertThrows(IllegalArgumentException.class, () -> manager.updateSubTask(subTask));
        assertEquals(Status.NEW, manager.getSubTaskById(1).getStatus());
    }

    @Test
    public void shouldRemoveTaskById() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
        assertEquals(1, manager.getTaskMap().size());
        manager.removeTaskById(0);
        assertEquals(0, manager.getTaskMap().size());
    }

    @Test
    public void shouldThrowExceptionRemoveTaskIncorrectId() {
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));
       assertThrows(IllegalArgumentException.class, () -> manager.removeTaskById(1));
    }

    @Test
    public void shouldRemoveEpicById() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertEquals(1, manager.getEpicMap().size());
        manager.removeEpicById(0);
        assertEquals(0, manager.getEpicMap().size());
    }

    @Test
    public void shouldThrowExceptionRemoveEpicIncorrectId() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertThrows(IllegalArgumentException.class, () -> manager.removeTaskById(1));
    }

    @Test
    public void shouldRemoveSubTaskById() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description", time2, duration, 0));
        assertEquals(1, manager.getSubTaskMap().size());
        assertEquals(1, manager.getEpicById(0).getSubTaskIdList().size());
        manager.removeSubTaskById(1);
        assertEquals(0, manager.getSubTaskMap().size());
        assertEquals(0, manager.getEpicById(0).getSubTaskIdList().size());
    }

    @Test
    public void shouldThrowExceptionRemoveSubTaskIncorrectId() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description", time1, duration, 0));
        assertThrows(IllegalArgumentException.class, () -> manager.removeSubTaskById(2));
    }

    @Test
    public void shouldReturnSubTasksListOfEpic() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertTrue(manager.getSubTasksOfEpic(0).isEmpty());

        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        manager.createSubTask(new SubTask(2, "SubTask2", "Description",
                time2, duration, 0));
        manager.createSubTask(new SubTask(3, "SubTask3", "Description",
                time3, duration, 0));

        assertEquals(3, manager.getSubTasksOfEpic(0).size());

    }

    @Test
    public void shouldCheckAndSetEpicStatus() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                time1, duration, 0));
        manager.createSubTask(new SubTask(2, "SubTask2", "Description",
                time2, duration, 0));
        manager.createSubTask(new SubTask(3, "SubTask3", "Description",
                time3, duration, 0));

        assertEquals(Status.NEW, manager.getEpicById(0).getStatus());

        manager.getSubTaskById(1).setStatus(Status.DONE);
        manager.getSubTaskById(2).setStatus(Status.IN_PROGRESS);
        manager.updateEpic(new Epic(0,"Epic1", "Description"));

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0).getStatus());
    }

    @Test
    public void shouldReturnPrioritizedListOfTasks() {
        manager.createTask(new Task(0, "Task1", "Description",
                LocalDateTime.of(2022, 7, 7, 13, 0), Duration.ofMinutes(30)));
        manager.createEpic(new Epic(1, "Epic1", "Description"));
        manager.createSubTask(new SubTask(2, "SubTask1", "Description",
                LocalDateTime.of(2022, 7, 7, 14, 30), Duration.ofMinutes(30), 1));
        manager.createSubTask(new SubTask(3, "SubTask2", "Description",
                LocalDateTime.of(2022, 7, 7, 15, 30), Duration.ofMinutes(30), 1));
        assertEquals(3, manager.getPrioritizedTasks().size());
    }

    @Test
    public void subTaskShouldHaveEpic() {
        assertThrows(IllegalArgumentException.class, () -> manager.createSubTask(new SubTask(0,
                "SubTask",
                "Description",
                time1, duration, 1)));
    }

    @Test
    public void shouldCalculateAndSetTimesForEpic() {
        manager.createEpic(new Epic(0, "Epic1", "Description"));
        assertNull(manager.getEpicById(0).getStartTime());
        assertNull(manager.getEpicById(0).getEndTime());
        assertNull(manager.getEpicById(0).getDuration());

        manager.createSubTask(new SubTask(1, "SubTask1", "Description",
                LocalDateTime.of(2022, 7, 7, 13, 30), Duration.ofMinutes(30), 0));
        manager.createSubTask(new SubTask(2, "SubTask2", "Description",
                LocalDateTime.of(2022, 7, 7, 14, 30), Duration.ofMinutes(30), 0));

        assertNotNull(manager.getEpicById(0).getStartTime());
        assertNotNull(manager.getEpicById(0).getEndTime());
        assertNotNull(manager.getEpicById(0).getDuration());
    }

    @Test
    public void shouldValidateTimeCreateTask() {
        manager.createTask(new Task(0, "Task1", "Description",
                LocalDateTime.of(2022,7,7,13,0), Duration.ofMinutes(30)));
        manager.createTask(new Task(1, "Task2",
                "Description", LocalDateTime.of(2022,7,7,12,15),
                Duration.ofMinutes(30)));
        assertThrows(IllegalArgumentException.class, () -> manager.createTask(new Task(2, "Task3",
                "Description", LocalDateTime.of(2022,7,7,13,15),
                Duration.ofMinutes(30))));
        manager.createTask(new Task(3, "Task4", "Description",
                LocalDateTime.of(2022,7,7,13,35), Duration.ofMinutes(30)));
    }
}