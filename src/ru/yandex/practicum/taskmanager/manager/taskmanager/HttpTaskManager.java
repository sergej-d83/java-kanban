package yandex.practicum.taskmanager.manager.taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import yandex.practicum.taskmanager.manager.client.KVTaskClient;
import yandex.practicum.taskmanager.manager.server.adapter.DurationAdapter;
import yandex.practicum.taskmanager.manager.server.adapter.LocalDateTimeAdapter;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;
import yandex.practicum.taskmanager.task.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient client;
    private final Gson gson;

    private final String TASK_KEY = "tasks";
    private final String EPIC_KEY = "epics";
    private final String SUBTASK_KEY = "subtasks";
    private final String HISTORY_KEY = "history";

    public HttpTaskManager(String uri) {
        this.client = new KVTaskClient(uri);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void save() {

        String tasks = gson.toJson(getTaskMap());
        String epics = gson.toJson(getEpicMap());
        String subtasks = gson.toJson(getSubTaskMap());
        String history = gson.toJson(getHistoryManager().getHistory());

        client.put(TASK_KEY, tasks);
        client.put(EPIC_KEY, epics);
        client.put(SUBTASK_KEY, subtasks);
        client.put(HISTORY_KEY, history);
    }

    public void load() {

        try {

            Map<Integer, Task> taskMap = gson.fromJson(client.load(TASK_KEY),
                    new TypeToken<HashMap<Integer, Task>>() {
                    }.getType());
            Map<Integer, Epic> epicMap = gson.fromJson(client.load(EPIC_KEY),
                    new TypeToken<HashMap<Integer, Epic>>() {
                    }.getType());
            Map<Integer, SubTask> subTaskMap = gson.fromJson(client.load(SUBTASK_KEY),
                    new TypeToken<HashMap<Integer, SubTask>>() {
                    }.getType());
            List<Task> history = gson.fromJson(client.load(HISTORY_KEY), new TypeToken<ArrayList<Task>>() {
            }.getType());

            if (taskMap != null) {
                for (Map.Entry<Integer, Task> task : taskMap.entrySet()) {
                    setTaskMap(task.getKey(), task.getValue());
                }
            }
            if (epicMap != null) {
                for (Map.Entry<Integer, Epic> epic : epicMap.entrySet()) {
                    setEpicMap(epic.getKey(), epic.getValue());
                }
            }
            if (subTaskMap != null) {
                for (Map.Entry<Integer, SubTask> subTask : subTaskMap.entrySet()) {
                    setSubTaskMap(subTask.getKey(), subTask.getValue());
                }
            }

            setId((taskMap != null ? taskMap.size() : 0)
                    + (epicMap != null ? epicMap.size() : 0)
                    + (subTaskMap != null ? subTaskMap.size() : 0));

            if (history != null) {
                for (Task t : history) {
                    if (t.getType() == TaskType.TASK) {
                        getHistoryManager().add(taskMap != null ? taskMap.get(t.getId()) : null);
                    } else if (t.getType() == TaskType.EPIC) {
                        getHistoryManager().add(epicMap != null ? epicMap.get(t.getId()) : null);
                    } else if (t.getType() == TaskType.SUBTASK) {
                        getHistoryManager().add(subTaskMap != null ? subTaskMap.get(t.getId()) : null);
                    }
                }
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }

}
