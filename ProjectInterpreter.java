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
        ArrayList<Token> listOfLexemes;
        String loc;

        //add it to the symbol table
        boolean noErr = true;
        for (int i = 0 ; i < cleanedProgram.size(); i++){
            loc = cleanedProgram.get(i);
            listOfLexemes = lexicalAnalyser.lexicalAnalysis(loc);
           
            symbolTable.addAll(listOfLexemes);
            if (!syntaxAnalyser.syntaxAnalyse(listOfLexemes)){
                System.out.println(loc);
                System.out.println("Syntax error at line " + (i+1));
                noErr = false;
                break;
            }
        }

        if (!syntaxAnalyser.isEndProgram() && noErr){
            System.out.println("KTHXBYE not found; Syntax error at line " + cleanedProgram.size());
        }

        if (!syntaxAnalyser.isEndIfBlock() && noErr){
            System.out.println("OIC not found; Syntax error!");
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
