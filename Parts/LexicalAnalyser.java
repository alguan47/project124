package Parts;

import java.util.ArrayList;
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
    "O RLY", "YA RLY","NO WAI","IM IN YR","IM OUTTA YR","AN","A","R"};
    private String[] numbar = {"(-)?[0-9]+.[0-9]+"};
    private String[] numbr = {"(-)?[0-9]+"};
    private String[] yarn = {"\"[^\"]+\""};
    private String[] troof = {"(WIN|FAIL)"};
    private String[] typeLiteral = {"(NOOB|NUMBR|NUMBAR|YARN|TROOF|TYPE)"};
    private String[] variableIdentifier = {"[A-Za-z][A-Za-z0-9_]*"};
    
    private ArrayList<Token> symbolTable;
    private ArrayList<String> program;
    private String loc;
    private boolean multilineComment;

    public LexicalAnalyser(){
        this.symbolTable = new ArrayList<Token>();
        this.program = new ArrayList<String>();
        this.multilineComment = false;
    }

    private ArrayList<String> removeComment(String code){
        ArrayList<String> commentStr = new ArrayList<String>();
        //why ArrayList?
        //because we need to get two strings: the one that has the keyword BTW,TLDR,OBTW to it because we need to get the lexeme
        //and we need another string that has been no comments.

        if (!this.multilineComment){    //we're checking if we are at the start of the multiline comment
            //+4 and +3 because I need to include the keywords BTW or OBTW in the string    
            if (code.contains("OBTW")){
                int beginIndex = code.indexOf("OBTW");
                commentStr.add(code.substring(0,beginIndex+4));
                commentStr.add(code.substring(0,beginIndex));
            }else if (code.contains("BTW")){
                int beginIndex = code.indexOf("BTW");
                commentStr.add(code.substring(0,beginIndex+3));
                commentStr.add(code.substring(0,beginIndex));
            } else {
                commentStr.add(code);
                commentStr.add(code);
            }
        } else {
            //check if we meet TLDR (end of multiline comment)
            if (code.contains("TLDR")){
                commentStr.add(code);
                commentStr.add("");
            } else {
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
                    this.symbolTable.add(new Token(lexeme, type));

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
                comment = this.removeComment(loc);
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

