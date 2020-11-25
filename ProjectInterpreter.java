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
        ArrayList<Token> symbolTable = new ArrayList<Token>();

        //clean the code by removing the comments first
        ArrayList<String> cleanedProgram = lexicalAnalyser.getCleanProgram(linesOfCode);
        
        //add it to the symbol table
        for (String loc : cleanedProgram){
            symbolTable.addAll(lexicalAnalyser.lexicalAnalysis(loc));
        }
        
        //print the cleaned code
        for (String s : cleanedProgram){
            System.out.println(s);
        }

        System.out.println();

        //print the tokens
        for (Token t : symbolTable){
            System.out.println(t.getLexeme() + " " + t.getType());
        }
    }
}
