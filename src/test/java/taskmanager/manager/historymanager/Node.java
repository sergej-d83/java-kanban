package taskmanager.manager.historymanager;

public class Node<T> {
    private final T data;
    private Node<T> previous;
    private Node<T> next;

    Node(Node<T> previous, T data, Node<T> next) {
        this.previous = previous;
        this.data = data;
        this.next = next;
    }

    //GETTER
    public T getData() {
        return data;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public Node<T> getNext() {
        return next;
    }

    //SETTER
    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
