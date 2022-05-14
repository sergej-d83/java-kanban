package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.manager.InMemoryTaskManager;
import ru.yandex.practicum.taskmanager.task.Epic;
import ru.yandex.practicum.taskmanager.task.Status;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager manager = new InMemoryTaskManager();

        //Создайте 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей.
        manager.createTask(new Task("Прочитать книгу", "Core Java"));
        manager.createTask(new Task("Посмотреть фильм", "The Batman"));


        manager.createEpic(new Epic("Переезд", "В другую страну"));
        manager.createSubTask(new SubTask("Информация", "Зайти на сайт страны", 2));
        manager.createSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2));

        manager.createEpic(new Epic("Закупка", "Купить продукты на завтра"));
        manager.createSubTask(new SubTask("Список", "Написать список товаров", 5));

        //Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
        System.out.println();
        System.out.println(manager.getAllTasks());
        System.out.println();
        System.out.println(manager.getAllEpics());
        System.out.println();
        System.out.println(manager.getAllSubTasks());

        //Измените статусы созданных объектов, распечатайте.
        //Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        manager.updateTask(new Task("Прочитать книгу", "Core Java"), 0, Status.IN_PROGRESS);
        manager.updateTask(new Task("Посмотреть фильм", "The Batman"), 1, Status.DONE);

        manager.updateEpic(new Epic("Переезд", "В другую страну"), 2);
        manager.updateSubTask(new SubTask("Информация", "Зайти на сайт страны", 2),
                3, Status.IN_PROGRESS);
        manager.updateSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2),
                4, Status.DONE);

        manager.updateEpic(new Epic("Закупка", "Купить продукты на завтра"), 5);
        manager.updateSubTask(new SubTask("Список", "Написать список товаров", 5),
                6, Status.DONE);

        System.out.println();
        System.out.println(manager.getAllTasks());
        System.out.println();
        System.out.println(manager.getAllEpics());
        System.out.println();
        System.out.println(manager.getAllSubTasks());

        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.removeTaskById(0);
        manager.removeEpicById(2);

        System.out.println();
        System.out.println(manager.getAllTasks());
        System.out.println();
        System.out.println(manager.getAllEpics());
        System.out.println();
        System.out.println(manager.getAllSubTasks());
    }
}
