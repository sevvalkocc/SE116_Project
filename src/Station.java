import java.util.*;

public class Station {
    private String stationID;
    private int maxCapacity;
    private int currentCapacity;
    private boolean multiFlag;
    private boolean fifoFlag;
    private Map<String, Queue<Job>> taskQueues;
    private double stationSpeed;
    private double speedVariation;
    private Map<String, Double> taskSpeeds;

    public Station(String stationID, int maxCapacity, boolean multiFlag, boolean fifoFlag) {
        this.stationID = stationID;
        this.maxCapacity = maxCapacity;
        this.multiFlag = multiFlag;
        this.fifoFlag = fifoFlag;
        this.taskQueues = new HashMap<>();
        this.taskSpeeds = new HashMap<>();
        this.stationSpeed = 1.0;
        this.speedVariation = 0.0;
    }

    public Station(String stationID, int maxCapacity, boolean multiFlag, boolean fifoFlag, double stationSpeed, double speedVariation) {
        this.stationID = stationID;
        this.maxCapacity = maxCapacity;
        this.multiFlag = multiFlag;
        this.fifoFlag = fifoFlag;
        this.taskQueues = new HashMap<>();
        this.taskSpeeds = new HashMap<>();
        this.stationSpeed = stationSpeed;
        this.speedVariation = speedVariation;
    }

    public void addTaskType(String taskTypeID, double speed) {
        taskQueues.put(taskTypeID, new LinkedList<>());
        taskSpeeds.put(taskTypeID, speed);
    }

    public void enqueueTask(TaskType task, Job job) {
        if (!taskQueues.containsKey(task.getTaskTypeID())) {
            System.out.println("Task type cannot be executed here: " + task.getTaskTypeID());
            return;
        }
        taskQueues.get(task.getTaskTypeID()).add(job);
        System.out.println("Job " + job.getJobID() + " is added to station " + stationID + " with task " + task.getTaskTypeID());
        printTaskQueue();
    }

    public boolean canHandleTaskType(String taskTypeID) {
        return taskQueues.containsKey(taskTypeID);
    }

    public int getQueueLengthForTask(String taskTypeID) {
        Queue<Job> queue = taskQueues.get(taskTypeID);
        return (queue != null) ? queue.size() : 0;
    }

    public double getSpeedForTask(String taskTypeID) {
        double baseSpeed = taskSpeeds.getOrDefault(taskTypeID, stationSpeed);
        if (speedVariation > 0) {
            Random random = new Random();
            double variationFactor = 1 + (random.nextDouble() * 2 - 1) * speedVariation;
            return baseSpeed * variationFactor;
        } else {
            return baseSpeed;
        }
    }

    public double getSpeedVariation() {
        return speedVariation;
    }

    public void setSpeedVariation(double speedVariation) {
        this.speedVariation = speedVariation;
    }

    public void processTasks() {
        if (multiFlag) {
            for (String taskTypeID : taskQueues.keySet()) {
                processTasksForType(taskTypeID);
            }
        } else {
            for (String taskTypeID : taskQueues.keySet()) {
                processTasksForType(taskTypeID);
                break;
            }
        }
    }

    private void processTasksForType(String taskTypeID) {
        Queue<Job> queue = taskQueues.get(taskTypeID);
        if (fifoFlag) {
            processTasksFIFO(queue);
        } else {
            processTasksEDD(queue);
        }
    }

    private void processTasksFIFO(Queue<Job> queue) {
        if (!queue.isEmpty() && currentCapacity < maxCapacity) {
            Job job = queue.poll();
            currentCapacity++;
            System.out.println("Process started: Job ID: " + job.getJobID() + ", Task ID: " + job.getJobType().getJobTypeID() + " at station " + stationID);
        }
        printTaskQueue();
    }

    private void processTasksEDD(Queue<Job> queue) {
        List<Job> jobList = new ArrayList<>(queue);
        jobList.sort(Comparator.comparingInt(job -> job.getStartTime() + job.getDuration()));

        if (!jobList.isEmpty() && currentCapacity < maxCapacity) {
            Job job = jobList.get(0);
            queue.remove(job);
            currentCapacity++;
            System.out.println("Process started: Job ID: " + job.getJobID() + ", Task ID: " + job.getJobType().getJobTypeID() + " at station " + stationID);
        }
        printTaskQueue();
    }

    private void printTaskQueue() {
        System.out.println("--------------------------------------");
        System.out.println("Station " + stationID + " current task queues:");
        for (Map.Entry<String, Queue<Job>> entry : taskQueues.entrySet()) {
            String taskTypeID = entry.getKey();
            Queue<Job> queue = entry.getValue();
            System.out.print("Task Type: " + taskTypeID + " Queue: ");
            for (Job job : queue) {
                System.out.print(job.getJobID() + " ");
            }
            System.out.println();
        }
        printCurrentTasks();
    }

    private void printCurrentTasks() {
        System.out.println("Station " + stationID + " currently processing tasks:");
        for (Map.Entry<String, Queue<Job>> entry : taskQueues.entrySet()) {
            String taskTypeID = entry.getKey();
            Queue<Job> queue = entry.getValue();
            if (!queue.isEmpty()) {
                Job job = queue.peek();
                System.out.println("Currently processing: Job ID: " + job.getJobID() + ", Task Type: " + taskTypeID);
            }
        }
        System.out.println("--------------------------------------");
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void decrementCapacity() {
        currentCapacity--;
    }

    public String getStationID() {
        return stationID;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isMultiFlag() {
        return multiFlag;
    }

    public boolean isFifoFlag() {
        return fifoFlag;
    }

    public Map<String, Queue<Job>> getTaskQueues() {
        return taskQueues;
    }

    public Map<String, Double> getTaskSpeeds() {
        return taskSpeeds;
    }
}