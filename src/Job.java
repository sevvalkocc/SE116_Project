import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job {
    private String jobID;
    private JobType jobType;
    private int startTime;
    private int duration;
    private int completionTime;

    public Job(String jobID, JobType jobType, int startTime, int duration) {
        this.jobID = jobID;
        this.jobType = jobType;
        this.startTime = startTime;
        this.duration = duration;
        this.completionTime = 0;
    }

    public String getJobID() {
        return jobID;
    }

    public JobType getJobType() {
        return jobType;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public List<TaskType> getTasks() {
        return jobType.getTasks();
    }

    public double getTaskSize(String taskTypeID) {
        return jobType.getTaskSize(taskTypeID);
    }

}