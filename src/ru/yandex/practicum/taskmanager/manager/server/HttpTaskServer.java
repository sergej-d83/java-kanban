package yandex.practicum.taskmanager.manager.server;

import com.sun.net.httpserver.HttpServer;
import yandex.practicum.taskmanager.manager.server.handler.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int PORT = 8080;
    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/tasks/task", new TaskHandler());
        server.createContext("/tasks/epic", new TaskHandler());
        server.createContext("/tasks/subtask", new TaskHandler());
        server.createContext("/tasks/history", new TaskHandler());
        server.start();
        System.out.println("Server started on port " + PORT);

    }
}
