package yandex.practicum.taskmanager.manager.taskmanager;

import yandex.practicum.taskmanager.exception.ManagerSaveException;
import yandex.practicum.taskmanager.task.TaskType;
import yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;
    private static final String TASK_HEADER = "ID,TYPE,NAME,DESCRIPTION,STATUS,START,DURATION,EPIC";
    private static final String HISTORY_HEADER = "HISTORY";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager() {

    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        //Считываем данные из файла.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            List<String> linesFromFile = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.equals(TASK_HEADER) || line.equals(HISTORY_HEADER)) {
                    continue;
                }
                linesFromFile.add(line);
            }

            //Восстанавливаем задачи.
            for (String s : linesFromFile) {

                Task task = manager.taskFromString(s);

                if (task == null) {
                    continue;
                }

                TaskType type = Objects.requireNonNull(task).getType();

                if (task.getType() == TaskType.TASK) {
                    manager.setTaskMap(task.getId(), task);
                } else if (type == TaskType.EPIC) {
                    manager.setEpicMap(task.getId(), (Epic) task);
                } else if (type == TaskType.SUBTASK) {
                    SubTask subTask = (SubTask) task;

                    int epicId = subTask.getEpicId();
                    manager.setSubTaskMap(task.getId(), (SubTask) task);
                    manager.getEpicMap().get(epicId).setSubTaskIdList(subTask.getId());
                } else {
                    System.out.println("Неизвестный тип задачи..");
                }
            }

            //Восстанавливаем ID
            manager.setId(manager.getTaskMap().size() +
                    manager.getEpicMap().size() +
                    manager.getSubTaskMap().size());

            //Восстанавливаем историю.
            if (linesFromFile.size() > 0) {
                List<Integer> historyIds = historyFromString(linesFromFile.get(linesFromFile.size() - 1));

                for (Integer id : historyIds) {
                    manager.getTaskById(id);
                    manager.getEpicById(id);
                    manager.getSubTaskById(id);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу считать данные из файла...: " + file.getName());
        }
        return manager;
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
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
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
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    protected void save() {

        //Пишем все созданные задачи в один список.
        List<String> tasks = new ArrayList<>();

        for (Task task : getTaskMap().values()) {
            tasks.add(taskToString(task));
        }
        for (Task epic : getEpicMap().values()) {
            tasks.add(taskToString(epic));
        }
        for (Task subTask : getSubTaskMap().values()) {
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
            writer.write(HISTORY_HEADER);
            writer.newLine();
            writer.write(historyToString(getHistoryManager()));

        } catch (IOException e) {
            throw new ManagerSaveException("Не могу записать данные в файл: " + file.getName());
        }

    }

    private String taskToString(Task task) {
        StringBuilder line = new StringBuilder(String.format("%d,%S,%s,%s,%S,",
                task.getId(), task.getType(), task.getTaskName(), task.getTaskDescription(),
                task.getStatus()));

        if (task instanceof Epic) {
            if (task.getStartTime() != null && task.getDuration() != null) {
                line.append(String.format("%s,%s",
                        task.getStartTime().format(Task.formatter),
                        task.getDuration().toMinutes()));
            }
            return line.toString();
        } else if (task instanceof SubTask) {
            line.append(String.format("%s,%s,%d",
                    task.getStartTime().format(Task.formatter),
                    task.getDuration().toMinutes(), ((SubTask) task).getEpicId()));
            return line.toString();
        } else {
            line.append(String.format("%s,%s",
                    task.getStartTime().format(Task.formatter),
                    task.getDuration().toMinutes()));
            return line.toString();
        }
    }

    private Task taskFromString(String value) {
        String[] elements = value.split(",");

        if (elements[1].chars().allMatch(Character::isDigit)) {
            return null;
        }

        switch (elements[1]) {
            case "TASK":
                return new Task(Integer.parseInt(elements[0]), elements[2], elements[3],
                        LocalDateTime.parse(elements[5], Task.formatter),
                        Duration.ofMinutes(Long.parseLong(elements[6])));
            case "EPIC":
                Epic epic = new Epic(Integer.parseInt(elements[0]), elements[2], elements[3]);
                if (elements.length > 5 && elements[5] != null && elements[6] != null) {
                    epic.setStartTime(LocalDateTime.parse(elements[5], Task.formatter));
                    epic.setDuration(Duration.ofMinutes(Long.parseLong(elements[6])));
                }
                return epic;
            case "SUBTASK":
                return new SubTask(Integer.parseInt(elements[0]), elements[2], elements[3],
                        LocalDateTime.parse(elements[5], Task.formatter),
                        Duration.ofMinutes(Long.parseLong(elements[6])),
                        Integer.parseInt(elements[7]));
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
        String[] values = value.split(",");
        if (values[1].chars().allMatch(Character::isDigit)) {
            for (int i = 0; i < values.length - 1; i++) {
                history.add(Integer.parseInt(values[i]));
            }
        }

        return history;
    }
}
