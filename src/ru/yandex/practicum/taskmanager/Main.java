package yandex.practicum.taskmanager;

import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.server.HttpTaskServer;
import yandex.practicum.taskmanager.manager.server.KVServer;
import yandex.practicum.taskmanager.manager.taskmanager.HttpTaskManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {

        KVServer cloudServer = new KVServer();
        cloudServer.start();

        HttpTaskManager manager = Managers.getHttpTaskManager("http://localhost:8078");

        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();

        LocalDateTime time1 = LocalDateTime.of(2022, 7, 7, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2022, 7, 8, 12, 0);
        LocalDateTime time3 = LocalDateTime.of(2022, 7, 9, 14, 0);
        Duration duration = Duration.ofMinutes(30);


        //Заведите несколько разных задач, эпиков и подзадач.
        manager.createTask(new Task(0, "Task1", "Description", time1, duration));

        manager.createEpic(new Epic(1, "Epic1", "Description"));
        manager.createSubTask(new SubTask(2, "SubTask1", "Description",
                time2, duration, 1));
        manager.createSubTask(new SubTask(3, "Subtask2", "Description",
                time3, duration, 1));

        //Запросите некоторые из них, чтобы заполнилась история просмотра.
        manager.getTaskById(0);
        manager.getEpicById(1);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);


        System.out.println(manager.getHistoryManager().getHistory());
        //Создайте новый FileBackedTasksManager менеджер из этого же файла.

        HttpTaskManager manager1 = Managers.getHttpTaskManager("http://localhost:8078");
        /*
        Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи,
        которые были в старом, есть в новом менеджере.
         */
        System.out.println("Данные восстановлены.");
        System.out.println(manager1.getHistoryManager().getHistory());

    }
}
