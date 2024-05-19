import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobM {
    private Map<String, Job> jobs = new HashMap<>();
    private EventSimulation eventQueue;
    private StationM stationManager;

    public JobM(StationM stationManager) {
        this.stationManager = stationManager;
        this.eventQueue = new EventSimulation();
    }

    public void addJob(Job job) {
        System.out.println("Job is added: " + job.getJobID());
        List<TaskType> tasks = job.getJobType().getTasks();
        if (tasks.isEmpty()) {
            System.out.println("JobType for Job ID " + job.getJobID() + " has no tasks defined.");
            return;
        }

        TaskType firstTask = tasks.get(0);
        Event startEvent = new Event(job.getStartTime(), Event.Type.JOB_START, job, firstTask);
        eventQueue.addEvent(startEvent);
        jobs.put(job.getJobID(), job);
    }

    public void processNextEvent() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.getNextEvent();
            System.out.println("Event time: " + event.getTime() + ", Event type: " + event.getType() + ", Job ID: "
                    + event.getJob().getJobID());
            switch (event.getType()) {
                case JOB_START:
                    handleJobStart(event);
                    break;
                case TASK_COMPLETE:
                    handleTaskComplete(event);
                    break;
            }
            printJobStatus(event.getJob());
            // Process tasks for all stations after each event
            for (Station station : stationManager.getStations().values()) {
                station.processTasks();
            }
        }
    }

    private void printJobStatus(Job job) {
        System.out.println("Job status:");
        System.out.println("  Job ID: " + job.getJobID());
        System.out.println("  Starting Time: " + job.getStartTime());
        System.out.println(
                "  Complete Time: " + (job.getCompletionTime() > 0 ? job.getCompletionTime() : "Not Completed"));
        System.out.println("  Tasks:");
        for (TaskType task : job.getJobType().getTasks()) {
            System.out.println(
                    "    Task ID: " + task.getTaskTypeID() + ", Default Size: " + task.getDefaultSize());
        }
        System.out.println("--------------------------------------");
    }

    private void handleJobStart(Event event) {
        Job job = event.getJob();
        TaskType taskType = event.getTaskType();
        Station station = stationManager.getBestStationForTask(taskType);
        if (station != null) {
            station.enqueueTask(taskType, job);
            System.out.println("Job " + job.getJobID() + " started at station " + station.getStationID() + " for task "
                    + taskType.getTaskTypeID());
            // Schedule task completion event
            int completionTime = event.getTime() + (int) (taskType.getDefaultSize() * 60 / station.getSpeedForTask(taskType.getTaskTypeID()));
            Event completeEvent = new Event(completionTime, Event.Type.TASK_COMPLETE, job, taskType);
            eventQueue.addEvent(completeEvent);
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
            Station nextStation = stationManager.getBestStationForTask(nextTask);
            if (nextStation != null) {
                nextStation.enqueueTask(nextTask, job);
                System.out.println("Task " + completedTask.getTaskTypeID() + " completed for job " + job.getJobID()
                        + ". Next task " + nextTask.getTaskTypeID() + " started at station "
                        + nextStation.getStationID());
                // Schedule next task completion event
                int completionTime = event.getTime() + (int) (nextTask.getDefaultSize() * 60 / nextStation.getSpeedForTask(nextTask.getTaskTypeID()));
                Event completeEvent = new Event(completionTime, Event.Type.TASK_COMPLETE, job, nextTask);
                eventQueue.addEvent(completeEvent);
            } else {
                System.out.println("No suitable station found for next task " + nextTask.getTaskTypeID() + " of job "
                        + job.getJobID());
            }
        } else {
            System.out.println("Job " + job.getJobID() + " completed.");
            job.setCompletionTime(event.getTime());
        }
    }

    public boolean isEventQueueEmpty() {
        return eventQueue.isEmpty();
    }
}
