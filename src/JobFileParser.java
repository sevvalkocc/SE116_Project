import java.io.*;
import java.util.*;

public class JobFileParser {

    private String fileName;

    private static ArrayList<JobType> jobTypeList;
    private static ArrayList<JobType> jobList;
    private ArrayList<String> errorMessages;

    public JobFileParser(String fileName) {
        this.fileName = fileName;
        JobFileParser.jobTypeList = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
    }

    public void parse() throws FileNotFoundException {

        File file = new File(fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

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

            JobType job = new JobType (jobId, jobTypeId, startTime, duration);

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
}
