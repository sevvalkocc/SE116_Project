public class Task {
    private String taskId;
    private String jobType;
    private int executionTime;

    public Task(String taskId, String jobType, int executionTime) {
        this.taskId = taskId;
        this.jobType = jobType;
        this.executionTime = executionTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
}

