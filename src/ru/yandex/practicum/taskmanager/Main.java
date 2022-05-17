package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.manager.Managers;
import ru.yandex.practicum.taskmanager.manager.taskmanager.TaskManager;
import ru.yandex.practicum.taskmanager.task.Epic;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        //Создайте несколько задач разного типа.
        manager.createTask(new Task("Прочитать книгу", "Core Java"));
        manager.createTask(new Task("Посмотреть фильм", "The Batman"));


        manager.createEpic(new Epic("Переезд", "В другую страну"));
        manager.createSubTask(new SubTask("Информация", "Зайти на сайт страны", 2));
        manager.createSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2));

        manager.createEpic(new Epic("Закупка", "Купить продукты на завтра"));
        manager.createSubTask(new SubTask("Список", "Написать список товаров", 5));


        /* Вызовите разные методы интерфейса TaskManager и напечатайте историю просмотров после каждого вызова.
           Если код рабочий, то история просмотров задач будет отображаться корректно
        */

        manager.getTaskById(0);
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getEpicById(2);
        System.out.println(manager.getHistoryManager().getHistory());
        manager.getSubTaskById(3);
        System.out.println(manager.getHistoryManager().getHistory());

    }
}
