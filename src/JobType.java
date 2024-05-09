import java.util.ArrayList;

public class JobType {
    private String jobTypeId;
    private ArrayList<TaskType> taskTypes;;
    private double size;

    public String getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(String jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public JobType(String jobTypeId, ArrayList<TaskType> taskTypes, double size) {
        this.jobTypeId = jobTypeId;
        this.taskTypes = taskTypes;
        this.size = size;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public ArrayList<TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(ArrayList<TaskType> taskTypes) {
        this.taskTypes = taskTypes;
    }

}
