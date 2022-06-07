package ru.yandex.practicum.taskmanager.manager.historymanager;

public class Node<Task> {
    private Task data;
    private Node<Task> previous;
    private Node<Task> next;

    Node(Node<Task> previous, Task data, Node<Task> next) {
        this.previous = previous;
        this.data = data;
        this.next = next;
    }

    //GETTER
    public Task getData() {
        return data;
    }

    public Node<Task> getPrevious() {
        return previous;
    }

    public Node<Task> getNext() {
        return next;
    }

    //SETTER
    public void setPrevious(Node<Task> previous) {
        this.previous = previous;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }
}
