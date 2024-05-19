import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please enter workflow file and job file names from the command line.");
            return;
        }

        String workflowFileName = args[0];
        String jobFileName = args[1];

        File workflowFile = new File(workflowFileName);
        File jobFile = new File(jobFileName);

        try {
            WorkflowFileParser workflowParser = new WorkflowFileParser();
            Map<String, Station> stations = workflowParser.parse(workflowFile);
            Map<String, JobType> jobTypes = workflowParser.getJobTypes();

            if (jobTypes.isEmpty()) {
                System.out.println("No job types found after parsing. Please check the workflow file.");
                return;
            }

            JobFileParser jobParser = new JobFileParser(jobTypes);
            Map<String, Job> jobs = jobParser.parse(jobFile);

            StationM stationManager = new StationM();
            stations.forEach((id, station) -> stationManager.addStation(station));

            JobM jobManager = new JobM(stationManager);
            jobs.values().forEach(jobManager::addJob);

            while (!jobManager.isEventQueueEmpty()) {
                jobManager.processNextEvent();
            }

            Map<String, List<Job>> jobsByType = jobs.values().stream()
                    .collect(Collectors.groupingBy(job -> job.getJobType().getJobTypeID()));

            EventReport reporting = new EventReport(jobsByType, stationManager.getStations());
            reporting.calculateAverageTardiness();
            reporting.calculateStationUtilization();

        } catch (FileNotFoundException e) {
            System.out.println("File cannot found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }


}