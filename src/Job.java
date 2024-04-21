import java.util.List;
import java.util.ArrayList;
public class Job {
    private String jobId;
    private List<Task> tasks;
    private int startTime;
    private int duration;
    private int deadline;

    public Job(String jobId, int startTime, int duration) {
        this.jobId = jobId;
        this.startTime = startTime;
        this.duration = duration;
        this.deadline = startTime + duration;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
