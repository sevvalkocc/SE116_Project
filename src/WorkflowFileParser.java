import java.io.File;
import java.util.*;

public class WorkflowFileParser {

    private Map<String, TaskType> taskTypes = new HashMap<>();
    private Map<String, JobType> jobTypes = new HashMap<>();
    private Map<String, Station> stations = new HashMap<>();
    private List <String> errorMessages = new ArrayList<>();


    public Map<String,Station> parse(File file)throws Exception {
        Scanner sc = new Scanner(file);
        int lineNumber = 1;
        try {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    lineNumber++;
                    continue;
                }
                if (line.startsWith("(TASK TYPES")) {
                    parseTaskTypes(sc, lineNumber, line.substring("(TASKTYPES".length()).trim());

                } else if (line.startsWith("(JOB TYPES")) {
                    parseJobTypes(sc, lineNumber);

                } else if (line.startsWith("(STATIONS")) {
                    parseStations(sc, lineNumber);
                    break;
                }
                lineNumber++;
            }
        } finally {
            sc.close();
            reportErrors();
        }
        return stations;
    }
    private void parseTaskTypes(Scanner sc,int lineNumber,String content){
        System.out.println("Parsing TASKTYPES at line "+lineNumber);
        content=content.replaceAll("[()]","");
        String parts[]=content.split("\\s+");
        for (int i=0;i<parts.length;i++){
            String part=parts[i];
            if (part.matches("^[a-zA-Z][a-zA-Z0-9_]*$")){
                if (!taskTypes.containsKey(part)){
                    double size=1.0;
                    if (i+1< parts.length && parts[i+1].matches("-?\\d+(\\.\\d+)?")){
                        size=Double.parseDouble(parts[i+1]);
                        if (size<0){
                            errorMessages.add("line "+lineNumber+": "+part+" has a negative task size:");
                            i++;
                            continue;
                        }
                        i++;
                    }
                    taskTypes.put(part,new TaskType(part,size));
                    System.out.println("Added task type: "+part+" with size "+size);
                }else {
                    errorMessages.add("Line "+lineNumber+": TasktypeID listed twice: "+part);
                }
            }else {
                errorMessages.add("Line: "+lineNumber+": Invalid tasktypeID: "+part);
            }
        }
    }
    private void parseJobTypes(Scanner sc,int lineNumber){
        System.out.println("Parsing JOBTYPES at line "+lineNumber);
        while (sc.hasNextLine()){
            String line=sc.nextLine().trim();
            System.out.println("Line "+lineNumber+": "+line);
            if (line.startsWith("(STATIONS")){
                parseStations(sc,lineNumber);
                break;
            }
            if (line.startsWith("(")){
                line=line.substring(1).trim();
            }
            if (line.endsWith("))")){
                line=line.substring(0,line.length()-1).trim();
            }
            if (line.isEmpty()){
                lineNumber++;
                continue;
            }
            String parts[]=line.split("\\s+");
            if (parts.length==0){
                continue;
            }
            String jobTypeID=parts[0];
            System.out.println("Found job type ID: "+jobTypeID);

            if (jobTypes.containsKey(jobTypeID)){
                errorMessages.add("Line "+lineNumber+": JobType "+jobTypeID+" already declared.");
                continue;
            }
            List<TaskType> tasks=new ArrayList<>();
            for (int i=1;i<parts.length;i++){
                String taskTypeID=parts[i];
                double size=1.0;
                if (i+1< parts.length && parts[i+1].matches("-?\\d+(\\.\\d+)?")){
                    size=Double.parseDouble(parts[i+1]);
                    i++;
                }else if (taskTypes.containsKey(taskTypeID)){
                    size=taskTypes.get(taskTypeID).getDefaultSize();
                }
                if (!taskTypes.containsKey(taskTypeID)){
                    errorMessages.add("Line "+lineNumber+": Undefined task type "+taskTypeID+" in job type "+ jobTypeID);
                }else{
                    TaskType taskType=new TaskType(taskTypeID,size);
                    tasks.add(taskType);
                }
            }
            jobTypes.put(jobTypeID,new JobType(jobTypeID,tasks));
            System.out.println("Added job type: "+jobTypeID);
            lineNumber++;
        }
    }

    private void parseStations(Scanner sc,int lineNumber){
        System.out.println("Parsing STATIONS at line "+lineNumber);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            System.out.println("Line "+lineNumber+": "+line);
            if (line.startsWith("(")){
                line=line.substring(1).trim();
            }

            if (line.endsWith("))")){
                line=line.substring(0,line.length()-1).trim();
            }

            if (line.endsWith(")")){
                line=line.substring(0,line.length()-1).trim();
            }
            if (line.isEmpty()){
                lineNumber++;
                continue;
            }

            String parts[]=line.split("\\s+");
            if (parts.length<4){
                errorMessages.add("Line "+lineNumber+": Insufficient data for station definition.");
                lineNumber++;
                continue;
            }
            String stationID=parts[0];
            int capacity;
            try{
                capacity=Integer.parseInt(parts[1]);
            }catch (NumberFormatException e){
                errorMessages.add("Line "+lineNumber+": Invalid capacity for station "+stationID);
                lineNumber++;
                continue;
            }
            boolean multiFlag="Y".equals(parts[2]);
            boolean fifoFlag="Y".equals(parts[3]);
            double stationSpeed=1.0;
            double speedVariation=0.0;
            Station station=new Station(stationID,capacity,multiFlag,fifoFlag,stationSpeed,speedVariation);

            if (stations.containsKey(stationID)){
                errorMessages.add("Line "+lineNumber+": Station "+stationID+" already declared.");
                continue;
            }

            int i=4;
            while (i<parts.length){
                String taskTypeID=parts[i];
                double speed=0.0;

                if (i+1<parts.length && parts[i+1].matches("-?\\d+(\\.\\d+)?")){
                    speed=Double.parseDouble(parts[i+1]);
                    i+=2;
                } else if (i==parts.length-1 && parts[i].matches("-?\\d+(\\.\\d+)?")) {
                    speedVariation=Double.parseDouble(parts[i]);
                    break;
                }
                if (!taskTypes.containsKey(taskTypeID)){
                    errorMessages.add("Line "+lineNumber+": TaskType "+taskTypeID+" not executed in any STATIONS");
                }
                station.addTaskType(taskTypeID,speed);
            }
            station.setSpeedVariation(speedVariation);

            stations.put(stationID,station);
            System.out.println("Added station: "+stationID);

            System.out.println("Station details: ");
            System.out.println(" Station ID: "+station.getStationID());
            System.out.println(" Max Capacity: "+station.getMaxCapacity());
            System.out.println(" MultiFlag: "+station.isMultiFlag());
            System.out.println(" FifoFlag: "+station.isFifoFlag());
            System.out.println(" Speed Variation: "+station.getSpeedVariation());


            for (String taskType : station.getTaskSpeeds().keySet()) {
                Double speed = station.getTaskSpeeds().get(taskType);
                System.out.println(" Task Type: " + taskType + ", Speed: " + speed);
            }
            lineNumber++;

        }
    }

    public void reportErrors(){
        if (!errorMessages.isEmpty()) {
            for (String error : errorMessages) {
                System.out.println(error);
            }
            System.exit(1);
        }
    }

    public Map<String, TaskType> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(Map<String, TaskType> taskTypes) {
        this.taskTypes = taskTypes;
    }

    public Map<String, JobType> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(Map<String, JobType> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public Map<String, Station> getStations() {
        return stations;
    }

    public void setStations(Map<String, Station> stations) {
        this.stations = stations;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
