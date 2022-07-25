package yandex.practicum.taskmanager.manager.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String apiToken;
    private final HttpClient client;
    private final String serverAddress;

    public KVTaskClient(String serverAddress) {
        this.serverAddress = serverAddress;
        this.client = HttpClient.newHttpClient();
        this.apiToken = registerClient();
        if (apiToken != null) {
            System.out.println("KVClient зарегистрирован");
        }
    }

    private String registerClient() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverAddress + "/register/"))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(serverAddress + "/save/" + key + "?API_TOKEN=" + apiToken))
                .header("Content-Type", "application/json")
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverAddress + "/load/" + key + "?API_TOKEN=" + apiToken))
                .header("Accept", "application/json")
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
