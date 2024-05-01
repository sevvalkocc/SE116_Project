import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please write in this format: java Main <workflow_file> <job_file>");
            return;
        }
            String workflowFileName;
            String jobFileName;

            if(args[0].endsWith(".job")){
                jobFileName=args[0];
                workflowFileName=args[1];
            }else{
                jobFileName=args[1];
                workflowFileName=args[0];
            }
            checkFile(workflowFileName);
            checkFile(jobFileName);

            File workflowFile=new File(workflowFileName);
            WorkflowFileParser workParser=new WorkflowFileParser();

            try{
                workParser.parse(workflowFile);
            }catch(Exception e){
                System.out.println("An error occurred: "+e.getMessage());
            }

            File jobFile=new File(jobFileName);
            JobFileParser jobParser=new JobFileParser();
            try{
                jobParser.parse(jobFile);
            }catch(Exception e){
                System.out.println("An error occurred: "+e.getMessage());
            }


    }
        private static boolean checkFile(String fileName){
            File file=new File(fileName);
            if(!file.exists()){
                System.out.println("Error: File "+ fileName +" does not exist");
                return false;
            }
            if (!file.canRead()) {
                System.out.println("Error: File " + fileName + " is not accessible.");
                return false;
            }
            System.out.println("File " + fileName + " exists and is accessible.");
            return true;
        }


}