import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class JobFileParser {
    private Map<String, JobType> jobTypes;

    public JobFileParser(Map<String, JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public Map<String, Job> parse(File file) throws FileNotFoundException {
        Map<String, Job> jobs = new HashMap<>();
        Scanner scanner = new Scanner(file);
        List<String> errorMessages = new ArrayList<>();
        int lineNum = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\s+");
            if (parts.length < 4) {
                errorMessages.add("Invalid job entry format at line " + lineNum);
                continue;
            }
            String jobId = parts[0];
            String jobTypeId = parts[1];
            int startTime = -1;
            int duration = -1;
            try {
                startTime = Integer.parseInt(parts[2]);
                duration = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                errorMessages.add("Invalid number format at line " + lineNum);
                continue;
            }

            JobType jobType = jobTypes.get(jobTypeId);
            if (jobType == null) {
                errorMessages.add("JobType " + jobTypeId + " at line " + lineNum + " is not defined.");
                continue;
            }

            List<Double> taskSizes = new ArrayList<>();
            for (int i = 4; i < parts.length; i++) {
                try {
                    taskSizes.add(Double.parseDouble(parts[i]));
                } catch (NumberFormatException e) {
                    taskSizes.add(-1.0); // Default to -1.0 to use job type's default size
                }
            }

            Job job = new Job(jobId, jobType, startTime, duration);
            jobs.put(jobId, job);
            lineNum++;
        }
        scanner.close();
        if (!errorMessages.isEmpty()) {
            for (String error : errorMessages) {
                System.out.println(error);
            }
            System.exit(1); // Stop code if error
        }
        return jobs;
    }
}
