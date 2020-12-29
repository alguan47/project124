package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class ProjectInterpreter extends Application{
    public static void main(String[] args) throws Exception{
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		String path = "./src/TestFiles/arith.lol";
		ProjectStage stage = new ProjectStage(path);
		stage.setStage(primaryStage,path);
	}
}
