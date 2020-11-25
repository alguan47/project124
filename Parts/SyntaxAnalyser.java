package Parts;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

//add a HAI and KTHXBYE checker.

public class SyntaxAnalyser {
    private ArrayList<Token> symbolTable;
    private ArrayList<Token> poppedSymbolTable;
    private boolean isClosedInfiniteArity;

    public SyntaxAnalyser(){
        this.poppedSymbolTable = new ArrayList<Token>();
        this.isClosedInfiniteArity = true;
    }

    private ArrayList<String> parse() throws Exception{
        if (this.symbolTable.size() != 0){
            String lexemeType = this.symbolTable.get(0).getType();
            if (lexemeType.equals("Arithmetic operator")){
                return this.parseArithmetic();
            } else if (lexemeType.equals("Finite boolean operator")){
                return this.parseFiniteBool();
            } else if (lexemeType.equals("Infinite boolean operator")){
                return this.parseInfiniteBool();
            } else if (lexemeType.equals("Comparison operator")){
                return this.parseComparison();
            } else if (lexemeType.equals("String concatenation operator")){
                return this.parseConcat();
            } else if (lexemeType.equals("Input operator")){
                return this.parseInput();
            }
        } 
        return new ArrayList<String>();
    }

    //helper functions
    private ArrayList<String> popLexeme(ArrayList<String> parseTree, String type) throws Exception{
        Token keyWord; 
        if (this.symbolTable.size() != 0){
            keyWord = this.symbolTable.get(0);
            if (keyWord.getType().equals(type)){    //get the type of keyword we need
                this.poppedSymbolTable.add(this.symbolTable.remove(0));
                parseTree.add(keyWord.getType());
            } else {
                System.out.println(type);
                throw new Exception("Syntax error!");
            }
        } else throw new Exception("Inadequate arguments");

        return parseTree;
    }

    //ARITHMETIC OPERATIONS
    private ArrayList<String> parseArithmetic() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Arithmetic operator")){
                return this.parseArithmeticKeyword();
            } else {
                return this.parseArithmeticOp();
            }
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseArithmeticKeyword(){ 
        ArrayList<String> parseTree = new ArrayList<String>();
          
        try{
            parseTree = this.popLexeme(parseTree, "Arithmetic operator"); //get SUM OF, DIFF OF, ...
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseArithmeticOp() throws Exception{
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Numbr") || t.getType().equals("Numbar") || t.getType().equals("Variable identifier")){
                this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                return new ArrayList<String>(Arrays.asList(t.getType()));
            } else {
                throw new Exception("Invalid operand for an arithmetic operation");
            }
        } else throw new Exception("Inadequate arguments");
    }

    //FINITE BOOLEAN OPERATIONS (FINITE ARITY)
    private ArrayList<String> parseFiniteBool() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Finite boolean operator")){
                return this.parseFiniteBoolKeyword();
            } else if (this.symbolTable.get(0).getType().equals("Not operator")){
                return this.parseNotKeyword();
            } else {
                return this.parseFiniteBoolOp();
            }
        } else throw new Exception("Inadequate arguments");
      
    }

    private ArrayList<String> parseFiniteBoolKeyword(){
        ArrayList<String> parseTree = new ArrayList<String>();

        try{
            
            parseTree = this.popLexeme(parseTree, "Finite boolean operator"); //get finite bool ops such as "BOTH OF, EITHER OF"
            parseTree.addAll(this.parseFiniteBool());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseFiniteBool());   //get a nested arithmetic operator OR a literal

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseNotKeyword(){
        ArrayList<String> parseTree = new ArrayList<String>();
        try{
            parseTree = this.popLexeme(parseTree, "Not operator"); //get NOT
            parseTree.addAll(this.parseFiniteBool());   //get a nested arithmetic operator OR a literal
        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseFiniteBoolOp() throws Exception{
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Troof") || t.getType().equals("Variable identifier")){
                this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                return new ArrayList<String>(Arrays.asList(t.getType()));
            } else {
                throw new Exception("Invalid operand for a boolean operation");
            }
        } else throw new Exception("Inadequate arguments");
    }

    //INFINITE BOOLEAN OPERATIONS (INFINITE ARITY)
    private ArrayList<String> parseInfiniteBool() throws Exception{
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Infinite boolean operator")){
                return this.parseInfiniteBoolKeyword();
            } else {
                return this.parseFiniteBoolOp();
            }
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseInfiniteBoolKeyword(){
        //ALL OF <x> AN <y> .... MKAY
        // .... <- may refer to AN <x> .... MKAY
        ArrayList<String> parseTree = new ArrayList<String>();
        try{
            this.isClosedInfiniteArity = false;
            parseTree = this.popLexeme(parseTree, "Infinite boolean operator"); //get ALL OF, etc  
            parseTree.addAll(this.parseFiniteBool());   //get a nested finite boolean or a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseFiniteBool());   //get a nested arithmetic operator OR a literal
            parseTree.addAll(this.parseRecursiveInfiniteBool());  //we will get the "infinite" arity

        } catch(Exception e){
            e.printStackTrace();
        }

        return parseTree;
    }

    private ArrayList<String> parseRecursiveInfiniteBool() throws Exception{
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Arity keyword")){
                return this.parseRecursiveInfBoolKeyword(); 
            } else if (this.symbolTable.get(0).getType().equals("End of boolean operator")){
                Token t = this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                this.isClosedInfiniteArity = true;  //we need to close the infinity arity with MKAY.
                return new ArrayList<String>(Arrays.asList(t.getType()));
            }
            return new ArrayList<String>();
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseRecursiveInfBoolKeyword(){
        ArrayList<String> parseTree = new ArrayList<String>();
        try{
            
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseFiniteBool());   //get a nested finite boolean or a literal
            parseTree.addAll(this.parseRecursiveInfiniteBool());

        } catch(Exception e){
            e.printStackTrace();
        }

        return parseTree;
    }

    //COMPARISON OPERATIONS
    private boolean isRelational() throws Exception{

        if (this.symbolTable.size() != 0){
            for (Token t : this.symbolTable){
                if (t.getLexeme().equals("BIGGR OF") || t.getLexeme().equals("SMALLR OF")){
                    return true;
                }
            }
        } else throw new Exception("Inadequate arguments");
        
        return false;
    }

    private ArrayList<String> parseComparison() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.isRelational()){   //relational
                return this.parseRelational();
            } else {
                return this.parseAbsoluteComparison();  //not relational, absolute comparision (equal or not equal)
            }
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseRelational() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Comparison operator")){
                return this.parseRelationalKeyword();
            } else {
                return this.parseArithmeticOp();    //same as arithmetic (numbar, numbr, variable)
            }
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseRelationalKeyword(){ 
        //BOTH SAEM <x> AN BIGGR OF <x> AN <y>
        //BOTH SAEM <x> AN SMALLR OF <x> AN <y>
        //DIFFRINT <x> AN SMALLR OF <x> AN <y> 
        //DIFFRINT <x> AN BIGGR OF <x> AN <y> 

        ArrayList<String> parseTree = new ArrayList<String>();
              
        try{
            parseTree = this.popLexeme(parseTree, "Comparison operator"); //get BOTH SAME, DIFFRINT
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
     
            //we must check for two specific keywords : biggr of or smallr of
            if (this.symbolTable.size() != 0){
                Token keyword = this.symbolTable.get(0);
                if (keyword.getLexeme().equals("BIGGR OF") || keyword.getLexeme().equals("SMALLR OF")){    //get the "BIGGR OF" or "SMALLR OF"
                    this.poppedSymbolTable.add(this.symbolTable.remove(0));
                    parseTree.add(keyword.getType());
                } else {
                    throw new Exception("Syntax error!");
                }
            } else throw new Exception("Inadequate arguments");

            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseAbsoluteComparison(){ 
        //BOTH SAEM <x> AN <y>
        //BOTH SAEM <x> AN <y>
        //DIFFRINT <x> AN <y> 
        //DIFFRINT <x> AN <y> 

        ArrayList<String> parseTree = new ArrayList<String>();
   
        try{
            parseTree = this.popLexeme(parseTree, "Comparison operator"); //get BOTH SAME, DIFFRINT
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseArithmetic());   //get a nested arithmetic operator OR a literal

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseConcat() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("String concatenation operator")){   //concatenation
                return this.parseConcatKeyword();
            } else {
                return this.parseConcatOp();
            }
        } else throw new Exception("Inadequate arguments");
    }

    private ArrayList<String> parseConcatKeyword(){ 
        // SMOOSH str1 AN str2 AN ... AN strN

        ArrayList<String> parseTree = new ArrayList<String>();
              
        try{
            parseTree = this.popLexeme(parseTree, "String concatenation operator"); //get SMOOSH
            parseTree.addAll(this.parseConcatOp());   //get a nested arithmetic operator OR a literal
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseConcatOp());   //get a nested arithmetic operator OR a literal
            parseTree.addAll(this.parseConcatInf());    //concat infinite number of yarns

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseConcatOp() throws Exception{
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Troof") || t.getType().equals("Variable identifier") || t.getType().equals("Numbr") || t.getType().equals("Numbar")){
                this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                return new ArrayList<String>(Arrays.asList(t.getType()));
            } else  if (t.getType().equals("Arithmetic operator")){
                return this.parseArithmetic();
            } else if (t.getType().equals("Finite boolean operator")){
                return this.parseFiniteBool();
            } else if (t.getType().equals("Infinite boolean operator")){
                return this.parseInfiniteBool();
            } else if (t.getType().equals("Comparison operator")){
                return this.parseComparison();
            } else {
                throw new Exception("Invalid operand for a string operation");
            }
        } else throw new Exception("Inadequate arguments");

        //java.lang.Exception: Inadequate arguments
    }

    private ArrayList<String> parseConcatInf(){
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Arity keyword")){
                return this.parseRecursiveStringConcat(); 
            } 
        }
        return new ArrayList<String>();
    }

    private ArrayList<String> parseRecursiveStringConcat(){
        //AN <x> AN <y> ....
        ArrayList<String> parseTree = new ArrayList<String>();
              
        try{
            parseTree = this.popLexeme(parseTree, "Arity keyword"); //get AN
            parseTree.addAll(this.parseConcatOp());   //get a nested arithmetic operator OR a literal
            parseTree.addAll(this.parseConcatInf());    //concat infinite number of yarns

        } catch(Exception e){
            System.out.println(e);
        }
        return parseTree;
    }

    private ArrayList<String> parseInput() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Input operator")){
                return this.parseInputKeyword();
            }
        } else throw new Exception("Inadequate arguments");
        return new ArrayList<String>();
    }

    private ArrayList<String> parseInputKeyword(){ 
        ArrayList<String> parseTree = new ArrayList<String>();
          
        try{
            parseTree = this.popLexeme(parseTree, "Input operator"); //GIMMEH
            parseTree.addAll(this.parseInputOp());   //get a nested arithmetic operator OR a literal

        } catch(Exception e){
            System.out.println(e);
        }

        return parseTree;
    }

    private ArrayList<String> parseInputOp() throws Exception{
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Variable identifier")){
                this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                return new ArrayList<String>(Arrays.asList(t.getType()));
            } else {
                throw new Exception("Invalid operand for an input operation");
            }
        } else throw new Exception("Inadequate arguments");
    }

    public boolean syntaxAnalyse(ArrayList<Token> symbolTable){
        this.poppedSymbolTable.clear();
        int symbolTableSize = symbolTable.size();
        this.symbolTable = symbolTable;
        try{

            ArrayList<String> parseTree = this.parse();
            
            System.out.println(this.poppedSymbolTable.size() != parseTree.size() );
            System.out.println(this.symbolTable.size() != 0);
            System.out.println(this.poppedSymbolTable.size() + " " + symbolTableSize);
            System.out.println(this.symbolTable.size());
            
            if (this.poppedSymbolTable.size() != parseTree.size()) return false;
            if (this.symbolTable.size() != 0) return false;
            if (!this.isClosedInfiniteArity) return false;

            for (String s : parseTree){
                System.out.println(s);
            }
    
            for (int i = 0; i < parseTree.size(); i++){
                if (!parseTree.get(i).equals(this.poppedSymbolTable.get(i).getType())){
                    return false;
                }
            }
            
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
