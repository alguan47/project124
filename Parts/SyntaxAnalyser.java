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
    private boolean isClosedInfiniteArity;  //check for MKAY
    private boolean startProgram;   //check for HAI
    private boolean endProgram;     //check for KTHXBYE
    private boolean ifStart;        //check for O RLY?
    private boolean ifStatementFound;   //check for YA RLY
    private boolean ifEnd;      //check for OIC in IF ELSE 
    private boolean caseStart;  //check for WTF?
    private boolean caseStatementFound; //check for OMG
    private boolean caseEnd;    //check for OIC in case statement 

    public SyntaxAnalyser(){
        this.poppedSymbolTable = new ArrayList<Token>();
        this.isClosedInfiniteArity = true;
        this.startProgram = false;
        this.endProgram = false;
        this.ifStart = false;        
        this.ifStatementFound = false;
        this.ifEnd = true;      
        this.caseStart = false;  
        this.caseStatementFound = false; 
        this.caseEnd = true;    
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
            } else if (lexemeType.equals("Print operator")){
                return this.parsePrint();
            } else if (lexemeType.equals("Variable identifier")){
                return this.parseAssignment();
            } else if (lexemeType.equals("Program start keyword") || lexemeType.equals("Program end keyword")){
                return this.parseStartAndEnd();
            } else if (lexemeType.equals("Start of If block keyword") || lexemeType.equals("If keyword") || lexemeType.equals("Else if keyword") || lexemeType.equals("Else keyword")){
                return this.parseIfBlock();
            } else if (lexemeType.equals("End of (If or Case) keyword") && this.ifStart){
                return this.parseIfBlock();
            } else if (lexemeType.equals("Start of Case block keyword") || lexemeType.equals("Case statement keyword") || lexemeType.equals("Default case keyword") || lexemeType.equals("Break keyword")){
                return this.parseCaseBlock();
            } else if (lexemeType.equals("End of (If or Case) keyword") && this.caseStart){
                return this.parseCaseBlock();
            } else if (lexemeType.equals("End of (If or Case) keyword")) {  //we found an OIC without any O RLY? or WTF? 
                throw new Exception("Cannot end an if or case block if it wasn't opened.");
            } else if (lexemeType.equals("Declaration operator")) {
                return this.parseDeclaration();
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
           e.printStackTrace();
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
           e.printStackTrace();
        }

        return parseTree;
    }

    private ArrayList<String> parseNotKeyword(){
        ArrayList<String> parseTree = new ArrayList<String>();
        try{
            parseTree = this.popLexeme(parseTree, "Not operator"); //get NOT
            parseTree.addAll(this.parseFiniteBool());   //get a nested arithmetic operator OR a literal
        } catch(Exception e){
           e.printStackTrace();
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
            } else if (t.getType().equals("Comparison operator")) { //check for comparison too
                return this.parseComparison();
            }

            return new ArrayList<String>();
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
           e.printStackTrace();
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
           e.printStackTrace();
        }

        return parseTree;
    }

    //Concatenation
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
           e.printStackTrace();
        }

        return parseTree;
    }

    private ArrayList<String> parseConcatOp() throws Exception{
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            
            if (t.getType().equals("Troof") || t.getType().equals("Variable identifier") || t.getType().equals("Numbr") || t.getType().equals("Numbar") || t.getType().equals("Yarn")){
                this.symbolTable.remove(0);
                this.poppedSymbolTable.add(t);
                return new ArrayList<String>(Arrays.asList(t.getType()));
            } else  if (t.getType().equals("Arithmetic operator")){
                return this.parseArithmetic();
            } else if (t.getType().equals("Finite boolean operator") || t.getType().equals("Not operator")){
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
           e.printStackTrace();
        }
        return parseTree;
    }

    //User input
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
           e.printStackTrace();
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

    //Print statements
    private ArrayList<String> parsePrint() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Print operator")){
                return this.parsePrintKeyword();
            }
        } else throw new Exception("Inadequate arguments");
        return new ArrayList<String>();
    }

    private ArrayList<String> parsePrintKeyword(){ 
        ArrayList<String> parseTree = new ArrayList<String>();
          
        try{
            parseTree = this.popLexeme(parseTree, "Print operator"); //VISIBLE
            parseTree.addAll(this.parseConcatOp());     //just convert it to yarn later lol.
            parseTree.addAll(this.parsePrintInf());   //infinite arity

        } catch(Exception e){
           e.printStackTrace();
        }

        return parseTree;
    }

    private ArrayList<String> parsePrintInf() throws Exception{  //for infinity arity
        ArrayList<String> parseTree = new ArrayList<String>();
        if (this.symbolTable.size() != 0){
            parseTree.addAll(this.parseConcatOp()); //get the actual arguments
            parseTree.addAll(this.parsePrintInf()); //recursively get those remaining arguments
        }
        return parseTree;
    } 

    //Assignment operator
    private ArrayList<String> parseAssignment() throws Exception{
        
        if (this.symbolTable.size() != 0){
            if (this.symbolTable.get(0).getType().equals("Variable identifier")){
                return this.parseAssignmentKeyword();
            }
        } else throw new Exception("Inadequate arguments");
        return new ArrayList<String>();
    }

    private ArrayList<String> parseAssignmentKeyword(){ 
        ArrayList<String> parseTree = new ArrayList<String>();
          
        try{
            parseTree = this.popLexeme(parseTree, "Variable identifier"); //VISIBLE
            parseTree = this.popLexeme(parseTree, "Assignment operator");
            parseTree.addAll(this.parseConcatOp());     //get anything.

        } catch(Exception e){
           e.printStackTrace();
        }

        return parseTree;
    }

    //parse HAI and KTHXBYE
    private ArrayList<String> parseStartAndEnd(){
        ArrayList<String> parseTree = new ArrayList<String>();
        if (this.symbolTable.size() != 0 && this.symbolTable.size() >= 1){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Program start keyword")){
                this.startProgram = true;
                
                this.poppedSymbolTable.add(this.symbolTable.remove(0));
                parseTree.add(t.getType());
                
                if (this.symbolTable.size() != 0){  
                    //in HAI, we may include a version number but this irrelevant and just remove it if it exists or not
                    Token versionNumber = this.symbolTable.get(0);
                    this.poppedSymbolTable.add(this.symbolTable.remove(0));
                    parseTree.add(versionNumber.getType());
                }

            } else if (t.getType().equals("Program end keyword")){
                this.endProgram = true;
                this.poppedSymbolTable.add(this.symbolTable.remove(0));
                parseTree.add(t.getType());
            }
            
        } 

        return parseTree;
    }

    //parse start of if block
    private ArrayList<String> parseIfBlock() throws Exception{
        ArrayList<String> parseTree = new ArrayList<String>();
        if (this.symbolTable.size() != 0 && this.symbolTable.size() == 1){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Start of If block keyword")){
                if (!this.ifStart && this.ifEnd && !this.caseStart){
                    this.ifStart = true;
                    this.ifEnd = false;
                } else throw new Exception("Not allowed to start a new if block"); 
            } 
            
            if (t.getType().equals("If keyword")){
                if (this.ifStart && !this.ifEnd){
                    this.ifStatementFound = true;
                } else throw new Exception("Not allowed to have an if statement without O RLY?");
            } 
            
            if (t.getType().equals("Else if keyword")){
                if (!this.ifStatementFound) throw new Exception("Not allowed to have an else if statement without an accompanying if statement");
            }

            if (t.getType().equals("Else keyword")){
                if (!this.ifStatementFound) throw new Exception("Not allowed to have an else statement without an accompanying if statement");
            }
            
            if (t.getType().equals("End of (If or Case) keyword")){
                if (!this.ifStatementFound) throw new Exception("Not allowed to have a closing block keyword without an accompanying if statement");
                if (!this.ifStart) throw new Exception("Not allowed to have a closing block keyword without having an accompanying opening block keyword");
                this.ifEnd = true;
                this.ifStart = false;
                this.ifStatementFound = false;
            }
            parseTree.add(t.getType());
            this.poppedSymbolTable.add(this.symbolTable.remove(0));
        } else throw new Exception("O RLY?, YA RLY, MEBBE, NO WAI, and OIC must be alone in their respective lines");

        return parseTree;
    }

    private ArrayList<String> parseCaseStatement(Token t) throws Exception{
        ArrayList<String> parseTree = new ArrayList<String>();

        parseTree = this.popLexeme(parseTree, "Case statement keyword");
        parseTree.addAll(this.parseConcatOp());

        return parseTree;
    }

    private ArrayList<String> parseCaseBlock() throws Exception{
        ArrayList<String> parseTree = new ArrayList<String>();
        if (this.symbolTable.size() != 0){
            Token t = this.symbolTable.get(0);
            if (t.getType().equals("Case statement keyword")){
                //we need to elaborate on OMG
                if (t.getType().equals("Case statement keyword")){
                    if (this.caseStart && !this.caseEnd){
                        this.caseStatementFound = true;
                        parseTree.addAll(this.parseCaseStatement(t));
                    } else throw new Exception("Not allowed to have an case statement without WTF?");
                } else if (t.getType().equals("Break keyword")){
                    if (this.caseStart && !this.caseEnd && this.caseStatementFound){
                        parseTree.addAll(this.popLexeme(parseTree, "Break keyword"));
                        this.caseStatementFound = false;
                    } else throw new Exception("Break keyword only works for case statements");
                }
            }else{
                if (t.getType().equals("Start of Case block keyword")){
                    if (!this.caseStart && this.caseEnd && !this.ifStart){
                        this.caseStart = true;
                        this.caseEnd = false;
                    } else throw new Exception("Not allowed to start a new case block"); 
                } 
                
                if (t.getType().equals("Default case keyword")){
                    if (!this.caseStatementFound) throw new Exception("Not allowed to have a default statement without an accompanying case statement");
                }
                
                if (t.getType().equals("End of (If or Case) keyword")){
                    if (!this.caseStatementFound) throw new Exception("Not allowed to have a closing block keyword without an accompanying case statement");
                    if (!this.caseStart) throw new Exception("Not allowed to have a closing block keyword without having an accompanying case opening block keyword");
                    this.caseEnd = true;
                    this.caseStart = false;
                    this.caseStatementFound = false;
                }
                parseTree.add(t.getType());
                this.poppedSymbolTable.add(this.symbolTable.remove(0));
            }
        } else throw new Exception("WTF?, OMG, OMGWTF, and OIC must be alone in their respective lines");

        return parseTree;
    }

    private ArrayList<String> parseDeclaration() throws Exception{
        ArrayList<String> parseTree = new ArrayList<String>();
        Token t;

        parseTree = this.popLexeme(parseTree, "Declaration operator");
        parseTree.addAll(this.parseInputOp());

        if (this.symbolTable.size() != 0){
            t = this.symbolTable.get(0);
            if (t.getType().equals("Assignment during declaration operator")){
                parseTree = this.popLexeme(parseTree, "Assignment during declaration operator");

                //get the actual value

                parseTree.addAll(this.parseConcatOp());
            }
        }

        return parseTree;
    }


    public boolean syntaxAnalyse(ArrayList<Token> symbolTable){
        this.poppedSymbolTable.clear();
        this.symbolTable = symbolTable;
        try{

            ArrayList<String> parseTree = this.parse();
            // System.out.println(this.poppedSymbolTable.size());
            // System.out.println(parseTree.size());
            // System.out.println(this.poppedSymbolTable.size() != parseTree.size() );
            // System.out.println(this.symbolTable.size() != 0);
            // System.out.println(this.symbolTable.size());
            
            if (this.symbolTable.size() == 0) return true;      //it a blank line of code
            if (this.poppedSymbolTable.size() != parseTree.size()) return false;
            if (this.symbolTable.size() != 0) return false;
            if (!this.isClosedInfiniteArity) return false;
            if (!this.startProgram) return false;

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

    public boolean isEndProgram(){  //returns true if KTHXBYE has been read
        return this.endProgram;
    }

    public boolean isEndIfBlock(){  //returns true if there is no if-block that has been opened and wasn't closed (didn't meet OIC)
        return this.ifEnd;
    }

    public boolean isEndCaseBlock(){  //returns true if there is no case-block that has been opened and wasn't closed (didn't meet OIC)
        return this.ifEnd;
    }
}
