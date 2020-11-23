import java.util.ArrayList;
import java.util.HashMap;

import FileHandlers.FileHandler;
import Parts.LexicalAnalyser;
import Parts.Token;

public class ProjectInterpreter{
    public static void main(String[] args) throws Exception{
        FileHandler fh = new FileHandler();        
        ArrayList<String> linesOfCode = fh.openFile("./TestFiles/io.lol");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
        ArrayList<Token> symbolTable = lexicalAnalyser.lexicalAnalysis(linesOfCode);
        ArrayList<String> cleanedProgram = lexicalAnalyser.getCleanProgram();

        for (String s : cleanedProgram){
            System.out.println(s);
        }

        System.out.println();

        for (Token t : symbolTable){
            System.out.println(t.getLexeme() + " " + t.getType());
        }
    }
}
