import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class JobFileParser {
    private Map<String, JobType> jobTypes;

    public JobFileParser(Map<String, JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }
    //parse jobs from job file
    public Map<String, Job> parse(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);

        Map<String, Job> jobs = new HashMap<>();

        //put all errors in an arraylist
        List<String> errorMessages = new ArrayList<>();

        int lineNumber=1;

        //it goes until can not find any line

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\s+");
            if (parts.length<4) {
                errorMessages.add("Invalid entry format for job\n->at line : " + lineNumber);
                continue;
            }

            String jobId=parts[0];
            String jobTypeId=parts[1];

            int timestart=-1;
            int duration=-1;

            try {
                timestart=Integer.parseInt(parts[2]);
                duration=Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                errorMessages.add("Invalid number format\n->at line : " + lineNumber);
                continue;
            }

            JobType jobType = jobTypes.get(jobTypeId);
            if (jobType == null) {
                errorMessages.add("JobType " + jobTypeId + " at line " + lineNumber + " is not defined.");
                continue;
            }

            List<Double> taskSizes = new ArrayList<>();
            for (int i=4;i<parts.length;i++) {
                try {
                    taskSizes.add(Double.parseDouble(parts[i]));
                } catch (NumberFormatException e) {
                    taskSizes.add(-1.0); // Default to -1.0 to use job type's default size
                }
            }
            //initalize jobId jobType timestart duration to the job hash map

            Job job = new Job(jobId, jobType, timestart, duration);
            jobs.put(jobId, job);
            lineNumber++;
        }
        //close file
        sc.close();

        if (!errorMessages.isEmpty()) {
            for (String error:errorMessages) {
                System.out.println(error);
            }
            System.exit(1); // Stop code if error
        }
        return jobs;
    }
}
