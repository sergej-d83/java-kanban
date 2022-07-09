package taskmanager;

import taskmanager.manager.Managers;
import taskmanager.manager.taskmanager.FileBackedTaskManager;
import taskmanager.task.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager manager = Managers.getFileBackedManager(new File("tasks.csv"));

        LocalDateTime start1 = LocalDateTime.of(2022, 7, 7, 16, 30);
        LocalDateTime start2 = LocalDateTime.of(2022, 7, 7, 17, 30);
        LocalDateTime start3 = LocalDateTime.of(2022, 7, 7, 18, 30);
        LocalDateTime start4 = LocalDateTime.of(2022, 7, 7, 19, 30);
        LocalDateTime start5 = LocalDateTime.of(2022, 7, 7, 20, 30);

        Duration duration = Duration.ofMinutes(30);

        //Заведите несколько разных задач, эпиков и подзадач.
        manager.createTask(new Task(manager.generateId(),"Task1", "Description", start1, duration));
        manager.createTask(new Task(manager.generateId(),"Task2", "Description", start2, duration));
        manager.createEpic(new Epic(manager.generateId(),"Epic1", "Description"));
        manager.createSubTask(new SubTask(manager.generateId(),"SubTask1", "Description",
                start3, duration, 2));
        manager.createSubTask(new SubTask(manager.generateId(),"SubTask2", "Description",
                start4, duration, 2));
        manager.createSubTask(new SubTask(manager.generateId(),"SubTask1", "Description",
                start5, duration, 2));
        //Запросите некоторые из них, чтобы заполнилась история просмотра.
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getEpicById(6);
        manager.getEpicById(2);
        manager.getSubTaskById(4);
        manager.getSubTaskById(5);

        System.out.println("Данные сохранены в файл.");
        System.out.println(manager.getHistoryManager().getHistory());

        //Создайте новый FileBackedTasksManager менеджер из этого же файла.
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("tasks.csv"));

        /*
        Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи,
        которые были в старом, есть в новом менеджере.
         */
        System.out.println("Данные восстановились из файла.");
        System.out.println(fileBackedTaskManager.getHistoryManager().getHistory());


        manager.getPrioritizedTasks().forEach(System.out::println);
    }
}
