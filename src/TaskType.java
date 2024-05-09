public class TaskType{
    private String tasktypesID;
    private int defaultSize;
    private int size;

    public TaskType(String tasktypesID, int defaultSize, int size) {
        this.tasktypesID = tasktypesID;
        this.defaultSize = defaultSize;
        this.size = size;
    }

    public String getTasktypesID() {
        return tasktypesID;
    }

    public void setTasktypesID(String tasktypesID) {
        this.tasktypesID = tasktypesID;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

