package FileHandlers;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FileHandler {

    public ArrayList<String> openFile(String filename){
        //read the file using the filename
        ArrayList<String> linesOfCode = new ArrayList<String>();
        try{
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine()){
                String loc = sc.nextLine();
                linesOfCode.add(loc);
            }

            sc.close();
            
            
        }catch(FileNotFoundException f){
            System.out.println("File not found!");
        }
        return linesOfCode;
    }
}
