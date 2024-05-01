import java.io.File;
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
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            lineNumber++;
            if(line.equals(")")) break;
            String[] tokens = line.split("\\s+");
            //write situations that may cause errors
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
