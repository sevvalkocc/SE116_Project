import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WorkflowFileParser {
    private Map<String, TaskType> taskTypes = new HashMap<>();
    private Map<String, JobType> jobTypes = new HashMap<>();
    private Map<String, Station> stations = new HashMap<>();
    private List<String> errorMessages = new ArrayList<>();

    public Map<String, Station> parse(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int lineNumber = 1;
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    lineNumber++;
                    continue;
                }

                if (line.startsWith("(TASKTYPES")) {
                    parseTaskTypes(scanner, lineNumber, line.substring("(TASKTYPES".length()).trim());
                } else if (line.startsWith("(JOBTYPES")) {
                    parseJobTypes(scanner, lineNumber);
                } else if (line.startsWith("(STATIONS")) {
                    parseStations(scanner, lineNumber);
                    break; // STATIONS bölümünden sonra daha fazla okumaya gerek yok
                }
                lineNumber++;
            }
        } finally {
            scanner.close();
            reportErrors();
        }
        return stations;
    }

    private void parseTaskTypes(Scanner scanner, int lineNumber, String content) {
        System.out.println("Parsing TASKTYPES at line " + lineNumber);
        content = content.replaceAll("[()]", ""); // Parantezleri kaldır
        String[] parts = content.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                if (!taskTypes.containsKey(part)) {
                    double size = 1.0; // Varsayılan size
                    if (i + 1 < parts.length && parts[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                        size = Double.parseDouble(parts[i + 1]);
                        if (size < 0) {
                            errorMessages.add("Line " + lineNumber + ": " + part + " has a negative task size.");
                            i++;
                            continue; // Hatalı size nedeniyle bu taskType'ı atlama
                        }
                        i++; // Sonraki kısmı atla çünkü size
                    }
                    taskTypes.put(part, new TaskType(part, size));
                    System.out.println("Added task type: " + part + " with size " + size); // Hata ayıklama mesajı
                } else {
                    errorMessages.add("Line " + lineNumber + ": TasktypeID listed twice: " + part);
                }
            } else {
                errorMessages.add("Line " + lineNumber + ": Invalid tasktypeID: " + part);
            }
        }
    }

    private void parseJobTypes(Scanner scanner, int lineNumber) {
        System.out.println("Parsing JOBTYPES at line " + lineNumber);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            System.out.println("Line " + lineNumber + ": " + line); // Hata ayıklama mesajı

            if (line.startsWith("(STATIONS")) {
                parseStations(scanner, lineNumber);
                break; // STATIONS bölümüne geçtiğimizde JOBTYPES bölümünü bitiririz
            }

            if (line.startsWith("(")) {
                line = line.substring(1).trim(); // Başlangıç parantezini kaldır
            }

            // Çift parantez ile biten satırları işle
            if (line.endsWith("))")) {
                line = line.substring(0, line.length() - 1).trim();
            }

            if (line.endsWith(")")) {
                line = line.substring(0, line.length() - 1).trim(); // Son parantezi kaldır
            }

            if (line.isEmpty()) {
                lineNumber++;
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length == 0)
                continue;

            String jobTypeID = parts[0];
            System.out.println("Found job type ID: " + jobTypeID); // Hata ayıklama mesajı

            if (jobTypes.containsKey(jobTypeID)) {
                errorMessages.add("Line " + lineNumber + ": JobType " + jobTypeID + " already declared.");
                continue;
            }

            List<TaskType> tasks = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String taskTypeID = parts[i];
                double size = 1.0; // Varsayılan büyüklük

                if (i + 1 < parts.length && parts[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                    size = Double.parseDouble(parts[i + 1]);
                    i++;
                } else if (taskTypes.containsKey(taskTypeID)) {
                    size = taskTypes.get(taskTypeID).getDefaultSize(); // Default size'ı kullan
                }

                if (!taskTypes.containsKey(taskTypeID)) {
                    errorMessages.add("Line " + lineNumber + ": Undefined task type " + taskTypeID + " in job type " + jobTypeID);
                } else {
                    TaskType taskType = new TaskType(taskTypeID, size);
                    tasks.add(taskType);
                }
            }
            jobTypes.put(jobTypeID, new JobType(jobTypeID, tasks));
            System.out.println("Added job type: " + jobTypeID); // Hata ayıklama mesajı

            lineNumber++; // Satır numarasını güncelle
        }
    }

    private void parseStations(Scanner scanner, int lineNumber) {
        System.out.println("Parsing STATIONS at line " + lineNumber);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            System.out.println("Line " + lineNumber + ": " + line); // Hata ayıklama mesajı

            if (line.startsWith("(")) {
                line = line.substring(1).trim(); // Sadece başlangıç parantezini kaldır
            }

            // Çift parantez ile biten satırları işle
            if (line.endsWith("))")) {
                line = line.substring(0, line.length() - 1).trim();
            }

            if (line.endsWith(")")) {
                line = line.substring(0, line.length() - 1).trim(); // Son parantezi kaldır
            }

            if (line.isEmpty()) {
                lineNumber++;
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length < 4) {
                errorMessages.add("Line " + lineNumber + ": Insufficient data for station definition.");
                lineNumber++;
                continue;
            }
            String stationID = parts[0];
            int capacity;
            try {
                capacity = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                errorMessages.add("Line " + lineNumber + ": Invalid capacity for station " + stationID);
                lineNumber++;
                continue;
            }
            boolean multiFlag = "Y".equals(parts[2]);
            boolean fifoFlag = "Y".equals(parts[3]);
            double stationSpeed = 1.0; // Varsayılan istasyon hızı
            double speedVariation = 0.0; // Varsayılan hız varyasyonu

            Station station = new Station(stationID, capacity, multiFlag, fifoFlag, stationSpeed, speedVariation);

            int i = 4;
            while (i < parts.length) {
                String taskTypeID = parts[i];
                double speed = 0.0;

                if (i + 1 < parts.length && parts[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                    speed = Double.parseDouble(parts[i + 1]);
                    i += 2;
                } else if (i == parts.length - 1 && parts[i].matches("-?\\d+(\\.\\d+)?")) {
                    // Bu, hız varyasyonu olmalı
                    speedVariation = Double.parseDouble(parts[i]);
                    break;
                } else {
                    errorMessages.add("Line " + lineNumber + ": Missing speed for task type " + taskTypeID);
                    break;
                }

                if (!taskTypes.containsKey(taskTypeID)) {
                    errorMessages.add("Line " + lineNumber + ": TaskType " + taskTypeID + " not executed in any STATIONS.");
                }
                station.addTaskType(taskTypeID, speed);
            }

            // İstasyonun hız varyasyonunu ayarla
            station.setSpeedVariation(speedVariation);

            stations.put(stationID, station);
            System.out.println("Added station: " + stationID); // Hata ayıklama mesajı

            // Yeni eklenen kısım: İstasyon özelliklerini yazdır
            System.out.println("Station Details:");
            System.out.println("  Station ID: " + station.getStationID());
            System.out.println("  Max Capacity: " + station.getMaxCapacity());
            System.out.println("  MultiFlag: " + station.isMultiFlag());
            System.out.println("  FIFO Flag: " + station.isFifoFlag());
            System.out.println("  Speed Variation: " + station.getSpeedVariation());
            for (Map.Entry<String, Double> entry : station.getTaskSpeeds().entrySet()) {
                System.out.println("  Task Type: " + entry.getKey() + ", Speed: " + entry.getValue());
            }

            lineNumber++; // Satır numarasını güncelle
        }
    }

    private void reportErrors() {
        if (!errorMessages.isEmpty()) {
            for (String error : errorMessages) {
                System.out.println(error);
            }
            System.exit(1); // Hata varsa programı durdur
        }
    }

    public Map<String, TaskType> getTaskTypes() {
        return taskTypes;
    }

    public Map<String, JobType> getJobTypes() {
        return jobTypes;
    }

    public Map<String, Station> getStations() {
        return stations;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
