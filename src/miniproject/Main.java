package miniproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage){
		SplashScreen theGameStage = new SplashScreen();
		theGameStage.setStage(stage);
		stage.setResizable(false);
	}

}
