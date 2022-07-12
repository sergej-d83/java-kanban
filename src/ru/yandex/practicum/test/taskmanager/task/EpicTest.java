package yandex.practicum.test.taskmanager.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yandex.practicum.taskmanager.manager.taskmanager.InMemoryTaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.Status;
import yandex.practicum.taskmanager.task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager manager;
    public LocalDateTime time1 = LocalDateTime.of(2022,7,7,10,0);
    public LocalDateTime time2 = LocalDateTime.of(2022,7,8,12,0);
    public Duration duration = Duration.ofMinutes(30);

    @BeforeEach
    public void createManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void epicShouldHaveStatusNewWithoutSubTasks() {
        manager.createEpic(new Epic(manager.generateId(),"Epic1", "Description"));
        Epic epic = manager.getEpicById(0);
        epic.setStatus(manager.checkStatus(epic.getId()));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicShouldHaveStatusNewAllSubTasksNew() {
        manager.createEpic(new Epic(manager.generateId(), "Epic1", "Description"));
        Epic epic = manager.getEpicById(0);
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask1", "Description",
                time1, duration, epic.getId()));
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask2", "Description",
                time2, duration, epic.getId()));
        epic.setStatus(manager.checkStatus(epic.getId()));
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicShouldHaveStatusDoneSubTasksDone() {
        manager.createEpic(new Epic(manager.generateId(), "Epic1", "Description"));
        Epic epic = manager.getEpicById(0);
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask1", "Description",
                time1, duration, epic.getId()));
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask2", "Description",
                time2, duration, epic.getId()));
        manager.getSubTaskById(1).setStatus(Status.DONE);
        manager.getSubTaskById(2).setStatus(Status.DONE);

        epic.setStatus(manager.checkStatus(epic.getId()));
        assertEquals(Status.DONE, epic.getStatus());
    }
    @Test
    public void epicShouldHaveStatusInProgressSubTasksNewAndDone() {
        manager.createEpic(new Epic(manager.generateId(), "Epic1", "Description"));
        Epic epic = manager.getEpicById(0);
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask1", "Description",
                time1, duration, epic.getId()));
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask2", "Description",
                time2, duration, epic.getId()));
        manager.getSubTaskById(2).setStatus(Status.DONE);

        epic.setStatus(manager.checkStatus(epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void epicShouldHaveStatusInProgressSubTasksInProgress() {
        manager.createEpic(new Epic(manager.generateId(), "Epic1", "Description"));
        Epic epic = manager.getEpicById(0);
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask1", "Description",
                time1, duration, epic.getId()));
        manager.createSubTask(new SubTask(manager.generateId(), "Subtask2", "Description",
                time2, duration, epic.getId()));
        manager.getSubTaskById(1).setStatus(Status.IN_PROGRESS);
        manager.getSubTaskById(2).setStatus(Status.IN_PROGRESS);

        epic.setStatus(manager.checkStatus(epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}