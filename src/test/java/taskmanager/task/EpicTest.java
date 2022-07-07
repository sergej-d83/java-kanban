package taskmanager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.manager.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    InMemoryTaskManager manager;

    @BeforeEach
    public void createTaskManager() {
        manager = new InMemoryTaskManager();
        manager.createEpic(new Epic("Epic", "Description"));
    }

    @Test
    public void shouldHaveStatusNewNoSubtasks() {
        assertEquals(Status.NEW, manager.getEpicById(0).getStatus());
    }

    @Test
    public void shouldHaveStatusNewAllSubtasksNew() {
        manager.createSubTask(new SubTask("Subtask1", "Description1", 0));
        manager.createSubTask(new SubTask("Subtask2", "Description2", 0));
        assertEquals(Status.NEW, manager.getEpicById(0).getStatus());
    }

    @Test
    public void shouldHaveStatusDoneAllSubtasksDone() {
        manager.createSubTask(new SubTask("Subtask1", "Description1", 0));
        manager.createSubTask(new SubTask("Subtask2", "Description2", 0));
        manager.getSubTaskById(1).setStatus(Status.DONE);
        manager.getSubTaskById(2).setStatus(Status.DONE);
        manager.getEpicById(0).setStatus(manager.checkStatus(0));
        assertEquals(Status.DONE, manager.getEpicById(0).getStatus());
    }

    @Test
    public void shouldHaveStatusInProgressSubtasksNewAndDone() {
        manager.createSubTask(new SubTask("Subtask1", "Description1", 0));
        manager.createSubTask(new SubTask("Subtask2", "Description2", 0));
        manager.getSubTaskById(2).setStatus(Status.DONE);
        manager.getEpicById(0).setStatus(manager.checkStatus(0));
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0).getStatus());
    }

    @Test
    public void shouldHaveStatusInProgressSubtasksInProgress() {
        manager.createSubTask(new SubTask("Subtask1", "Description1", 0));
        manager.createSubTask(new SubTask("Subtask2", "Description2", 0));
        manager.getSubTaskById(1).setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(2).setStatus(Status.IN_PROGRESS);
        manager.getEpicById(0).setStatus(manager.checkStatus(0));
        assertEquals(Status.IN_PROGRESS, manager.getEpicById(0).getStatus());
    }
}