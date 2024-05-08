public class TaskType extends Task{
    private String defaultSize;



    public TaskType(String taskId, String taskType, int executionTime, String defaultSize) {
        super(taskId, taskType, executionTime);
        this.defaultSize = defaultSize;
    }

    public String getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(String defaultSize) {
        this.defaultSize = defaultSize;
    }
}

