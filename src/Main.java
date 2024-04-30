import java.io.File;

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