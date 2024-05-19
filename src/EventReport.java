import java.util.List;
import java.util.Map;

public class EventReport {
    private Map<String, List<Job>> jobsByType;
    private Map<String, Station> stations;

    public EventReport(Map<String, List<Job>> jobsByType, Map<String, Station> stations) {
        this.jobsByType = jobsByType;
        this.stations = stations;
    }

    public void calculateAverageTardiness() {
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

    public void calculateStationUtilization() {
        System.out.println("Station Usage Rates:");
        for (Map.Entry<String, Station> entry : stations.entrySet()) {
            String stationId = entry.getKey();
            Station station = entry.getValue();
            double utilization = (double) station.getCurrentCapacity() / station.getMaxCapacity() * 100;
            System.out.println("Station ID: " + stationId + ", Usage Rate: " + utilization + "%");
        }
    }
    }


