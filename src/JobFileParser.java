import java.io.File;
import java.util.Scanner;

public class JobFileParser {
    Scanner sc=new Scanner(System.in);

    public void parse(File file){
        int lineNumber=0;
        while(sc.hasNextLine()){
            String line=sc.nextLine().trim();
            lineNumber++;

        }
    }
}
