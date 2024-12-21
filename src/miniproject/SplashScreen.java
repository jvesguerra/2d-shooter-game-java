package miniproject;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen {

	private Scene splashScene;
	private Scene scene;
	public Stage stage;
	private VBox root;
	private Canvas canvas;
	private GraphicsContext gc;

	public static final int WINDOW_HEIGHT = 750;
	public static final int WINDOW_WIDTH = 1200;

	public static final String NGM = "New Game";
	public static final String ABT = "About Game";
	public static final String INS = "Instructions";

	//the class constructor
	public SplashScreen() {
		this.root = new VBox();
		this.scene = new Scene(root, SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT,Color.BLACK);
		this.canvas = new Canvas(SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.root.getChildren().add(this.canvas);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle( "Mini Ship Shooting Game" );
		this.initSplash(stage);
		stage.setScene( this.scene );
        stage.setResizable(false);
		stage.show();
	}

	private void initSplash(Stage stage) {
		// TODO Auto-generated method stub
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createCanvas(),this.createVBox());
        this.scene = new Scene(root);
	}

	private Canvas createCanvas() {
		// TODO Auto-generated method stub
		this.canvas = new Canvas(SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		Image bg = new Image("images/SplashBackground.png");
		 gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		return canvas;
	}

	private VBox createVBox() {
		// TODO Auto-generated method stub
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);

		Button b1 = new Button();
		Button b2 = new Button();
		Button b3 = new Button();
		b1.setId(NGM);
		b2.setId(INS);
		b3.setId(ABT);
		Image bt1 = new Image("images/Button1.png");
		Image bt2 = new Image("images/Button2.png");
		Image bt3 = new Image("images/Button3.png");

		b1.setGraphic(new ImageView(bt1));
		b2.setGraphic(new ImageView(bt2));
		b3.setGraphic(new ImageView(bt3));

		this.addEventHandler(b1);
		this.addEventHandler(b2);
		this.addEventHandler(b3);

		vbox.getChildren().add(b1);
		vbox.getChildren().add(b2);
		vbox.getChildren().add(b3);

		return vbox;
	}

	void setStage(int num) {
		PauseTransition transition = new PauseTransition(Duration.millis(1));
		transition.play();
		transition.setOnFinished(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				switch(num) {
				case 0:
					GameStage theGameStage = new GameStage();
					theGameStage.setStage(stage);
					break;
				case 1:
					AboutStage theAboutStage = new AboutStage(1);
					theAboutStage.setStage(stage);
					break;
				case 2:
					InstructionStage theInstructionStage = new InstructionStage();
					theInstructionStage.setStage(stage);
					break;
				}

			}
		});
	}

	private void addEventHandler(Button btn) {
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				switch(btn.getId()) {
				case NGM:
					setStage(0);
					System.out.println("NGM");
					break;
				case ABT:
					setStage(1);
					System.out.println("ABT");
					break;
				case INS:
					setStage(2);
					System.out.println("INS");
					break;
				}
			}
		});

	}

	Scene getScene(){
		return this.splashScene;
	}
}

