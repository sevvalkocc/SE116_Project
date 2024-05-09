import java.util.ArrayList;

public class JobType {
    private String jobID;
    private String jobtypeID;
    private int startTime;
    private int duration;
    private ArrayList<TaskType> taskTypes;


    public JobType(String jobID, String jobtypeID, int startTime, int duration) {
        this.jobID = jobID;
        this.jobtypeID = jobtypeID;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobtypeID() {
        return jobtypeID;
    }

    public void setJobtypeID(String jobtypeID) {
        this.jobtypeID = jobtypeID;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
