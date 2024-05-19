import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please enter workflow file and job file names...");
            return;
        }
        String workflowFileName;
        String jobFileName;

        if(args[0].startsWith("job.")){
            jobFileName=args[0];
            workflowFileName=args[1];
        }else{
            jobFileName=args[1];
            workflowFileName=args[0];
        }

        checkFile(workflowFileName);
        checkFile(jobFileName);

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

            Station stationManager = new Station();
            for (Station station : stations.values()) {
                stationManager.addStation(station);
            }


            JobType jobManager = new JobType(stationManager);
            for (Job job : jobs.values()) {
                jobManager.addJob(job);
            }

            while (!jobManager.isEventQueueEmpty()) {
                jobManager.nextEventProcess();
            }

            Map<String, List<Job>> jobsByType=new HashMap<>();
            for (Job job: jobs.values()){
                String jobTypeID=job.getJobType().getJobTypeID();
                if (!jobsByType.containsKey(jobTypeID)){
                    jobsByType.put(jobTypeID,new ArrayList<>());
                }
                jobsByType.get(jobTypeID).add(job);
            }

            calculateAverageTardiness(jobsByType);
            calculateStationUtilization(stations);

        } catch(Exception e){
            System.out.println("An error occurred: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private static void calculateAverageTardiness(Map<String, List<Job>> jobsByType) {
        double totalTardiness = 0;
        int completedJobs = 0;
        System.out.println("Average Latency Times:");
        for (Map.Entry<String, List<Job>> entry : jobsByType.entrySet()) {
            String jobType = entry.getKey();
            List<Job> jobs = entry.getValue();
            double jobTotalTardiness = 0;
            int jobCompletedJobs = 0;

            for (Job job : jobs) {
                if (job.getCompletionTime() > 0) {
                    int tardiness = job.getCompletionTime() - (job.getStartTime() + job.getDuration());
                    if (tardiness > 0) {
                        jobTotalTardiness += tardiness;
                        jobCompletedJobs++;
                        totalTardiness += tardiness;
                        completedJobs++;
                    }
                }
            }

            if (jobCompletedJobs > 0) {
                double averageTardiness = jobTotalTardiness / jobCompletedJobs;
                System.out.println("Job Type: " + jobType + ", Average Latency: " + averageTardiness + " seconds");
            }
        }

        double overallAverageTardiness = completedJobs > 0 ? totalTardiness / completedJobs : 0;
        System.out.println("Overall Average Tardiness: " + overallAverageTardiness + " seconds");
    }

    private static void calculateStationUtilization(Map<String, Station> stations) {
        System.out.println("Station Usage Rates:");
        for (Map.Entry<String, Station> entry : stations.entrySet()) {
            String stationId = entry.getKey();
            Station station = entry.getValue();
            double utilization = (double) station.getCurrentCapacity() / station.getMaxCapacity() * 100;
            System.out.println("Station ID: " + stationId + ", Usage Rate: " + utilization + "%");
       }
}

    private static boolean checkFile(String fileName){
        File file=new File(fileName);
        if(!file.exists()){
            System.out.println("Error: File "+ fileName +" does not exist");
            return false;
        }
        if (!file.canRead()) {
            System.out.println("Error: File " + fileName + " is not accessible.");
            return false;
        }
        System.out.println("File " + fileName + " exists and is accessible.");
        return true;
    }


}