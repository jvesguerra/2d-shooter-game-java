package miniproject;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class GameStage {
	private Scene scene; //Game Scene
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;

	//the class constructor
	public GameStage() {
		this.root = new Group();
		this.scene = new Scene(root, SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT, Color.BLACK);
		this.canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene);
	}

	//method to add the stage elements and also make the splash stage into game stage
	public void setStage(Stage stage) {
		this.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);
		this.stage.setTitle("Mini Ship Shooting Game");
		this.stage.setScene(this.scene);

		//invoke the start method of the animation timer
		this.gametimer.start();
		this.stage.show();
	}
}

