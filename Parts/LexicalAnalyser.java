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
    "DIFFRINT", "SMOOSH", "MAEK", "I HAS A","IS NOW A", "VISIBLE", "GIMMEH", "MEBBE",  "OIC", "WTF?", "OMG", 
    "OMGWTF", "UPPIN", "NERFIN","YR", "TIL", "WILE", "SUM OF", "DIFF OF", "PRODUKT OF", "QUOSHUNT OF", 
    "MOD OF", "BIGGR OF", "SMALLR OF","WON OF", "BOTH OF", "EITHER OF","ANY OF", "ALL OF", "BOTH SAEM",
    "O RLY?", "YA RLY","NO WAI","IM IN YR","IM OUTTA YR","AN","A","R"};
    private String[] numbar = {"(-)?[0-9]+.[0-9]+"};
    private String[] numbr = {"(-)?[0-9]+"};
    private String[] yarn = {"\"[^\"]+\""};
    private String[] troof = {"(WIN|FAIL)"};
    private String[] typeLiteral = {"(NOOB|NUMBR|NUMBAR|YARN|TROOF|TYPE)"};
    private String[] variableIdentifier = {"[A-Za-z][A-Za-z0-9_]*"};
    
    private ArrayList<Token> symbolTable;

    public LexicalAnalyser(){
        this.symbolTable = new ArrayList<Token>();
    }

    private ArrayList<String> checkRegex(String word, String[] regexArr){
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
            matcher = pattern.matcher(word);    
            found = matcher.find();
            while (found){
                //java sucks: you need to escape the parenthesis to make any regex that has a "( )" work.
                matched = matcher.group(1);
                lexemes.add(matched);
                matched = matched.replaceAll("\\(", "\\\\(");
                matched = matched.replaceAll("\\)", "\\\\)");
                matched = matched.replaceAll("\\[", "\\\\[");
                matched = matched.replaceAll("\\]", "\\\\]");
                matched = matched.replaceAll("\\{", "\\\\{");
                matched = matched.replaceAll("\\}", "\\\\}");
                matched = matched.replaceAll("\\+", "\\\\+");
                matched = matched.replaceAll("\\*", "\\\\*");


                if (identifyVar){
                    String[] wordSplit = word.split("\\s");
                    for (int i = 0 ; i < wordSplit.length; i++){
                        if (wordSplit[i].equals(matched)){
                            wordSplit[i] = "";
                        }
                    }
                    word = String.join(" ", wordSplit);
                } else {
                    word = word.replaceAll(matched, "");
                }
                // System.out.println(word + " regex: " + matched);
                found = matcher.find();
            }
        }
        
        return lexemes;
    }

    private String updateLOC(String loc, ArrayList<String> lexemes, boolean identifyVar){
        for (String s : lexemes){
            s = s.replaceAll("\\(", "\\\\(");
            s = s.replaceAll("\\)", "\\\\)");
            s = s.replaceAll("\\[", "\\\\[");
            s = s.replaceAll("\\]", "\\\\]");
            s = s.replaceAll("\\{", "\\\\{");
            s = s.replaceAll("\\}", "\\\\}");
            s = s.replaceAll("\\+", "\\\\+");
            s = s.replaceAll("\\*", "\\\\*");

            if (identifyVar){
                String[] wordSplit = loc.split("\\s");
                for (int i = 0 ; i < wordSplit.length; i++){
                    if (wordSplit[i].equals(s)){
                        wordSplit[i] = "";
                    }
                }
                loc = String.join(" ", wordSplit);
            } else {
                loc = loc.replaceAll(s, "");
            }

        }
        return loc;
    }

    private void updateSymbolTable(ArrayList<String> lexemes, String type){
        for (String s : lexemes){
            this.symbolTable.add(new Token(s, type));
        }
    }

    public ArrayList<Token> lexicalAnalysis(ArrayList<String> linesOfCode) throws Exception {
        ArrayList<String> keywordLexeme;
        ArrayList<String> numbarLexeme;
        ArrayList<String> numbrLexeme;
        ArrayList<String> yarnLexeme;
        ArrayList<String> troofLexeme;
        ArrayList<String> typeLexeme;
        ArrayList<String> variableLexeme;

        try{
            for (String loc : linesOfCode) {

                keywordLexeme = this.checkRegex(loc, keywords);
                loc = updateLOC(loc, keywordLexeme, false);
                yarnLexeme = this.checkRegex(loc, yarn);
                loc = updateLOC(loc, yarnLexeme, false);
                troofLexeme = this.checkRegex(loc, troof);
                loc = updateLOC(loc, troofLexeme, false);
                typeLexeme = this.checkRegex(loc, typeLiteral);
                loc = updateLOC(loc, typeLexeme, false);
                variableLexeme = this.checkRegex(loc, variableIdentifier);
                loc = updateLOC(loc, variableLexeme, true);
                numbarLexeme = this.checkRegex(loc, numbar);
                loc = updateLOC(loc, numbarLexeme, false);
                numbrLexeme = this.checkRegex(loc, numbr);
                loc = updateLOC(loc, numbrLexeme, false);
                System.out.println(loc);

                if (loc.contains("[^\\s]")){
                    throw new Exception("Lexical analysis error");
                }

                this.updateSymbolTable(keywordLexeme, "Keyword");
                this.updateSymbolTable(yarnLexeme, "Yarn");
                this.updateSymbolTable(troofLexeme, "Troof");
                this.updateSymbolTable(typeLexeme, "Type");
                this.updateSymbolTable(variableLexeme, "Variable identifier");
                this.updateSymbolTable(numbarLexeme, "Numbar");
                this.updateSymbolTable(numbrLexeme, "Numbr");
                
            }
            
        } catch(Exception e){
            System.out.println(e);
        }
        return symbolTable;
    }
}
