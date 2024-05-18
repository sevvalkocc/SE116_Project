public class Event implements Comparable<Event>{
    public enum Type {
        StartJob,CompleteTask
    }

    private int time;
    private Type type;
    private Job job;
    private TaskType taskType;

    public Event(int time, Type type, Job job, TaskType taskType) {
        this.time = time;
        this.type = type;
        this.job = job;
        this.taskType = taskType;
    }

    public int getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public Job getJob() {
        return job;
    }

    public TaskType getTaskType() {
        return taskType;
    }
    public int compareTo(Event event1){
        return Integer.compare(this.time, event1.time);
    }
}
