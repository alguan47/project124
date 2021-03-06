package Parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class LexicalAnalyser {
    //for doing the actual regex
    private Pattern pattern;
    private Matcher matcher;
    


    //regex
    private String[] keywords = {"HAI", "KTHXBYE", "BTW", "OBTW", "TLDR", "ITZ", "NOT",
    "DIFFRINT", "SMOOSH", "MAEK", "I HAS A","IS NOW A", "VISIBLE", "GIMMEH", "MEBBE",  "OIC", "WTF", "OMG", 
    "OMGWTF", "UPPIN", "NERFIN","YR", "TIL", "WILE", "GTFO", "SUM OF", "DIFF OF", "PRODUKT OF", "QUOSHUNT OF", 
    "MOD OF", "BIGGR OF", "SMALLR OF","WON OF", "BOTH OF", "EITHER OF","ANY OF", "ALL OF", "BOTH SAEM",
    "O RLY", "YA RLY","NO WAI","IM IN YR","IM OUTTA YR","AN","A","R", "MKAY"};
    private String[] numbar = {"(-)?[0-9]+\\.[0-9]+"};
    private String[] numbr = {"(-)?[0-9]+"};
    private String[] yarn = {"\"[^\"]+\""};
    private String[] troof = {"(WIN|FAIL)"};
    private String[] typeLiteral = {"(NOOB|NUMBR|NUMBAR|YARN|TROOF|TYPE)"};
    private String[] variableIdentifier = {"[A-Za-z][A-Za-z0-9_]*"};
    
    private String[] arithmeticOp = {"SUM OF", "DIFF OF", "PRODUKT OF", "QUOSHUNT OF", "MOD OF", "BIGGR OF", "SMALLR OF"};
    private String[] finiteBoolean = {"BOTH OF", "EITHER OF", "WON OF"};
    private String[] notOp = {"NOT"};
    private String[] infiniteBoolean = {"ALL OF", "ANY OF"};
    private String[] booleanEnd = {"MKAY"};
    private String[] comparsionOp = {"BOTH SAEM", "DIFFRINT"};
    private String[] concat = {"SMOOSH"};
    private String[] print = {"VISIBLE"};
    private String[] input = {"GIMMEH"};
    private String[] ifStart = {"O RLY?"};
    private String[] ifKey = {"YA RLY"};
    private String[] elifKey = {"MEBBE"};
    private String[] elseKey = {"NO WAI"};
    private String[] flowEnd = {"OIC"};
    private String[] caseStart = {"WTF?"};
    private String[] caseStatement = {"OMG"};
    private String[] caseDefault = {"OMGWTF"};
    private String[] and = {"AN"};
    private String[] start = {"HAI"};
    private String[] end = {"KTHXBYE"};
    private String[] assignment = {"R"};
    private String[] declaration = {"I HAS A"};
    private String[] declareAssign = {"ITZ"};
    private String[] breakStatement = {"GTFO"};

    private ArrayList<Token> symbolTable;
    private ArrayList<String> program;
    private String loc;
    private boolean multilineComment;

    public LexicalAnalyser(){
        this.symbolTable = new ArrayList<Token>();
        this.program = new ArrayList<String>();
        this.multilineComment = false;
    }

    private ArrayList<String> removeUnnecessaryCode(String code){
        ArrayList<String> commentStr = new ArrayList<String>();
        //why ArrayList?
        //because we need to get two strings: the one that has the keyword BTW,TLDR,OBTW to it because we need to get the lexeme (to check )
        //and we need another string that has been no comments.

        if (!this.multilineComment){    //we're checking if we are at the start of the multiline comment    
            if (code.contains("OBTW")){
                int obtwIndex = code.indexOf("OBTW");
                commentStr.add(code.substring(0,obtwIndex+"OBTW".length()));
                commentStr.add(code.substring(0,obtwIndex));
            }else if (code.contains("BTW")){
                int btwIndex = code.indexOf("BTW");
                commentStr.add(code.substring(0,btwIndex+"BTW".length()));
                commentStr.add(code.substring(0,btwIndex));
            } else {
                commentStr.add(code);
                commentStr.add(code);
            }
        } else {    //check for the end of a multiline comment OR HAI version number.
            //check if we meet TLDR (end of multiline comment)
            if (code.contains("TLDR")){
                commentStr.add(code);
                commentStr.add("");
            }else {
                commentStr.add(" ");
                commentStr.add(" ");
            }
        }

        return commentStr;
    }

    private ArrayList<String> checkRegex(String[] regexArr){
        boolean found;
        ArrayList<String> lexemes = new ArrayList<String>();
        boolean isLiteralRegex = regexArr.equals(numbar) || regexArr.equals(numbr) || regexArr.equals(yarn) || regexArr.equals(troof) || regexArr.equals(typeLiteral);
        boolean identifyVar = regexArr.equals(variableIdentifier);
        String matched;

        for (String regex: regexArr){
            if (isLiteralRegex){
                if (identifyVar){
                    pattern = Pattern.compile( "([^A-Za-z0-9_]+?" + regex + " " + ")" );
                } else {
                    pattern = Pattern.compile("("+regex+")");
                }
            } else {
                pattern = Pattern.compile("\\b("+regex+")\\b");
            }
            matcher = pattern.matcher(this.loc);    
            found = matcher.find();
            
            //handle comments of the code
            if (found){
                matched = matcher.group(1);
                if (matched.equals("OBTW") && !this.multilineComment){
                    lexemes.add("OBTW");
                    this.loc = this.loc.replaceAll(matched, " ");
                    this.multilineComment = true;
                } else if (matched.equals("TLDR") && this.multilineComment){
                    this.multilineComment = false;
                }
            }

            //start finding the lexemes if we found something and we're not in the middle of a multiline comment
            while (found && !this.multilineComment){
                //java sucks: you need to escape the parenthesis to make any regex that has a "( )" work.
                matched = matcher.group(1);


                if (!matched.equals("WTF") && !matched.equals("O RLY")){
                    lexemes.add(matched);
                }

                //replace these characters because when we use regex in java, we need to escape those characters to use them as an actual character
                //this only works if they are yarns. doesn't work with keywords because there's no way around to escape those characters
                matched = matched.replaceAll("\\(", "\\\\(");
                matched = matched.replaceAll("\\)", "\\\\)");
                matched = matched.replaceAll("\\[", "\\\\[");
                matched = matched.replaceAll("\\]", "\\\\]");
                matched = matched.replaceAll("\\{", "\\\\{");
                matched = matched.replaceAll("\\}", "\\\\}");
                matched = matched.replaceAll("\\+", "\\\\+");
                matched = matched.replaceAll("\\*", "\\\\*");
                matched = matched.replaceAll("\\^", "\\\\^");
                matched = matched.replaceAll("\\?", "\\\\?");  
        
                //edge case: since WTF? or O RLY? has a question mark, Java interprets it as the ? for regex not as a string, escaping it does nothing!
                if (matched.equals("WTF") || matched.equals("O RLY")){
                    matched = matched + "\\?";
                }

                //responsible for removing the valid lexemes inside the loc so we can repeatedly apply the lexical analysis on it
                                
                if (identifyVar){
                    //keep running into a problem about identifiers that have some part of another identifiers
                    //example, if the regex is "flag", it gets the word "anotherflag" too
                    //can't think of a regex for that
                    //also, it doesnt work with numbers
                    String[] wordSplit = this.loc.split("\\s");
                    for (int i = 0 ; i < wordSplit.length; i++){
                        if (wordSplit[i].equals(matched)){
                            wordSplit[i] = "";
                        }
                    }
                    this.loc = String.join(" ", wordSplit);
                } else {
                    this.loc = this.loc.replaceAll(matched, " ");
                }
                
                //edge case for WTF? and O RLY?
                if (matched.equals("WTF\\?") || matched.equals("O RLY\\?")){
                    matched = matched.substring(0, matched.indexOf("\\")) + "?";
                    lexemes.add(matched);
                }


                //check again if we can find more valid regex in the loc
                found = matcher.find();
            }
        }
        
        return lexemes;
    }

    private boolean checkNotEmpty(){
        String[] emptyStr = this.loc.split("");
        for (String str : emptyStr){
            if (str.matches("[^\\s]")){
                return true;
            }
        }
        return false;
    }

    private int findFirstLetter(){
        String[] emptyStr = this.loc.split("");
        for (int i = 0 ; i < emptyStr.length; i++){
            if (emptyStr[i].matches("[^\\s]")){
                return i;
            }
        }
        
        return -1;
    }

    private void classifyKeyword(String lexeme){
        // System.out.println(lexeme);
        if (Arrays.asList(arithmeticOp).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Arithmetic operator"));
        } else if (Arrays.asList(finiteBoolean).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Finite boolean operator"));
        } else if (Arrays.asList(notOp).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Not operator"));
        } else if (Arrays.asList(infiniteBoolean).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Infinite boolean operator"));
        } else if (Arrays.asList(comparsionOp).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Comparison operator"));
        } else if (Arrays.asList(booleanEnd).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "End of boolean operator"));
        } else if (Arrays.asList(print).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Print operator"));
        } else if (Arrays.asList(input).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Input operator"));
        } else if (Arrays.asList(concat).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "String concatenation operator"));
        } else if (Arrays.asList(ifStart).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Start of If block keyword"));
        } else if (Arrays.asList(ifKey).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "If keyword"));
        } else if (Arrays.asList(elifKey).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Else if keyword"));
        } else if (Arrays.asList(elseKey).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Else keyword"));
        } else if (Arrays.asList(flowEnd).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "End of (If or Case) keyword"));
        } else if (Arrays.asList(caseStart).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Start of Case block keyword"));
        } else if (Arrays.asList(caseStatement).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Case statement keyword"));
        } else if (Arrays.asList(caseDefault).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Default case keyword"));
        } else if (Arrays.asList(and).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Arity keyword"));
        } else if (Arrays.asList(start).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Program start keyword"));
        } else if (Arrays.asList(end).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Program end keyword"));
        } else if (Arrays.asList(assignment).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Assignment operator"));
        } else if (Arrays.asList(declaration).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Declaration operator"));
        } else if (Arrays.asList(declareAssign).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Assignment during declaration operator"));
        } else if (Arrays.asList(breakStatement).contains(lexeme)){
            this.symbolTable.add(new Token(lexeme, "Break keyword"));
        } else {
            this.symbolTable.add(new Token(lexeme, "Keyword"));
        }
    }

    private boolean addLexemeToSymbolTable(ArrayList<String> lexemes, String type){
        String substr;
        int startIndex = 0;
        for (String lexeme : lexemes){
            if (lexeme.length() <= this.loc.length()){
                //find the first occurence of a non-whitespace char
                startIndex = this.findFirstLetter();
                
                // find the substring that contains the lexeme
                if (lexeme.length()+startIndex <= this.loc.length()){
                    substr = this.loc.substring(startIndex, lexeme.length()+startIndex);
                } else {
                    substr = this.loc.substring(startIndex);
                }

                //we've found the lexeme
                if (substr.equals(lexeme)){
                    if (type.equals("Keyword")){
                        this.classifyKeyword(lexeme);
                    } else {
                        this.symbolTable.add(new Token(lexeme, type));
                    }
                        

                    //modify the loc and remove the lexeme
                    if (substr.length()+1+startIndex >= this.loc.length()){
                        this.loc = "";
                    } else {
                        this.loc = this.loc.substring(substr.length()+1+startIndex);
                    }
                    // System.out.println(substr);
                    lexemes.remove(lexeme);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateSymbolTable(ArrayList<String> keywordLexeme,ArrayList<String> yarnLexeme,ArrayList<String> troofLexeme,ArrayList<String> typeLexeme,ArrayList<String> variableLexeme,ArrayList<String> numbarLexeme,ArrayList<String> numbrLexeme){
        boolean lexemeFound;
        String mult;
        while (this.checkNotEmpty()){
            mult = this.loc;
            lexemeFound = this.addLexemeToSymbolTable(troofLexeme, "Troof");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(typeLexeme, "Type");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(keywordLexeme, "Keyword");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(yarnLexeme, "Yarn");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(variableLexeme, "Variable identifier");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(numbarLexeme, "Numbar");
            if (!lexemeFound) lexemeFound = this.addLexemeToSymbolTable(numbrLexeme, "Numbr");   
          

            if (this.loc.equals(mult)){
                // System.out.println(this.loc);
                break;
            }
        }
    }

    public ArrayList<Token> lexicalAnalysis(String loc) throws Exception {
        ArrayList<String> keywordLexeme;
        ArrayList<String> numbarLexeme;
        ArrayList<String> numbrLexeme;
        ArrayList<String> yarnLexeme;
        ArrayList<String> troofLexeme;
        ArrayList<String> typeLexeme;
        ArrayList<String> variableLexeme;
 

        this.symbolTable.clear();

        try{
    
            //cleanLOC should contain the "clean" version of the line of code (no comments or anything)
            this.loc = loc;
            // System.out.println(this.loc);

            //get the Token from the lines of code                
            troofLexeme = this.checkRegex(troof);
            typeLexeme = this.checkRegex(typeLiteral);
            keywordLexeme = this.checkRegex(keywords);
            yarnLexeme = this.checkRegex(yarn);
            variableLexeme = this.checkRegex(variableIdentifier);
            numbarLexeme = this.checkRegex(numbar);
            numbrLexeme = this.checkRegex(numbr);    
            
            //the string should be empty after removing all of the valid lexemes, if not, there's an error in the lexical analysis
           
            if (this.checkNotEmpty() && !this.multilineComment){
                throw new Exception("Lexical analysis error");
            }
            
            //add the tokens to the symbol table

            this.loc = loc;
            this.updateSymbolTable(keywordLexeme, yarnLexeme, troofLexeme, typeLexeme, variableLexeme, numbarLexeme, numbrLexeme);

            
        } catch(Exception e){
            e.printStackTrace();
        }

        if (this.multilineComment) throw new Exception("Lexical Analysis error!");

        return this.symbolTable;
    }

    private void cleanCode(ArrayList<String> code){
        String clean;
        ArrayList<String> comment;
        try{
            for (String loc : code){
                comment = this.removeUnnecessaryCode(loc.trim());
                this.loc = comment.get(0);
                clean = comment.get(1);
                this.checkRegex(keywords);


                if (this.multilineComment) continue;
    
                this.program.add(clean);
            }
    
            if (this.multilineComment) throw new Exception("Preprocessing code has run into a problem");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public ArrayList<String> getCleanProgram(ArrayList<String> code){

        this.cleanCode(code);
        
        return this.program;
    }

    public void resetProgram(){
        this.program.clear();
    }

}

