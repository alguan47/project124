import java.util.ArrayList;

import FileHandlers.FileHandler;
import Parts.LexicalAnalyser;
import Parts.SyntaxAnalyser;
import Parts.Token;

public class ProjectInterpreter{
    public static void main(String[] args) throws Exception{
        FileHandler fh = new FileHandler();        
        ArrayList<String> linesOfCode = fh.openFile("./TestFiles/test.lol");
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser();
        ArrayList<Token> symbolTable = new ArrayList<Token>();

        //clean the code by removing the comments first
        ArrayList<String> cleanedProgram = lexicalAnalyser.getCleanProgram(linesOfCode);
        ArrayList<Token> newSymbolTable;

        //add it to the symbol table
        for (String loc : cleanedProgram){
            newSymbolTable = lexicalAnalyser.lexicalAnalysis(loc);
            symbolTable.addAll(newSymbolTable);
            System.out.println("Syntactically correct? " + syntaxAnalyser.syntaxAnalyse(newSymbolTable));
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
