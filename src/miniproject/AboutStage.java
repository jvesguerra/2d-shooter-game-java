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

public class AboutStage {
	private Scene aboutScene;
	private Stage stage;
	private Group root;
	private Canvas canvas;

	public static final String NEXT = "Next Page";
	public static final String MAIN = "Main Menu";
	public static final String LAST = "Last Page";

	AboutStage(int num) {
		this.root = new Group();
		this.aboutScene = new Scene(root, SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT,Color.BLACK);
		this.canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
		this.setProperties(num);
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
	private void setProperties(int num){
		StackPane root = new StackPane();
	    root.getChildren().addAll(this.createCanvas(num),this.createVBox(num));
	    this.aboutScene = new Scene(root);
	}

//	where the design, background is drawn
	private Canvas createCanvas(int num) {
		Canvas canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
		if (num == 1) {
	        Image bg = new Image("images/About1.png");
	        gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		} else if (num == 2) {
			Image bg = new Image("images/About2.png");
	        gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		}
        return canvas;
	}

//	handles the creation of buttons in the middle of the scene
    private VBox createVBox(int num) {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_RIGHT);
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(8);

        if (num == 1) {
        	 Button b1 = new Button(NEXT);
             this.addEventHandler(b1);
             vbox.getChildren().add(b1);
             b1.setId(NEXT);
        } else if (num == 2) {
        	Button b1 = new Button(LAST);
            this.addEventHandler(b1);
            vbox.getChildren().add(b1);
            b1.setId(LAST);
        }

        Button b2 = new Button(MAIN);
        this.addEventHandler(b2);
        vbox.getChildren().add(b2);
        b2.setId(MAIN);

        return vbox;
    }

//  this changes the stage back to the original splash stage
	private void addEventHandler(Button btn) {
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				switch (btn.getId()) {
				case MAIN:
					SplashScreen theGameStage = new SplashScreen();
					theGameStage.setStage(stage);
					break;
				case NEXT:
					AboutStage theAboutStage2 = new AboutStage(2);
					theAboutStage2.setStage(stage);
					break;
				case LAST:
					AboutStage theAboutStage1 = new AboutStage(1);
					theAboutStage1.setStage(stage);
					break;
				}

			}
		});
	}

	Scene getScene(){
		return this.aboutScene;
	}
}
