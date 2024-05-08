import java.io.File;
import java.util.*;

public class WorkflowFileParser {
    Scanner sc=new Scanner(System.in);
    Map<String, TaskType> taskTypes = new HashMap<>();
    Map<String, JobType> jobTypes = new HashMap<>();
    Map<String, Station> stations = new HashMap<>();



    public void parse(File file){
        int lineNumber=0;
        while (sc.hasNextLine()){
            String line=sc.nextLine().trim();
            lineNumber++;
            if(line.isEmpty()) continue;
            try(Scanner sc=new Scanner(file)){
                if(line.startsWith("(TASK TYPES")){   //parse taskTypes
                    parseTaskTypes(sc,lineNumber);

                }else if(line.startsWith("(JOB TYPES")){   //parse jobTypes
                    parseJobTypes(sc,lineNumber);

                }else if(line.startsWith("(STATIONS")){   //parse stations
                    parseStations(sc,lineNumber);

                }
            }catch(Exception e){
                System.out.println("Error at line "+ lineNumber + ":" +e.getMessage());
            }
        }
    }
    private void parseTaskTypes(Scanner sc,int lineNumber){

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if (line.equals(")")) break;
            String[] tokens = line.split("\\s+");
            //write situations that may cause errors

            String defaultValue = "0";
            String taskTypeID = null;
            ArrayList listForTaskTypeID = new ArrayList<>();
            ArrayList listForTaskDefaultSize = new ArrayList<>();

            for (int i = 0; i < tokens.length; i += 2) {
                String defaultSize = defaultValue;
                if (tokens[i].matches("^[A-Za-z].*")) {
                    taskTypeID = tokens[i];
                    listForTaskTypeID.add(taskTypeID);
                } else {
                    i -= 3;
                    continue;
                }
                if (i + 1 < tokens.length && tokens[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                    defaultSize = tokens[i + 1];
                    listForTaskDefaultSize.add(defaultSize);
                } else {
                    defaultSize = defaultValue;
                    listForTaskDefaultSize.add(defaultSize);
                }
                if (defaultSize != null && Double.parseDouble(defaultSize) < 0) {
                    throw new IllegalArgumentException("TaskType " + taskTypeID + " has a negative task size at line " + lineNumber);
                }
                for (Object a : listForTaskTypeID) {
                    for (Object b : listForTaskTypeID) {
                        if (a.equals(b)) {
                            throw new IllegalArgumentException(a + "used before at line ");
                        }
                    }
                }
                if (!taskTypeID.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                    throw new IllegalArgumentException("Invalid taskTypeID " + taskTypeID + " at line " + lineNumber);
                }
                //taskTypes.put(taskID,new TaskType(taskID,defaultSize));    CREATE OBJECTS
            }
        }
    }
    private void parseJobTypes(Scanner sc,int lineNumber)throws Exception{

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals("))")) break;
            String[] parts = line.replaceAll("[()]","").split("\\s+");
            String jobTypeID=parts[0];
            if (!jobTypeID.matches("[A-Za-z][A-Za-z0-9_]*")){
                throw new Exception("Invalid jobID " + jobTypeID + " at line "+ lineNumber);
            }

            int i=1;
            while (i<parts.length){
                String taskTypeID=parts[i];
                Double size=null;
                if (i+1<parts.length && parts[i+1].matches("\\d+")){
                    size=Double.parseDouble(parts[i+1]);
                    if (size<=0){
                        throw new Exception("Task size must be positive at line "+ lineNumber);
                    }
                    i+=2;
                } else {
                    i++;
                }

                TaskType task=taskTypes.get(taskTypeID);

                if (task==null){
                    throw new Exception("Undefined taskTypeId " + taskTypeID + " referenced in job at line " + lineNumber);
                }
                if (task.getDefaultSize()==null && size==null){
                    throw new Exception("Task size for " + taskTypeID + " must be specified in job type at line " + lineNumber);
                }
            }

            //write situations that may cause errors
            //CREATE OBJECTS

        }
        }
    private void parseStations(Scanner sc,int lineNumber) throws Exception{

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals("))")) break;
            String[] parts = line.replaceAll("[()]","").split("\\s+");
            String stationID=parts[0];
            int maxCapacity=Integer.parseInt(parts[1]);


            Set<String> referencedTasks = new HashSet<>();

            for (int i=4;i<parts.length;i+=2){
                String taskTypeID=parts[i];
                referencedTasks.add(taskTypeID);
            }


            //write situations that may cause errors
            //CREATE OBJECTS

            checkForWhichTaskUsed(referencedTasks,lineNumber);
        }
    }

    private void checkForWhichTaskUsed(Set<String> referencedTasks,int lineNumber) throws Exception{
        for (String taskTypeID:taskTypes.keySet()){
         if (!referencedTasks.contains(taskTypeID)){
             throw new Exception(taskTypeID + " not executed in any stations");
         }
        }


    }
}
