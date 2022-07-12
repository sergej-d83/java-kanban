package yandex.practicum.taskmanager.manager.taskmanager;

import yandex.practicum.taskmanager.task.Status;
import yandex.practicum.taskmanager.manager.Managers;
import yandex.practicum.taskmanager.manager.historymanager.HistoryManager;
import yandex.practicum.taskmanager.task.Epic;
import yandex.practicum.taskmanager.task.SubTask;
import yandex.practicum.taskmanager.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id;
    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, SubTask> subTaskMap;
    private final HistoryManager historyManager;

    private final Set<Task> prioritizedTasks = new TreeSet<>((time1, time2) -> {
        if (time1.getStartTime() == null && time2.getStartTime() == null) {
            return 1;
        } else if (time1.getStartTime().isBefore(time2.getStartTime())) {
            return -1;
        } else if (time1.getStartTime().isAfter(time2.getStartTime())) {
            return 1;
        } else if (time1.getStartTime() == null) {
            return 1;
        } else if (time2.getStartTime() == null) {
            return -1;
        } else {
            return 0;
        }
    });

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        taskMap = new HashMap<>();
        epicMap = new HashMap<>();
        subTaskMap = new HashMap<>();
        id = 0;
    }

    @Override
    public int generateId() {
        return id++;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    @Override
    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    @Override
    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    @Override
    public void setTaskMap(int id, Task task) {
        this.taskMap.put(id, task);
    }

    @Override
    public void setEpicMap(int id, Epic epic) {
        this.epicMap.put(id, epic);
    }

    @Override
    public void setSubTaskMap(int id, SubTask subTask) {
        this.subTaskMap.put(id, subTask);
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // 1. Получение списка всех задач
    @Override
    public Map<Integer, Task> getAllTasks() {
        return taskMap;
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epicMap;
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasks() {
        return subTaskMap;
    }

    // 2. Удаление всех задач
    @Override
    public void clearAllTasks() {
        if (!taskMap.isEmpty()) {
            taskMap.clear();
        }
    }

    @Override
    public void clearAllEpics() {
        if (!epicMap.isEmpty()) {
            epicMap.clear();
        }
    }

    @Override
    public void clearAllSubTasks() {
        if (!subTaskMap.isEmpty()) {
            subTaskMap.clear();
        }
    }

    // 3. Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return null;
        }
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return null;
        }
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (!subTaskMap.containsKey(id)) {
            return null;
        }
        historyManager.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    // 4. Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void createTask(Task task) {
        if (task != null) {
            validateTime(task);

            taskMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (subTask != null) {
            validateTime(subTask);

            if (!epicMap.containsKey(subTask.getEpicId())) {
                throw new IllegalArgumentException("Не правильный номер Эпика.");
            }
                int epicId = subTask.getEpicId();

            subTaskMap.put(subTask.getId(), subTask);
            epicMap.get(epicId).setSubTaskIdList(subTask.getId());
            calculateEpicTimeAndDuration(epicMap.get(epicId));
            prioritizedTasks.add(subTask);
        }
    }

    // 5. Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра
    @Override
    public void updateTask(Task task) {
        if (taskMap.containsKey(task.getId())) {
            validateTime(task);
            taskMap.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicMap.containsKey(epic.getId())) {
            if (!epicMap.get(epic.getId()).getSubTaskIdList().isEmpty()) {
                List<Integer> subTasks = epicMap.get(id).getSubTaskIdList();

                for (Integer subId : subTasks) {
                    epic.setSubTaskIdList(subId);
                }
            }
            epic.setStatus(checkStatus(epic.getId()));
            epicMap.put(epic.getId(), epic);
            calculateEpicTimeAndDuration(epic);
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        if (subTaskMap.containsKey(subTask.getId())) {
            validateTime(subTask);
            int epicId = subTask.getEpicId();
            subTaskMap.put(subTask.getId(), subTask);
            epicMap.get(epicId).setStatus(checkStatus(epicId));
            calculateEpicTimeAndDuration(epicMap.get(epicId));
            prioritizedTasks.add(subTask);
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    // 6. Удаление по идентификатору
    @Override
    public void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            prioritizedTasks.remove(taskMap.get(id));
            taskMap.remove(id);
            historyManager.remove(id);
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {

            if (!epicMap.get(id).getSubTaskIdList().isEmpty()) {
                List<Integer> subTasks = epicMap.get(id).getSubTaskIdList();

                for (Integer subTaskId : subTasks) {
                    subTaskMap.remove(subTaskId);
                    historyManager.remove(subTaskId);
                }
            }
            epicMap.remove(id);
            historyManager.remove(id);
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {

            int epicId = subTaskMap.get(id).getEpicId();

            epicMap.get(epicId).getSubTaskIdList().remove(Integer.valueOf(id));
            prioritizedTasks.remove(subTaskMap.get(id));
            subTaskMap.remove(id);
            calculateEpicTimeAndDuration(epicMap.get(epicId));
            historyManager.remove(id);
            epicMap.get(epicId).setStatus(checkStatus(id));
        } else {
            throw new IllegalArgumentException("Задачи с таким номером нет в списке!");
        }
    }

    // 7. Получение списка всех подзадач определённого эпика
    @Override
    public List<SubTask> getSubTasksOfEpic(int epicId) {
        if (epicMap.get(epicId).getSubTaskIdList().isEmpty()) {
            return null;
        }

        List<SubTask> subTasks = new ArrayList<>();
        List<Integer> subTaskIds = epicMap.get(epicId).getSubTaskIdList();

        for (Integer subTask : subTaskIds) {
            subTasks.add(subTaskMap.get(subTask));
        }
        return subTasks;
    }

    @Override
    public void calculateEpicTimeAndDuration(Epic epic) {
        if (!epic.getSubTaskIdList().isEmpty()) {
            List<SubTask> subTasksList = new ArrayList<>();

            for (Integer id : epic.getSubTaskIdList()) {
                subTasksList.add(subTaskMap.get(id));
            }

            LocalDateTime startTime = epic.getStartTime();
            LocalDateTime endTime = epic.getEndTime();
            Duration duration = epic.getDuration();

            for (SubTask task : subTasksList) {
                if (startTime == null || task.getStartTime().isBefore(startTime)) {
                    startTime = task.getStartTime();
                }
                if (endTime == null || task.getEndTime().isAfter(endTime)) {
                    endTime = task.getEndTime();
                }
                if (duration == null) {
                    duration = task.getDuration();
                } else {
                    duration = duration.plus(task.getDuration());
                }
            }

            epic.setDuration(duration);
            epic.setEndTime(endTime);
            epic.setStartTime(startTime);
        }
    }

    @Override
    public Status checkStatus(int epicId) {
        if (!epicMap.containsKey(epicId)) {
            return null;
        } else if (epicMap.get(epicId).getSubTaskIdList().isEmpty()) {
            return Status.NEW;
        }

        Epic tempEpic = epicMap.get(epicId);
        List<Integer> subTaskIdList = tempEpic.getSubTaskIdList();

        int countNew = 0;
        int countDone = 0;

        for (Integer subTaskId : subTaskIdList) {
            if (subTaskMap.get(subTaskId).getStatus().equals(Status.NEW)) {
                countNew++;
            } else if (subTaskMap.get(subTaskId).getStatus().equals(Status.DONE)) {
                countDone++;
            }
        }

        if (countNew == subTaskIdList.size()) {
            return Status.NEW;
        } else if (countDone == subTaskIdList.size()) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    public <T extends Task> void validateTime(T task) {

        if (task.getStartTime() != null || task.getDuration() != null) {
            LocalDateTime startTime = task.getStartTime();
            Set<Task> taskList = getPrioritizedTasks();

            for (Task t : taskList) {
                if (!startTime.isBefore(t.getStartTime()) && !startTime.isAfter(t.getEndTime())) {
                    throw new IllegalArgumentException("Задача пересекается по времени с другой задачей. "
                            + t.getStartTime().format(Task.formatter)
                            + " - " + t.getEndTime().format(Task.formatter));
                }
            }
        }
    }
}
