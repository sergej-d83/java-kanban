package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.taskmanager.manager.Managers;
import ru.yandex.practicum.taskmanager.manager.taskmanager.TaskManager;
import ru.yandex.practicum.taskmanager.task.Epic;
import ru.yandex.practicum.taskmanager.task.SubTask;
import ru.yandex.practicum.taskmanager.task.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        //Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        manager.createTask(new Task("Прочитать книгу", "Core Java"));
        manager.createTask(new Task("Посмотреть фильм", "The Batman"));


        manager.createEpic(new Epic("Переезд", "В другую страну"));
        manager.createSubTask(new SubTask("Информация", "Зайти на сайт страны", 2));
        manager.createSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2));
        manager.createSubTask(new SubTask("Мебель", "Заказать мебель", 2));

        manager.createEpic(new Epic("Закупка", "Купить продукты на завтра"));

        /* Запросите созданные задачи несколько раз в разном порядке.
           После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
        */

        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getEpicById(6);
        manager.getEpicById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(3);

        System.out.println(manager.getHistoryManager().getHistory());

        manager.getTaskById(0);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getEpicById(6);
        manager.getEpicById(2);
        manager.getEpicById(6);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);
        manager.getSubTaskById(5);
        manager.getSubTaskById(4);

        System.out.println(manager.getHistoryManager().getHistory());

        //Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        manager.removeTaskById(0);
        System.out.println(manager.getHistoryManager().getHistory());

        //Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        manager.removeEpicById(2);
        System.out.println(manager.getHistoryManager().getHistory());
    }
}
