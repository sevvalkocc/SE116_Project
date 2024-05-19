import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobType {
    private String jobTypeID;
    private List<TaskType> tasks;
    private Map<String, Double> taskSizes;
    private Map<String, Job> jobs = new HashMap<>();
    private EventSimulation eventSimulation;
    private Map<String, Station> station;

    public JobType(String jobTypeID, List<TaskType> tasks, Map<String, Double> taskSizes, EventSimulation eventSimulation,Map<String, Station> station) {
        this.jobTypeID = jobTypeID;
        this.tasks = new ArrayList<>(tasks);
        this.taskSizes = new HashMap<>(taskSizes);
        this.station = station;
        this.eventSimulation = new EventSimulation();
    }

    public JobType(String jobTypeID, List<TaskType> tasks) {
        this.jobTypeID = jobTypeID;
        this.tasks = new ArrayList<>(tasks);

    }

    public JobType(Station stationManager) {
    }

    public String getJobTypeID() {
        return jobTypeID;
    }

    public List<TaskType> getTasks() {
        return tasks;
    }

    public double getTaskSize(String taskTypeID) {
        return taskSizes.getOrDefault(taskTypeID, 1.0);
    }

    public void setTaskSize(String taskTypeID, double size) {
        taskSizes.put(taskTypeID, size);
    }

    public void addJob(Job job) {
        System.out.println("Job is added: " + job.getJobID());
        if (tasks.isEmpty()) {
            System.out.println("JobType for Job ID " + job.getJobID() + " has no tasks defined.");
            return;
        }
        TaskType firstTask = tasks.get(0);

        Event firsEvent = new Event(job.getStartTime(), Event.Type.StartJob, job, firstTask);
        eventSimulation.addEvent(firsEvent);
        jobs.put(job.getJobID(), job);
    }

    public void nextEventProcess() {
        while (!eventSimulation.isEmpty()) {
            Event event = eventSimulation.getNextEvent();

            System.out.println("Event time: " + event.getTime() + ", Event type: " + event.getType() + ", Job ID: " + event.getJob().getJobID());
            switch (event.getType()) {
                case StartJob:
                    handleJobStart(event);
                    break;
                case CompleteTask:
                    handleTaskComplete(event);
                    break;
            }
            printJobStatus(event.getJob());
            for (Station station : station.values()) {
                station.processTasks();
            }
        }
    }

    private void handleJobStart(Event event) {
        Job job = event.getJob();
        TaskType taskType = event.getTaskType();
        Station station =getBestStationForTask(taskType);
        if (station != null) {
            station.addTask(taskType, job);
            System.out.println("Job " + job.getJobID() + " started at station " + station.getStationID() + " for task " + taskType.getTaskTypeID());
            int completionTime = event.getTime() + (int) (taskType.getDefaultSize() * 60 / station.getSpeedForTask(taskType.getTaskTypeID()));
            Event completeEvent = new Event(completionTime, Event.Type.CompleteTask, job, taskType);
            eventSimulation.addEvent(completeEvent);
        } else {
            System.out.println("No suitable station found for starting job " + job.getJobID());
        }
    }

    private void handleTaskComplete(Event event) {
        Job job = event.getJob();
        TaskType completedTask = event.getTaskType();
        List<TaskType> tasks = job.getJobType().getTasks();
        int taskIndex = tasks.indexOf(completedTask);
        if (taskIndex < tasks.size() - 1) {
            TaskType nextTask = tasks.get(taskIndex + 1);
            Station nextStation = getBestStationForTask(nextTask);
            if (nextStation != null) {
                nextStation.addTask(nextTask, job);
                int completionTime = event.getTime() + (int) (nextTask.getDefaultSize() * 60 / nextStation.getSpeedForTask(nextTask.getTaskTypeID()));
                Event completeEvent = new Event(completionTime, Event.Type.CompleteTask, job, nextTask);
                eventSimulation.addEvent(completeEvent);
            } else {
                System.out.println("No suitable station found for next task " + nextTask.getTaskTypeID() + " of job " + job.getJobID());
            }
        } else {
            System.out.println("Job " + job.getJobID() + " completed.");
            job.setCompletionTime(event.getTime());
        }
    }
    private Station getBestStationForTask(TaskType task) {
        Station bestStation = null;
        int minQueueLength = Integer.MAX_VALUE;
        for (Station station : station.values()) {
            if (station.isValidTaskType(task.getTaskTypeID())) {
                int queueLength = station.getQueueLengthForTask(task.getTaskTypeID());
                if (queueLength < minQueueLength) {
                    minQueueLength = queueLength;
                    bestStation = station;
                }
            }
        }
        return bestStation;
    }
    public boolean isEventQueueEmpty() {
        return eventSimulation.isEmpty();
    }

    private void printJobStatus(Job job) {
        System.out.println("Job status:");
        System.out.println("  Job ID: " + job.getJobID());
        System.out.println("  Starting Time: " + job.getStartTime());
        System.out.println("  Complete Time: " + (job.getCompletionTime() > 0 ? job.getCompletionTime() : "Not Completed"));
        System.out.println("  Tasks:");
        for (TaskType task : job.getJobType().getTasks()) {
            System.out.println("    Task ID: " + task.getTaskTypeID() + ", Default Size: " + task.getDefaultSize());
        }
        System.out.println("--------------------------------------");
    }

}