package ru.yandex.practicum.taskmanager.manager.historymanager;

import ru.yandex.practicum.taskmanager.task.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    final CustomLinkedList history;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    private static class CustomLinkedList {

        private final Map<Integer, Node<Task>> linkedHistory = new HashMap<>();
        private Node<Task> head;
        private Node<Task> tail;

        void linkLast(Task task) {

            if (linkedHistory.containsKey(task.getId())) {
                remove(task.getId());
            }

            final Node<Task> oldTail = this.tail;
            final Node<Task> newNode = new Node<>(oldTail, task, null);
            this.tail = newNode;

            if (oldTail == null) {
                this.head = newNode;
            } else {
                oldTail.setNext(newNode);
            }

            linkedHistory.put(task.getId(), newNode);
        }

        void remove(int id) {
            removeNode(linkedHistory.get(id));
            linkedHistory.remove(id);
        }

        void removeNode(Node<Task> node) {
            if (node != null) {
                Node<Task> previous = node.getPrevious();
                Node<Task> next = node.getNext();

                if (next != null) {
                    next.setPrevious(previous);
                } else {
                    this.tail = previous;
                }

                if (previous != null) {
                    previous.setNext(next);
                } else {
                    this.head = next;
                }
            }
        }

        List<Task> getTasks() {
            List<Task> nodeHistory = new ArrayList<>();
            if (this.head != null) {
                Node<Task> node = this.head;
                while (node != null) {
                    nodeHistory.add(node.getData());
                    node = node.getNext();
                }
            }
            return nodeHistory;
        }
    }
}

