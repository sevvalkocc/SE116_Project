import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WorkflowFileParser {
    Scanner sc=new Scanner(System.in);


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
                System.out.println("Error at line "+lineNumber+":"+e.getMessage());
            }
        }
    }
    protected void parseTaskTypes(Scanner sc,int lineNumber) {
        Map<String, Integer> taskTypeCounts = new HashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals(")")) break;
            String[] tokens = line.split("\\s+");
            //write situations that may cause errors

            String defaultValue = "0";
            String taskTypeID=null;

            for (int i = 0; i < tokens.length; i+=2) {
                String value = defaultValue;
                if (tokens[i].matches("^[A-Za-z].*")) {
                    taskTypeID = tokens[i];
                } else {
                    i -= 3;
                    continue;
                }
                if (i + 1 < tokens.length && tokens[i + 1].matches("-?\\d+(\\.\\d+)?")) {
                    value = tokens[i + 1];
                }
                if ( value!=null && Integer.parseInt(value)<0 ) {
                    throw new IllegalArgumentException("TaskType " + taskTypeID + " has a negative task size at line " + lineNumber);
                }
                if (!taskTypeID.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                    throw new IllegalArgumentException("Invalid taskTypeID " + taskTypeID + " at line " + lineNumber);
                }
                if (taskTypeCounts.containsKey(taskTypeID)) {
                    throw new IllegalArgumentException( taskTypeID + "used before at line " + lineNumber);
                }
            }


        }
    }
    protected void parseJobTypes(Scanner sc,int lineNumber){
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals("))")) break;
            String[] parts = line.split("\\s+");
            //write situations that may cause errors

        }
        }
    protected void parseStations(Scanner sc,int lineNumber){
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals("))")) break;
            //write situations that may cause errors

        }
    }
}
