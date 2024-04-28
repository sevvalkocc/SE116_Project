public class TaskType extends Task{
    private int defaultSize;



    public TaskType(String taskId, String taskType, int executionTime, int defaultSize) {
        super(taskId, taskType, executionTime);
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }
}

