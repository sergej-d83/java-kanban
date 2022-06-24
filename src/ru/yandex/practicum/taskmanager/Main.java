package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.manager.Managers;
import ru.yandex.practicum.taskmanager.manager.taskmanager.FileBackedTaskManager;
import ru.yandex.practicum.taskmanager.task.Epic;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        FileBackedTaskManager manager = Managers.getFileBackedManager(new File("tasks.csv"));

        //Заведите несколько разных задач, эпиков и подзадач.
        manager.createTask(new Task("Прочитать книгу", "Core Java"));
        manager.createTask(new Task("Посмотреть фильм", "The Batman"));

        manager.createEpic(new Epic("Переезд", "В другую страну"));
        manager.createSubTask(new SubTask("Информация", "Зайти на сайт страны", 2));
        manager.createSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2));
        manager.createSubTask(new SubTask("Мебель", "Заказать мебель", 2));

        manager.createEpic(new Epic("Закупка", "Купить продукты на завтра"));

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
        System.out.println("Данные востановленны из файла.");
        System.out.println(fileBackedTaskManager.getHistoryManager().getHistory());

        fileBackedTaskManager.createTask(new Task("Task 1", "Описание"));
        fileBackedTaskManager.createEpic(new Epic("Epic1", "Описание"));
        fileBackedTaskManager.createSubTask(new SubTask("SubTask1", "Описание", 8));
        fileBackedTaskManager.getTaskById(9);
        fileBackedTaskManager.getEpicById(8);
        fileBackedTaskManager.getSubTaskById(9);
        System.out.println(fileBackedTaskManager.getHistoryManager().getHistory());
    }
}
