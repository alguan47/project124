package Parts;

public class Token {
    private String lexeme;
    private String type;
    
    public Token(String lexeme, String type){
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme(){
        return this.lexeme;
    }

    public String getType(){
        return this.type;
    }
}
