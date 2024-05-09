public class TaskType2 {

    private double defaultSize;
    private String taskTypeId;
    private double size;

    public double getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(double defaultSize) {
        this.defaultSize = defaultSize;
    }

    public String getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(String taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public TaskType2 (String taskTypeId, double size) {
        this.taskTypeId = taskTypeId;
        this.size = size;
    }

    public TaskType2 (String taskTypeId) {
        this.taskTypeId = taskTypeId;
        defaultSize = 0;
    }

    @Override
    public String toString() {
        return "TaskType{" +
                "taskTypeId='" + taskTypeId + '\'' +
                ", size=" + size +
                '}';
    }
}

