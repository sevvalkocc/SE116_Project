import java.util.*;

public class Station {
    private String stationID;
    private int maxCapacity;
    private int currentCapacity;
    private boolean multiFlag;
    private boolean fifoFlag;
    private Map<String, ArrayList<Job>> taskLists;
    private double stationSpeed;
    private double speedVariation;
    private Map<String, Double> taskSpeeds;
    private Map<String, Station> stations;

    public Station(String stationID, int maxCapacity, boolean multiFlag, boolean fifoFlag, double stationSpeed, double speedVariation) {
        this.stationID = stationID;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.multiFlag = multiFlag;
        this.fifoFlag = fifoFlag;
        this.taskLists = new HashMap<>();
        this.taskSpeeds = new HashMap<>();
        this.stationSpeed = stationSpeed;
        this.speedVariation = speedVariation;
        this.stations = new HashMap<>();
        // this.stations.put(stationID, this);
    }

    public Station() {
        this.stations = new HashMap<>();
    }

    public int getCurrentCapacity() {
        return currentCapacity;
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
    public Map<String, Double> getTaskSpeeds() {
        return taskSpeeds;
    }

    public double getSpeedVariation() {
        return speedVariation;
    }

    public void setSpeedVariation(double speedVariation) {
        this.speedVariation = speedVariation;
    }

    public void addStation(Station station) {
        if (station != null) {
            stations.put(station.getStationID(), station);
        }
    }


    /*public Station getBestStationForTask(TaskType task) {
        Station bestStation = null;
        int minQueueLength = Integer.MAX_VALUE;

        for (Station station : stations.values()) {
            if (station.canHandleTaskType(task.getTaskTypeID())) {
                int queueLength = station.getQueueLengthForTask(task.getTaskTypeID());
                if (queueLength < minQueueLength) {
                    minQueueLength = queueLength;
                    bestStation = station;
                }
            }
        }

        return bestStation;
    }*/

    public void addTaskType(String taskTypeID, double speed) {
        taskLists.put(taskTypeID, new ArrayList<>());
        taskSpeeds.put(taskTypeID, speed);
    }

    public void addTask(TaskType task, Job job) {
        /*if (!taskLists.containsKey(task.getTaskTypeID())) {
            System.out.println("Task type cannot be executed here: " + task.getTaskTypeID());
            return;
        }
        taskLists.get(task.getTaskTypeID()).add(job);
        System.out.println("Job " + job.getJobID() + " is added to station " + stationID + " with task " + task.getTaskTypeID());
       printTaskQueue();
       */
        List<Job> jobs = taskLists.get(task.getTaskTypeID());
        if (jobs == null) {
            System.out.println("Task type cannot be executed here: " + task.getTaskTypeID());
            return;
        }
        jobs.add(job);
        System.out.println("Job " + job.getJobID() + " is added to station " + stationID + " with task " + task.getTaskTypeID());
        printTaskQueue();
    }


    public boolean isValidTaskType(String taskTypeID) {
        return taskLists.get(taskTypeID)!=null;
    }

    public int getQueueLengthForTask(String taskTypeID) {
        ArrayList<Job> list = taskLists.get(taskTypeID);
        if(list!=null){
            return list.size();
        }
        else{
            return 0;
        }
    }

    public double getSpeedForTask(String taskTypeID) {
        Double speed = taskSpeeds.get(taskTypeID);

        if (speed == null) {
            speed = stationSpeed;
        }
        if (speedVariation > 0) {
            double variationFactor = 1.0 + Math.random() * speedVariation - speedVariation / 2;
            return speed * variationFactor;
        }
        return speed;
    }
    /*public void mainProcessTasks() {
        if (multiFlag) {
            for (String taskTypeID : taskLists.keySet()) {
                processTasksForType(taskTypeID);
            }
        } else {
            for (String taskTypeID : taskLists.keySet()) {
                processTasksForType(taskTypeID);
                break;
            }
        }
    }
    public void processTasksForType(String taskTypeID) {
        ArrayList<Job> jobList  = taskLists.get(taskTypeID);
        if (fifoFlag) {
            processTasksForFIFO(jobList );
        } else {
            processTasksForEDD(jobList );
        }
    }

    public void processTasksForFIFO(ArrayList<Job>jobList ) {
        if (!jobList .isEmpty() && currentCapacity < maxCapacity) {
            Job job = jobList .remove(0);
            currentCapacity++;
            System.out.println("Process started: Job ID: " + job.getJobID() + ", Task ID: " + job.getJobType().getJobTypeID() + " at station " + stationID);
        }
        printTaskQueue();
    }

    public void processTasksForEDD(ArrayList<Job> jobList) {
        if (!jobList.isEmpty() && currentCapacity < maxCapacity) {
            Collections.sort(jobList, Comparator.comparingInt(Job::getStartTime));
            Job job = jobList.remove(0);
            currentCapacity++;
            System.out.println("Process started: Job ID: " + job.getJobID() + ", Task ID: " + job.getJobType().getJobTypeID() + " at station " + stationID);
        }
        printTaskQueue();
    }*/
    public void processTasks() {
        for (String taskTypeID : taskLists.keySet()) {
            ArrayList<Job> jobList = taskLists.get(taskTypeID);

            if (!jobList.isEmpty() && currentCapacity < maxCapacity) {
                Job jobToProcess;

                if (fifoFlag) {
                    jobToProcess = jobList.remove(0);
                } else {
                    for (int i=0;i<jobList.size()-1;i++) {
                        for (int j=i+1;j<jobList.size();j++) {
                            if (jobList.get(i).getStartTime()>jobList.get(j).getStartTime()) {
                                Job temp = jobList.get(i);
                                jobList.set(i, jobList.get(j));
                                jobList.set(j, temp);
                            }
                        }
                    }
                    jobToProcess = jobList.remove(0);
                }
                currentCapacity++;
                System.out.println("Process started: Job ID: " + jobToProcess.getJobID() + ", Task ID: " + jobToProcess.getJobType().getJobTypeID() + " at station " + stationID);

                if (!multiFlag) {
                    break;
                }
            }
        }
        printTaskQueue();
    }
    public void printTaskQueue() {
        System.out.println("--------------------------------------");
        System.out.println("Station " + stationID + " current task queues:");
        for (String taskTypeID : taskLists.keySet()) {
            ArrayList<Job> jobList = taskLists.get(taskTypeID);
            System.out.print("Task Type: " + taskTypeID + " Queue: ");
            for (Job job:jobList) {
                System.out.print(job.getJobID() + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }
}