package ru.yandex.practicum.taskmanager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        manager.createTask(new Task("Прочитать книгу", "Core Java"));
        manager.createTask(new Task("Посмотреть фильм", "The Batman"));


        manager.createEpic(new Epic("Переезд", "В другую страну"));
        manager.createSubTask(new SubTask("Информация", "Зайти на сайт страны", 2));
        manager.createSubTask(new SubTask("Квартира", "Найти квартиру для съема", 2));

        manager.createEpic(new Epic("Закупка", "Купить продукты на завтра"));
        manager.createSubTask(new SubTask("Список", "Написать список товаров", 5));
        
    }
}
