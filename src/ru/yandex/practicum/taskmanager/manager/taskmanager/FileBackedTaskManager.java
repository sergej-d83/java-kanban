package ru.yandex.practicum.taskmanager.manager.taskmanager;

import ru.yandex.practicum.taskmanager.exceptions.ManagerSaveException;
import ru.yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import ru.yandex.practicum.taskmanager.task.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private final String TASK_HEADER = "ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC";
    private final String HISTORY_HEADER = "HISTORY";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        //Считываем данные из файла.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            List<String> linesFromFile = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.equals(manager.TASK_HEADER) || line.equals(manager.HISTORY_HEADER) || line.isEmpty()) {
                    continue;
                }
                linesFromFile.add(line);
            }

            //Восстанавливаем задачи.
            for (int i = 0; i < linesFromFile.size() - 1; i++) {

                Task task = manager.taskFromString(linesFromFile.get(i));
                TaskType type = Objects.requireNonNull(task).getType();

                if (type == TaskType.TASK) {
                    taskMap.put(task.getId(), task);
                } else if (type == TaskType.EPIC) {
                    epicMap.put(task.getId(), (Epic) task);
                } else if (type == TaskType.SUBTASK) {
                    SubTask subTask = (SubTask) task;

                    int epicId = subTask.getEpicId();
                    subTaskMap.put(subTask.getId(), subTask);
                    epicMap.get(epicId).setSubTaskIdList(subTask.getId());
                } else {
                    System.out.println("Неизвестный тип задачи..");
                }
            }

            //Восстанавливаем историю.
            List<Integer> historyIds = historyFromString(linesFromFile.get(linesFromFile.size() - 1));

            for (Integer id : historyIds) {
                manager.getTaskById(id);
                manager.getEpicById(id);
                manager.getSubTaskById(id);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу считать данные из файла...: " + file.getName(), e);
        }
        return manager;
    }

    public void save() {

        //Пишем все созданные задачи в один список.
        List<String> tasks = new ArrayList<>();

        for (Task task : taskMap.values()) {
            tasks.add(taskToString(task));
        }
        for (Task epic : epicMap.values()) {
            tasks.add(taskToString(epic));
        }
        for (Task subTask : subTaskMap.values()) {
            tasks.add(taskToString(subTask));
        }

        //Пишем список и историю в файл.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(TASK_HEADER);
            writer.newLine();

            for (String line : tasks) {
                writer.write(line);
                writer.newLine();
            }
            writer.newLine();
            writer.write(HISTORY_HEADER);
            writer.newLine();
            writer.write(historyToString(getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу записать данные в файл: " + file.getName(), e);
        }

    }

    private String taskToString(Task task) {
        StringBuilder line = new StringBuilder(String.format("%d,%S,%s,%S,%s", task.getId(), task.getType(),
                                                                                     task.getTaskName(), task.getStatus(),
                                                                                     task.getTaskDescription()));

        if (task instanceof SubTask) {
            line.append(String.format(",%d", ((SubTask) task).getEpicId()));
        }
        return line.toString();
    }

    private Task taskFromString(String value) {
        String[] elements = value.split(",");

        switch (elements[1]) {
            case "TASK":
                return new Task(
                        Integer.parseInt(elements[0]),
                        TaskType.valueOf(elements[1]),
                        elements[2],
                        Status.valueOf(elements[3]),
                        elements[4]);
            case "EPIC":
                return new Epic(
                        Integer.parseInt(elements[0]),
                        TaskType.valueOf(elements[1]),
                        elements[2],
                        Status.valueOf(elements[3]),
                        elements[4]);
            case "SUBTASK":
                return new SubTask(
                        Integer.parseInt(elements[0]),
                        TaskType.valueOf(elements[1]),
                        elements[2],
                        Status.valueOf(elements[3]),
                        elements[4], Integer.parseInt(elements[5]));
            default:
                System.out.println("Неправильный тип задачи.");
                return null;
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder string = new StringBuilder();
        for (Task task : manager.getHistory()) {
            string.append(task.getId());
            string.append(",");
        }
        return string.toString();
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String id : value.split(",")) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task, int id, Status status) {
        super.updateTask(task, id, status);
        save();
    }

    @Override
    public void updateEpic(Epic epic, int id) {
        super.updateEpic(epic, id);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, int id, Status status) {
        super.updateSubTask(subTask, id, status);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(int epicId) {
        return super.getSubTasksOfEpic(epicId);
    }
}
