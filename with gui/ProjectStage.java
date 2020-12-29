package application;

import java.io.File;
import java.util.ArrayList;
import FileHandlers.FileHandler;
import Parts.LexicalAnalyser;
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
	static Stage stage;
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
	private VBox box;
	private Font theFont;
	
	private TextArea textarea;
	private TextArea input;
	private Text code; 
	private Text lexemes;
	private Text tables;
	private String inputString;
	
	static ArrayList<String> linesOfCode;
	static ArrayList<Token> symbolTable;
	static ArrayList<String> cleanedProgram;
	static ArrayList<Token> newSymbolTable;
	static String codeString;
	static String lexemesString;
	static String classificationString;
	static String tableString;
	
	final static int WINDOW_WIDTH = 1300; 
	final static int WINDOW_HEIGHT = 920;
	final static int GRIDPANE_SIZE = 1300;
	
    public ProjectStage(String path){
    	this.root = new Group();
		this.canvas = new Canvas(ProjectStage.WINDOW_WIDTH,ProjectStage.WINDOW_HEIGHT);	
		this.gc = canvas.getGraphicsContext2D();
		
		this.scene = new Scene(root, ProjectStage.WINDOW_WIDTH,ProjectStage.WINDOW_HEIGHT,Color.WHITE);	
		
		this.label = new Label(path);					//text indicating location of file
		this.pathText = new ScrollPane();
		this.openFileButton = new Button("Open New File");  //button for opening new file
		
		this.lci = new Label("LOL Code Interpreter");		//lol code interpreter title
		this.lex = new Label("Lexemes");					//lexemes title
		this.table = new Label("SYMBOL TABLE");				//symbol table title
		this.openFile = new BorderPane();					//open new file container
		
		this.scroll = new ScrollPane();						//scrollpane for actual code
		this.scroll1 = new ScrollPane();					//scrollpane for syntactical analysis
		this.scroll2 = new ScrollPane();					//scrollpane for semantic analysis
		
		this.textarea = new TextArea("Test");					//window for executing the lolcode
		this.input = new TextArea();						//text area for inputs
		this.inputString = new String();
		this.map = new GridPane();							//container for the top half of the interpreter
		this.box = new VBox();								//container for all the items
		this.titles = new BorderPane();						//container for titles and scrollpane of syntactical analysis
		this.titles1 = new BorderPane();					//container for titles and scrollpane of semantic analysis
		this.columns = new HBox();
		
		this.theFont = Font.font("Helvetica",20);			//set font type, style and size
		this.label.setFont(this.theFont);
		this.lci.setFont(this.theFont);
		this.lex.setFont(this.theFont);
		this.table.setFont(this.theFont);
		this.textarea.setFont(new Font("Lucida Console",20));
		this.input.setFont(new Font("Lucida Console",20));
    }
    
    /**
     * @param primaryStage
     * @throws Exception 
     */
    public void setStage(Stage primaryStage, String path) throws Exception {
    	stage = primaryStage;
    	this.read(path);
//    	this.gc.setFill(Color.BLACK);
    	
    	this.pathText.setContent(this.label);
    	this.pathText.setPrefSize(300, 50);
    	this.openFile.setLeft(this.pathText);	
    	this.openFile.setRight(this.openFileButton);
    	this.openFileButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    	
    	this.openNewFile(openFileButton,primaryStage);
    	
    	this.openFile.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), new Insets(-2, -2, -2, -2))));
    	this.map.add(openFile, 0, 0);
    	
    	
    	this.map.add(this.lci, 1, 0, 2, 1);
    	GridPane.setHalignment(this.lci, HPos.CENTER);
    	
    	this.scroll.setPrefSize(415, 420);
    	this.scroll.setPadding(new Insets(5, 20, 5, 20));
    	this.code = new Text(codeString);							//adds the lolcode	
    	this.scroll.setContent(code);
    	this.map.add(scroll, 0,1);
    	
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
    	
    	this.scroll2.setPrefSize(415, 420);
    	this.scroll2.setPadding(new Insets(5, 20, 5, 20));
    	this.tables = new Text(tableString);						//adds the semantic analysis
    	this.scroll2.setContent(tables);
    	
    	Text i = new Text("\tIdentifier\t\t\tValue");
    	this.titles1.setTop(this.table);
    	BorderPane.setAlignment(this.table, Pos.CENTER);
    	this.titles1.setLeft(i);
    	BorderPane.setAlignment(i, Pos.CENTER);
    	this.titles1.setBottom(scroll2);
    	this.map.add(titles1,2,1);
    	this.map.setHgap(5);
    	this.map.setVgap(5);

		this.box.setLayoutX(ProjectStage.WINDOW_WIDTH*0.02);
	    this.box.setLayoutY(ProjectStage.WINDOW_HEIGHT*0.02);
	    
	    this.box.setSpacing(5);
    	
    	Button execute = new Button("Execute");
    	execute.setFont(this.theFont);
    	this.addEventHandler(execute);
    	execute.setMaxWidth(Double.MAX_VALUE);
    	this.textarea.setPrefHeight(200);
    	
    	this.textarea.setText("oh hello there oUo"); //test
    	
    	this.input.setPrefHeight(100);
    	this.input.setText("input here");
    	this.box.getChildren().addAll(map,execute,this.textarea,this.input);
    	this.root.getChildren().add(this.box);
    	
		stage.setTitle("Project Interpreter");
		stage.setScene(this.scene);
		stage.setResizable(false);
		stage.show();
    }
    
    public void read(String path) throws Exception{
    	FileHandler fh = new FileHandler();        
        linesOfCode = new ArrayList<String>(); 
        linesOfCode = fh.openFile(path);
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
        SyntaxAnalyser syntaxAnalyser = new SyntaxAnalyser();
        symbolTable = new ArrayList<Token>();

        //clean the code by removing the comments first
        cleanedProgram = lexicalAnalyser.getCleanProgram(linesOfCode);
        

        //add it to the symbol table
        for (String loc : cleanedProgram){
            newSymbolTable = lexicalAnalyser.lexicalAnalysis(loc);
            symbolTable.addAll(newSymbolTable);
            System.out.println("Syntactically correct? " + syntaxAnalyser.syntaxAnalyse(newSymbolTable));
        }
        
        //print the cleaned code
        codeString = new String("");
        for (String s : cleanedProgram){
            System.out.println(s);
            codeString = codeString + s + "\n";
        }

        System.out.println();

        //print the tokens
        for (Token t : symbolTable){
            System.out.println(t.getLexeme() + " " + t.getType());
        }
        
        lexemesString = new String("");
        classificationString = new String("");
        for(Token l: symbolTable) {
        	lexemesString = lexemesString + l.getLexeme() + "\n";
        	classificationString = classificationString + l.getType() + "\n";
        }
        
        
        tableString = new String("");
//        for(Token v: symbolTable) {
//        	table = table + v.getLexeme() +"\t\t" + v.getType() +"\n";
//        }
        tableString = "Semantic Analysis";
    }
    
    public void addEventHandler(Button btn) {
    	btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		public void handle(MouseEvent arg0) {
    			inputString = input.getText();
    			System.out.println(inputString);
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
