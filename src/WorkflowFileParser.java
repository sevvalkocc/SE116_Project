import java.io.File;
import java.util.*;

public class WorkflowFileParser {
    Scanner sc=new Scanner(System.in);

    Map<String, TaskType> taskTypes = new HashMap<>();
    ArrayList<TaskType> taskTypeList;
    ArrayList <String> errorMessages=new ArrayList<>();


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
            String updatedLine=line.replaceAll("TASKTYPES\\s+","");
            lineNumber++;
            if (updatedLine.equals(")")) break;
            String[] tokens = updatedLine.split("\\s+");

            double defaultSize;
            String taskTypeID;
            ArrayList<String> listForTaskTypeID = new ArrayList<>();
            ArrayList<Double> listForTaskDefaultSize = new ArrayList<>();

            for (int i = 0; i < tokens.length; i += 2) {

                if (tokens[i].matches("^[A-Za-z].*")) {
                    taskTypeID = tokens[i];
                    listForTaskTypeID.add(taskTypeID);
                } else {
                    i -= 3;
                    continue;
                }
                if (i + 1 < tokens.length && tokens[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                    defaultSize = Double.parseDouble(tokens[i + 1]);
                    listForTaskDefaultSize.add(defaultSize);
                } else {
                    defaultSize = 0;
                    listForTaskDefaultSize.add(defaultSize);
                }
                if ( defaultSize < 0) {
                    errorMessages.add("TaskType " + taskTypeID + " has a negative task size at line " + lineNumber);
                    //throw new IllegalArgumentException("TaskType " + taskTypeID + " has a negative task size at line " + lineNumber);
                }

                for (int k=0;k<listForTaskTypeID.size();k++){
                    for (int j=k+1;j<listForTaskTypeID.size();j++) {
                        if(listForTaskTypeID.get(k).equals(listForTaskTypeID.get(j)))
                            errorMessages.add(listForTaskTypeID.get(k) + " declared before ");
                            //throw new IllegalArgumentException(listForTaskTypeID.get(k) + " declared before ");
                    }
                }
                if (!taskTypeID.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                    errorMessages.add("Invalid taskTypeID " + taskTypeID + " at line " + lineNumber);
                    //throw new IllegalArgumentException("Invalid taskTypeID " + taskTypeID + " at line " + lineNumber);
                }

                TaskType task=new TaskType(taskTypeID,defaultSize);
                taskTypeList.add(task);

            }
        }
    }
    private void parseJobTypes(Scanner sc,int lineNumber){

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            String updatedLine=line.replaceAll("JOBTYPES","");
            lineNumber++;
            if(updatedLine.equals("))")) break;
            String[] parts = updatedLine.replaceAll("[()]","").split("\\s+");
            ArrayList<String> forJobTypeID=new ArrayList<>();
            String jobTypeID=parts[0];
            forJobTypeID.add(parts[0]);
            for (int i=1;i<parts.length;i+=2){
                String taskTypeID=parts[i];
                Double size=null;
                if (i+1<parts.length && parts[i+1].matches("\\d+")){
                    size=Double.parseDouble(parts[i+1]);
                    if (size<=0){
                        errorMessages.add("Task size must be positive at line "+ lineNumber);
                        //throw new Exception("Task size must be positive at line "+ lineNumber);
                    }
                    i+=2;
                }else{
                    i++;
                }
                if (!jobTypeID.matches("[A-Za-z][A-Za-z0-9_]*")){
                    errorMessages.add("Invalid jobID " + jobTypeID + " at line "+ lineNumber);
                    //throw new Exception("Invalid jobID " + jobTypeID + " at line "+ lineNumber);
                }

                TaskType task=taskTypes.get(taskTypeID);

                if (task==null){
                    errorMessages.add("Undefined taskTypeId " + taskTypeID + " referenced in job at line " + lineNumber);
                    //throw new Exception("Undefined taskTypeId " + taskTypeID + " referenced in job at line " + lineNumber);
                }
                if (task.getDefaultSize()==0 && size==null){
                    errorMessages.add("Task size for " + taskTypeID + " must be specified in job type at line " + lineNumber);
                    //throw new Exception("Task size for " + taskTypeID + " must be specified in job type at line " + lineNumber);
                }

            }
            for (int i=0;i<forJobTypeID.size();i++){
                for (int j=i+1;j<forJobTypeID.size();j++){
                    if (forJobTypeID.get(i).equals(forJobTypeID.get(j))){
                        errorMessages.add(forJobTypeID.get(i)+" already declared in line: "+lineNumber);
                    }
                }
            }
        }
    }

    private void parseStations(Scanner sc,int lineNumber){

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            String updatedLine=line.replaceAll("STATIONS","");
            lineNumber++;
            if(updatedLine.equals("))")) break;
            String[] parts = updatedLine.replaceAll("[()]","").split("\\s+");
            String stationID=parts[0];
            int maxCapacity=Integer.parseInt(parts[1]);

            Set<String> referencedTasks = new HashSet<>();
            ArrayList<String> forTaskSizeInStations=new ArrayList<>();
            String taskSizeInStation;

            for (int i=4;i<parts.length;i+=2){
                if (parts[i].startsWith("[A-Za-z][A-Za-z0-9_]*")) {
                    String taskTypeID = parts[i];
                    referencedTasks.add(taskTypeID);
                    taskSizeInStation=parts[i+1];
                    forTaskSizeInStations.add(taskSizeInStation);
                }else{
                    double stationSpeed=Double.parseDouble(parts[i]);
                }
            }

            checkForWhichTaskUsed(referencedTasks);

            //write situations that may cause errors
            //CREATE OBJECTS

        }
    }

    private void checkForWhichTaskUsed(Set<String> referencedTasks){
        for (String taskTypeID:taskTypes.keySet()){
         if (!referencedTasks.contains(taskTypeID)){
             errorMessages.add(taskTypeID + " not executed in any stations");
             //throw new Exception(taskTypeID + " not executed in any stations");
         }
        }


    }

    public void displayErrors(ArrayList<String> errorMessages){
        for(String error:errorMessages){
            System.out.println(error);
        }
    }

}
