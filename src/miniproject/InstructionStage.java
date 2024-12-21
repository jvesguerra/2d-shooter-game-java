package miniproject;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InstructionStage {
	private Scene instructionScene;
	private Stage stage;
	private Group root;
	private Canvas canvas;

	InstructionStage() {
		this.root = new Group();
		this.instructionScene = new Scene(root, SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT,Color.BLACK);
		this.canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
		this.setProperties();
	}

//	changes the stage from splash scene to about scene
	public void setStage(Stage stage) {
		this.stage = stage;
		this.root.getChildren().add(canvas);
		this.stage.setTitle("Mini Ship Shooting Game");
		this.stage.setScene(this.getScene());
		this.stage.show();
	}

//	sets the properties of the stage from splash to about
	private void setProperties(){
		StackPane root = new StackPane();
	    root.getChildren().addAll(this.createCanvas(0),this.createVBox());
	    this.instructionScene = new Scene(root);
	    System.out.println("SetProperties!");
	}

//	where the design, background is drawn
	private Canvas createCanvas(int num) {
		// TODO Auto-generated method stub
		Canvas canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
		if (num == 0) {
	        Image bg = new Image("images/InstructionsBG.png");
	        gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		}
        return canvas;
	}

//	handles the creation of buttons in the middle of the scene
    private VBox createVBox() {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_RIGHT);
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(8);

        Button b1 = new Button("Main Menu");
        this.addEventHandler(b1);
        vbox.getChildren().add(b1);

        return vbox;
    }

//  this changes the stage back to the original splash stage
	private void addEventHandler(Button btn) {
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				SplashScreen theGameStage = new SplashScreen();
				theGameStage.setStage(stage);
			}
		});

	}

	Scene getScene(){
		return this.instructionScene;
	}
}
