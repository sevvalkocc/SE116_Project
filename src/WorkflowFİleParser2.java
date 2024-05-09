import java.io.*;
import java.util.*;

public class WorkflowFileParser2 {

    private String fileName;
    private ArrayList<TaskType2> taskTypeList;
    private ArrayList<JobType> jobTypeList;
    private ArrayList<Station> stationList;
    private ArrayList<String> errorMessages;
    Scanner scanner = new Scanner(System.in);

    public WorkflowFileParser2 (String fileName) {
        this.fileName = fileName;
        // WorkflowFileParser.taskTypeList = new ArrayList<>();
        // WorkflowFileParser.jobTypeList = new ArrayList<>();
        // WorkflowFileParser.stationList = new ArrayList<>();
        this.errorMessages = new ArrayList<>();

        this.taskTypeList = new ArrayList<>();
        this.jobTypeList = new ArrayList<>();
        this.stationList = new ArrayList<>();

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

    protected void parseLine(String line, int lineNumber) {
        if (line.startsWith("(TASKTYPES")) {
            parseTaskTypes(new Scanner(line.substring(10)), lineNumber);
        } else if (line.startsWith("(JOBTYPES")) {
            parseJobTypes(new Scanner(line.substring(9)), lineNumber);
        } else if (line.startsWith("(STATIONS")) {
            parseStations(new Scanner(line.substring(9)), lineNumber);
        }
    }

    protected void parseTaskTypes(Scanner scanner, int lineNumber) {
        String line = scanner.nextLine().trim(); // Read the whole line
        int endIndex = line.indexOf(")");

        if (endIndex != -1) {
            line = line.substring(10, endIndex).trim(); // Trim TASKTYPES and ")"
            String[] parts = line.split(" ");

            for (int i = 0; i < parts.length; i += 2) {
                String taskTypeId = parts[i];
                double size = 1.0; // Default size

                if (i + 1 < parts.length && isNumeric(parts[i + 1])) {
                    size = Double.parseDouble(parts[i + 1]);
                } else {
                    i--; // Move back if size not found
                }

                taskTypeList.add(new TaskType2(taskTypeId, size)); // Add TaskType to the list
            }
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected void parseJobTypes(Scanner scanner, int lineNumber) {
        // Skip "JOBTYPES" token
        scanner.next();

        while (scanner.hasNext()) {
            String token = scanner.next().trim();
            if (token.equals(")"))
                break;

            // Extract the job ID
            String jobTypeID = token.substring(1);

            // Read the whole line for the job types
            String jobTypeLine = scanner.nextLine().trim();

            // Remove the enclosing parentheses
            jobTypeLine = jobTypeLine.substring(1, jobTypeLine.length() - 1);

            // Split the job type line by whitespace
            String[] jobTypeParts = jobTypeLine.split("\\s+");

            // Create a JobType object for the current job type
            for (String part : jobTypeParts) {
                // Skip empty parts
                if (part.isEmpty())
                    continue;

                // Extract task ID and size
                String taskTypeID = part.substring(1);
                double size = 1.0; // Default size

                // Check if size is provided
                if (part.length() > 2 && isNumeric(part.substring(1))) {
                    size = Double.parseDouble(part.substring(1));
                }

                // Create and add the JobType object to the list
                jobTypeList.add(new JobType(jobTypeID, taskTypes, size));
            }
        }
    }

    protected void parseStations(Scanner scanner, int lineNumber) {
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue; // Boş satırları atla
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String token = lineScanner.next().trim();
                if (token.equals("STATIONS"))
                    continue; // "STATIONS" başlığını atla
                if (token.equals("(")) {
                    String stationId = lineScanner.next();
                    int maxCapacity = lineScanner.nextInt();
                    boolean multiFlag = lineScanner.next().equals("Y"); // "Y" ise true, değilse false
                    boolean fifoFlag = lineScanner.next().equals("Y"); // "Y" ise true, değilse false
                    ArrayList<TaskType> requiredTasks = new ArrayList<>();

                    while (lineScanner.hasNext()) {
                        String taskID = lineScanner.next().trim();
                        double taskSize = lineScanner.nextDouble();
                        if (taskID.endsWith(")")) {
                            taskID = taskID.substring(0, taskID.length() - 1); // ")" kaldır
                            requiredTasks.add(new TaskType2(taskID, taskSize));
                            break;
                        }
                        requiredTasks.add(new TaskType2(taskID, taskSize));
                    }
                    double stationSpeed = lineScanner.nextDouble();
                    // Station nesnesi oluştur ve listeye ekle

                    stationList.add(new Station(stationId, maxCapacity, multiFlag, fifoFlag, requiredTasks, stationSpeed));
                }
            }
            lineScanner.close();
        }
    }

    public ArrayList<JobType> getJobTypeList() {
        return jobTypeList;
    }

    public ArrayList<Station> getStationList() {
        return stationList;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public ArrayList<TaskType2> getTaskTypeList() {
        return taskTypeList;
    }

}

