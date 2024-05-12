import java.io.*;
import java.util.*;

public class JobFileParser {

    private static ArrayList<JobType> jobList;
    private ArrayList<String> errorMessages;

    public JobFileParser() {
        errorMessages = new ArrayList<>();
        jobList = new ArrayList<>();
    }

    public void parse(File file) throws FileNotFoundException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            Scanner scanner = new Scanner(file);
            int lineNumber = 0;

            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                try {
                    parseLine(line, lineNumber);
                } catch (Exception e) {
                    errorMessages.add("Error at line " + lineNumber + ": " + e.getMessage());
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    private void parseLine(String line, int lineNumber) {

        String[] parts = line.split("\\s+");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Incorrect number of fields");
        }

        try {
            String jobId = parts[0];
            String jobTypeId = parts[1];
            int startTime = Integer.parseInt(parts[2]);
            int duration = Integer.parseInt(parts[3]);

            JobType job = new JobType(jobId, jobTypeId, startTime, duration);

            jobList.add(job);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown error occurred");
        }
    }

    public static ArrayList<JobType> getJobs() {
        return jobList;
    }

    // For testing Job Array List
    public void test() {
        if (jobList != null) {
            System.out.println("Job List:");
            for (JobType job : jobList) {
                System.out.println(job);
            }
        } else {
            System.out.println("Job List is empty");
        }
        for (String errorMessage : errorMessages) {
            System.out.println(errorMessage);
        }
    }
}
