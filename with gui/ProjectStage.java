package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import FileHandlers.FileHandler;
import Parts.LexicalAnalyser;
import Parts.SemanticAnalyzer;
import Parts.SyntaxAnalyser;
import Parts.Token;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProjectStage{
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;

	private Label label;
	private Label lci;
	private Button openFileButton;
	private Label lex;
	private Label table;
	private GridPane map;
	private ScrollPane pathText;
	private ScrollPane scroll;
	private ScrollPane scroll1;
	private ScrollPane scroll2;
	private BorderPane openFile;
	private BorderPane titles;
	private BorderPane titles1;
	private HBox columns;
	private HBox columns1;
	private VBox box;
	private Font theFont;
	
	private TextArea terminal;
	private TextArea input;
	private Text code;
	private String inputString;
	
	static String codeString;
	static String lexemesString;
	static String classificationString;
	static String identifierString;
	static String valueString;
	
	private ArrayList<String> linesOfCode;
	private ArrayList<Token> visibles;
	private int num;
	private Boolean check;
	
	final static int WINDOW_WIDTH = 1300; 
	final static int WINDOW_HEIGHT = 920;
	
    public ProjectStage(String path){
    	this.root = new Group();
    	
		this.canvas = new Canvas(ProjectStage.WINDOW_WIDTH,ProjectStage.WINDOW_HEIGHT);	//might remove, just for backgrounds
		this.gc = canvas.getGraphicsContext2D();
		
		this.scene = new Scene(root, ProjectStage.WINDOW_WIDTH,ProjectStage.WINDOW_HEIGHT,Color.WHITE);	
		
		this.label = new Label(path);						//text string of location of file
		this.pathText = new ScrollPane();
		this.openFileButton = new Button("Open New File");  //button for opening new file
		
		this.lci = new Label("LOL Code Interpreter");		//lol code interpreter title
		this.lex = new Label("Lexemes");					//lexemes title
		this.table = new Label("SYMBOL TABLE");				//symbol table title
		this.openFile = new BorderPane();					//open new file container
		
		this.scroll = new ScrollPane();						//scrollpane for actual code
		this.scroll1 = new ScrollPane();					//scrollpane for syntactical analysis
		this.scroll2 = new ScrollPane();					//scrollpane for semantic analysis
		
		this.terminal = new TextArea();						//window for executing the lolcode / terminal
		this.input = new TextArea();						//text area for inputs
		this.inputString = new String();					//string of the inputs
		this.map = new GridPane();							//container for the top half of the interpreter
		this.box = new VBox();								//container for all the items
		this.titles = new BorderPane();						//container for titles and scrollpane of syntactical analysis
		this.titles1 = new BorderPane();					//container for titles and scrollpane of semantic analysis
		this.columns = new HBox();							//conatainer for the values of syntactical analysis
		this.columns1 = new HBox();							//conatainer for the values of semantic analysis
		this.visibles = new ArrayList<Token>();
		this.num = 0;
		
		this.theFont = Font.font("Helvetica",20);			//set font type, style and size
		this.label.setFont(this.theFont);
		this.lci.setFont(this.theFont);
		this.lex.setFont(this.theFont);
		this.table.setFont(this.theFont);
		this.terminal.setFont(new Font("Lucida Console",20));
		this.input.setFont(new Font("Lucida Console",20));
    }
    
    /**
     * @param primaryStage, path
     * @throws Exception 
     */
    /**
     * @param primaryStage
     * @param path
     * @throws Exception
     */
    public void setStage(Stage primaryStage, String path) throws Exception {
    	stage = primaryStage;
    	this.read(path);
//    	this.gc.setFill(Color.BLACK);
    	
    	//openfile container
    	this.pathText.setContent(this.label);
    	this.pathText.setPrefSize(300, 50);
    	this.openFile.setLeft(this.pathText);	
    	this.openFile.setRight(this.openFileButton);
    	this.openFileButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    	this.openNewFile(openFileButton,primaryStage);
    	this.openFile.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(-2, -2, -2, -2))));
    	this.map.add(openFile, 0, 0);
    	
    	//lol code interpreter title
    	this.map.add(this.lci, 1, 0, 2, 1);
    	GridPane.setHalignment(this.lci, HPos.CENTER);
    	
    	//container for lolcode
    	this.scroll.setPrefSize(415, 420);
    	this.scroll.setPadding(new Insets(5, 20, 5, 20));
    	this.code = new Text(codeString);							//adds the lolcode	
    	this.scroll.setContent(code);
    	this.map.add(scroll, 0,1);
    	
    	//container for lexemes
    	this.scroll1.setPrefSize(415, 420);
    	this.scroll1.setPadding(new Insets(5, 20, 5, 20));					
    	this.columns.getChildren().add(new Text(lexemesString));
    	this.columns.getChildren().add(new Text(classificationString));
    	this.columns.setSpacing(30);
    	this.scroll1.setContent(columns);							//adds the lexemes
    	Text l = new Text("\tLexeme\t\t\tClassification");
    	this.titles.setTop(this.lex);
    	BorderPane.setAlignment(this.lex, Pos.CENTER);
    	this.titles.setLeft(l);
    	BorderPane.setAlignment(l, Pos.CENTER);
    	this.titles.setBottom(scroll1);
    	this.map.add(titles,1,1);
    	
    	//container for symbol table
    	this.scroll2.setPrefSize(415, 420);
    	this.scroll2.setPadding(new Insets(5, 20, 5, 20));
    	this.columns1.getChildren().add(new Text(identifierString));//adds the semantic analysis
    	this.columns1.getChildren().add(new Text(valueString));
    	this.scroll2.setContent(columns1);
    	Text i = new Text("\tIdentifier\t\t\tValue");
    	this.titles1.setTop(this.table);
    	BorderPane.setAlignment(this.table, Pos.CENTER);
    	this.titles1.setLeft(i);
    	BorderPane.setAlignment(i, Pos.CENTER);
    	this.titles1.setBottom(scroll2);
    	this.map.add(titles1,2,1);
    	this.map.setHgap(5);
    	this.map.setVgap(5);
    	
    	//container for the whole window
		this.box.setLayoutX(ProjectStage.WINDOW_WIDTH*0.02);
	    this.box.setLayoutY(ProjectStage.WINDOW_HEIGHT*0.02);
	    this.box.setSpacing(5);
    	
	    //execute button
    	Button execute = new Button("Execute");
    	execute.setFont(this.theFont);
    	this.addEventHandler(execute);
    	execute.setMaxWidth(Double.MAX_VALUE);
    	
    	//terminal
    	this.terminal.setPrefHeight(200);
    	this.terminal.setEditable(false);
    	terminalVisible();
    	
    	//input window
    	this.input.setPrefHeight(100);
    	this.input.setPromptText("Input Here");
    	this.input.setDisable(true);
    	
    	this.box.getChildren().addAll(map,execute,this.terminal,this.input);
    	this.root.getChildren().add(this.box);
    	
    	//set stage
		stage.setTitle("Project LOLCode Interpreter");
		stage.setScene(this.scene);
		stage.setResizable(false);
		stage.show();
    }
    
    public void terminalVisible() {
    	for(String code: linesOfCode) {
    		if(code.contains("VISIBLE")) {
    			String commentRemove[];
    			String hold = "";
    			String copy = "";
    			hold = code.trim();
    			Boolean pair = false;

    			for(int a=0;a<hold.length();a++) {
    				if(pair && hold.charAt(a) != '\"') {
    					copy = copy + hold.charAt(a);
    					continue;
    				}
    				if(hold.charAt(a) == '\"') {
    					if(pair) {
    						pair = false;
    						continue;
    					}else {
    						pair = true;
    						continue;
    					}
    				}
    				//this part should be where the variable be or whatever arithmetic happens
    				copy = copy + hold.charAt(a);
    			}
    			copy = copy.replaceAll("VISIBLE ", "");
    			commentRemove = copy.split("BTW");
    			if(commentRemove != null) {
    				copy = commentRemove[0];
    			}
    			this.visibles.add(new Token(copy,"visible"));

    			
    		}else if(code.contains("GIMMEH")) {
    			this.visibles.get(this.visibles.size()-1).gimmeEdit();
    		}
    	}
    }
    
    public void read(String path) throws Exception{
    	FileHandler fh = new FileHandler();        
    	linesOfCode = new ArrayList<String>(); 
        linesOfCode = fh.openFile(path);
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        ArrayList<Token> symbolTable = new ArrayList<Token>();
        
        //actual lolcode
        codeString = new String("");
        for(String code: linesOfCode) {
        	codeString = codeString + code + "\n";
        }
        
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
        lexemesString = new String("");
        classificationString = new String("");
        for (Token t : symbolTable){
            System.out.println(t.getLexeme() + " " + t.getType());
        	lexemesString = lexemesString + t.getLexeme() + "\n";
        	classificationString = classificationString + t.getType() + "\n";
        }
        
        
//        for(int i = 0; i < symbolTable.size(); i++) {
//        	semanticAnalyzer.setCopyTable(symbolTable.get(i));      
//        }
//        
//        semanticAnalyzer.doTheDew();
        
    }
    
    public void addEventHandler(Button btn) {
    	btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent arg0) {
    			input.setPromptText("Input Here");
    			if(visibles.size() != 0 && num < visibles.size()) {
	    			String print = "";
	    			if(visibles.get(num).gimme()) {
	    				input.setDisable(false);
	    				inputString = input.getText();
	    				System.out.println(inputString);
	    				terminal.setText(visibles.get(num).getLexeme());
	    			}else{
	    				input.setText("");
	    				input.setDisable(true);
	    				while(!visibles.get(num).gimme()) {
	    					print = print + visibles.get(num).getLexeme() + "\n";
	    	    			if(num != visibles.size()-1) {
	    	    				num++;
	    	    			}else {
	    	    				break;
	    	    			}
	    				}
	    				terminal.setText(print);
	    				num++;
	    			}
	    			
	    			if(num != visibles.size()-1) {
	    				num++;
	    			}
    			}

    		}
    	});
    }
    
    public void openNewFile(Button open, Stage stage) {
        EventHandler<ActionEvent> event =  
        new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e){ 
                FileChooser fil_chooser = new FileChooser(); 
                fil_chooser.setTitle("Select File"); 
                fil_chooser.setInitialDirectory(new File("c:\\")); 
                File file = fil_chooser.showOpenDialog(stage); 
                
                if (file != null) { 
                    
                    ProjectStage newStage = new ProjectStage(file.getAbsolutePath());
                    try {
						newStage.setStage(stage,file.getAbsolutePath());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                } 
            } 
        }; 
        open.setOnAction(event);
    }
}
