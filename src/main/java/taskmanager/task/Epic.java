package taskmanager.task;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Integer> subTaskIdList;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public Epic(int id, String taskName, String taskDescription)
    {
        super(id, taskName, taskDescription);
        this.subTaskIdList = new ArrayList<>();
        this.startTime = null;
        this.endTime = null;
        this.duration = null;
        this.status = Status.NEW;
        this.type = TaskType.EPIC;
    }

    public void setSubTaskIdList(int id) {
        subTaskIdList.add(id);
    }

    public List<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIdList=" + subTaskIdList +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", type=" + type +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIdList, epic.subTaskIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subTaskIdList);
    }
}
