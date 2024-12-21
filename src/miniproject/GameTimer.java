package miniproject;

import java.util.ArrayList;
import java.util.Random;
//added
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method.
 */

public class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Scene theScene;
	private Ship myShip;

	//ArrayLists
	private ArrayList<Minion> minions;
	private ArrayList<PowerUp> collectibles; //List of power ups

	//Max values
	public static final int MAX_NUM_FISHES = 3;
	private static final int MAX_NUM_POTION= 1;
	private final static int MAX_TIME = 60;
	private final static int INITIAL_MINIONS = 7;
	private final static int NUM_MINIONS = 3;
	private final static int ONE_THIRD_HEALTH = 50;
	private final static int TWO_THIRD_HEALTH = 100;
	private final static int MAX_HEALTH = 150;

	//Timers
	private long gameTime;
	private long minionsSpawn;
	private long bossSpawn;
	private long potionSpawn;

	//Spawn Delays
	public static final int SPAWN_CREEP_DELAY = 5;
	public static final int SPAWN_POTION_DELAY = 10;
	public static final int SPAWN_BOSS_DELAY = 30;
	public static final int HUD_RENDER_DELAY = 1;

	//Duration
	private final static int POTION_DURATION = 6;
	private final static int POTION_DURATION_MILIS = 5000;

	//Boundary
	private final static int BOUNDARY = 100;
	private final static int HUD_BOUNDARY = 100;

	//Healthbar Properties
	private final static int HEALTHBAR_PLAYER_X_AXIS = 200;
	private final static int HEALTHBAR_BOSS_X_AXIS = 850;
	private final static int HEALTHBAR_Y_AXIS = 20;
	private final static int HEALTHBAR_PLAYER_INVINCIBLE = 150;



	GameTimer(GraphicsContext gc, Scene theScene){
		this.gc = gc;
		this.theScene = theScene;
		this.myShip = new Ship("Going merry",100,100);
		this.minions = new ArrayList<Minion>();	//instantiate the ArrayList of Minions
		this.collectibles = new ArrayList<PowerUp>();
		this.spawnMinions(INITIAL_MINIONS); //Initialized the minions at the start of the game
		//call method to handle mouse click event
		this.handleKeyPressEvent();
		//Timers
		this.gameTime = this.minionsSpawn = this.bossSpawn = this.potionSpawn = System.nanoTime();
	}

	@Override
	public void handle(long currentNanoTime) {
//		clears the canvas every nanotime
		this.gc.clearRect(0, 0, SplashScreen.WINDOW_WIDTH,SplashScreen.WINDOW_HEIGHT);

//		draws the background every nano time after it is cleared, also changes in color depending on the health, and powerup that the user has obtained
		if (this.myShip.getStrength() > ONE_THIRD_HEALTH && !this.myShip.getState()) {
			Image bg = new Image("images/GameBackground1.png");
			this.gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		} else if (this.myShip.getStrength() <= ONE_THIRD_HEALTH && !this.myShip.getState()){
			Image bg = new Image("images/GameBackground2.png");
			this.gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		} else if (this.myShip.getState()) {
			Image bg = new Image("images/GameBackground3.png");
			this.gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		}

		this.myShip.move();

		double timer = (currentNanoTime - this.gameTime) / 1000000000.0; //Game Time
		double spawnElapsedTime = (currentNanoTime - this.minionsSpawn) / 1000000000.0; //Creep Time
		double bossElapsedTime = (currentNanoTime - this.bossSpawn) / 1000000000.0; //Boss Time
		double potionElapsedTime = (currentNanoTime - this.potionSpawn) / 1000000000.0; //Potion Time

		//render the ship
		this.myShip.render(this.gc);

		//Timer for Minions
		if(spawnElapsedTime > GameTimer.SPAWN_CREEP_DELAY){
			this.spawnMinions(NUM_MINIONS);
			this.minionsSpawn = System.nanoTime();
		}
		//Timer for potions
		if(potionElapsedTime > GameTimer.SPAWN_POTION_DELAY){
			this.spawnCollectibles();
			this.potionSpawn = System.nanoTime();
		}
		//Timer for the boss
		if(bossElapsedTime > GameTimer.SPAWN_BOSS_DELAY){
			this.spawnBoss();
			this.bossSpawn = System.nanoTime();
		}
//		mov
		this.moveBullets();
		this.moveMinions();
		this.moveCollectibles();

		this.renderBullets();
		this.renderMinions();
		this.renderPowerUps();

		this.drawHUD(timer);

		if(timer > GameTimer.MAX_TIME){
			System.out.println("VICTORY!");
			this.drawGameOver(0, timer);
			this.stop();
		}
		if(!this.myShip.isAlive()){
			System.out.println("DEFEAT");
			this.drawGameOver(1, timer);
			this.stop();
		}
	}

	private void drawGameOver(int i, double timer) {
		// TODO Auto-generated method stub
		int timeLeft = (int) GameTimer.MAX_TIME - (int) timer;
		if (i == 0) {
			Image bg = new Image("images/GameOverWin.png");
			this.gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
		} else {
			Image bg = new Image("images/GameOverLose.png");
			this.gc.drawImage(bg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);

			this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
			this.gc.setFill(Color.WHITE);
			this.gc.fillText(timeLeft+"", 780, 450);
		}
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.myShip.getMinionKilled()+"", 780, 350);
	}

	private void drawHUD(double timer) {
//		SHIP HEALTH HUD
		Rectangle healthBarShip = new Rectangle();
		this.drawHealthBar(gc, healthBarShip, 0);

//		BOSS HEALTH HUD
		if (this.isBossAlive()) {
			Rectangle healthBarBoss = new Rectangle();
			this.drawHealthBar(gc, healthBarBoss, 1);
		}

//		MINIONS KILLED HUD
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(this.myShip.getMinionKilled()+"", 512, 50);

//		TIME LEFT HUD
		int timeLeft = (int) GameTimer.MAX_TIME - (int) timer;
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
		if (timeLeft <= 30) {
			this.gc.setFill(Color.MEDIUMVIOLETRED);
		} else {
			this.gc.setFill(Color.WHITE);
		}
		this.gc.fillText(timeLeft+"", 766, 50);

		Image fg = new Image("images/GameForeground1.png");
		this.gc.drawImage(fg, 0, 0, SplashScreen.WINDOW_WIDTH, SplashScreen.WINDOW_HEIGHT);
	}

	private void drawHealthBar(GraphicsContext gc,Rectangle rect, int type){
		double bossHealth = getBossHealth() ;
		int playerHealth = this.myShip.getStrength();

		switch (type) {
		case 0:
			if (this.myShip.getState()) {
				gc.setFill(Color.DARKMAGENTA);
				gc.fillRoundRect(GameTimer.HEALTHBAR_PLAYER_X_AXIS,
								GameTimer.HEALTHBAR_Y_AXIS,
				    			GameTimer.HEALTHBAR_PLAYER_INVINCIBLE,
				    			30,
				    			10,
				    			5);
				this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
				this.gc.setFill(Color.WHITE);
				this.gc.fillText("INVINSIBLE", 200,43);

			} else {

				if (playerHealth >= 100) {
					gc.setFill(Color.LIGHTSEAGREEN);
				} else if (playerHealth > 50 && playerHealth < 100) {
					gc.setFill(Color.LIGHTSALMON);
				} else if (playerHealth <= 50) {
					gc.setFill(Color.RED);
				}
				gc.fillRoundRect(GameTimer.HEALTHBAR_PLAYER_X_AXIS,
								GameTimer.HEALTHBAR_Y_AXIS,
					    		playerHealth,
					    		30,
					    		10,
					    		5);
				this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
				this.gc.setFill(Color.WHITE);
				this.gc.fillText(playerHealth+"",200, 46);

			}
			gc.setStroke(Color.BLACK);
			break;

		case 1:
			if (bossHealth >= GameTimer.TWO_THIRD_HEALTH) {
				gc.setFill(Color.LIGHTSEAGREEN);
			} else if (bossHealth > GameTimer.ONE_THIRD_HEALTH && bossHealth < GameTimer.TWO_THIRD_HEALTH) {
				gc.setFill(Color.LIGHTSALMON);
			} else if (bossHealth <= GameTimer.ONE_THIRD_HEALTH) {
				gc.setFill(Color.RED);
			}
			gc.fillRoundRect(GameTimer.HEALTHBAR_BOSS_X_AXIS,
							GameTimer.HEALTHBAR_Y_AXIS,
		    				bossHealth,
		    				30,
		    				10,
		    				5);
			gc.setStroke(Color.BLACK);
			break;
		}
	}

	//RENDER METHODS
	//method that will render/draw the fishes to the canvas
	private void renderMinions() {
		for (Minion f : this.minions){
			f.render(this.gc);
		}
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		for(Bullet b: myShip.getBullets()){
			b.render(this.gc);
		}
	}

	//method that will render/draw the power ups to the canvas
	private void renderPowerUps(){
		for(PowerUp p: this.collectibles){
			p.render(this.gc);
		}
	}

	private void fadePowerUps(){
		for(int i =0; i < this.collectibles.size();i++){
			PowerUp p = this.collectibles.get(i);
			if(p.isVisible()){
				p.vanish();
			}
		}
	}

	// SPAWN METHODS
	//method that will spawn/instantiate three fishes at a random x,y location
	private void spawnMinions(int numMinions){
		Random r = new Random();
		for(int i=0;i< numMinions;i++){
			int x = getRandX(r);
			int y = getRandY(r);
			Minion m = new Minion(x,y,0);
			this.minions.add(m);
		}
	}

	//method that will spawn/instantiate the Boss Fish at a random x,y location
	private void spawnBoss(){
		Random r = new Random();
		int x = getRandX(r);
		int y = getRandY(r);

		Minion b = new Minion(x,y,1);
		this.minions.add(b);
	}

	//method that will spawn/instantiate a power up at a random x,y location
	private void spawnCollectibles(){
		//POWER UP TIMER
		Timer potionTimer = new Timer();
		TimerTask potionTask = new TimerTask(){
			private int duration = GameTimer.POTION_DURATION; //Duration of 5 seconds
			public void run(){
				fadePowerUps();
				this.duration --;
				if(this.duration == 0){ //If duration is equal to 0, the ship will be vulnerable again
					cancel();
				}
			}
		};

		PowerUp buffs[]  = new PowerUp[2];
		Random r = new Random();
		for(int i=0;i<MAX_NUM_POTION;i++){
			int x = getRandX(r);
			int y = getRandY(r);

			StrengthPotion sp = new StrengthPotion(x,y);
			InvulnerablePotion ip = new InvulnerablePotion(x,y);
			buffs[0] = sp;
			buffs[1] = ip;
			int choice = r.nextInt(2); //Randomizes which potion to spawn
			this.collectibles.add(buffs[choice]);

			potionTimer.scheduleAtFixedRate(potionTask,POTION_DURATION_MILIS,1000); //Potion is shown for a duration of 5 secs before it disappears
		}
	}

//	calculates for same random position within bounds of the game
	private int getRandX(Random r) {
		int x = r.nextInt((SplashScreen.WINDOW_WIDTH/2)-GameTimer.BOUNDARY) + ((SplashScreen.WINDOW_WIDTH/2)-GameTimer.BOUNDARY);
		return x;
	}
	private int getRandY(Random r) {
		int y = r.nextInt(((SplashScreen.WINDOW_HEIGHT/2)-GameTimer.BOUNDARY) + ((SplashScreen.WINDOW_HEIGHT/2) - GameTimer.BOUNDARY));
		if (y <= GameTimer.HUD_BOUNDARY) {
			y += GameTimer.HUD_BOUNDARY;
		} else if (y >= SplashScreen.WINDOW_HEIGHT - GameTimer.BOUNDARY){
			y -= GameTimer.BOUNDARY;
		}
		return y;
	}

	private int getShipSpeed() {
		return this.myShip.getSpeed();
	}

	//method that will move the bullets shot by a ship
	private void moveBullets(){
		ArrayList<Bullet> bList = this.myShip.getBullets();
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			if(b.isVisible()){
				b.move();
			}else{
				bList.remove(i);
			}
		}
	}

	//method that will move the fishes
	private void moveMinions(){
		for(int i = 0; i < this.minions.size(); i++){
			Minion f = this.minions.get(i);
			if(f.isAlive()){
				f.move();
				f.checkCollision(this.myShip);
			}else{
				this.minions.remove(i);
			}
		}
	}

//	used for checking when to draw the healthbar for the boss, if the boss is already alive, or dead
	private boolean isBossAlive() {
		boolean status = false;
		for(int i = 0; i < this.minions.size(); i++) {
			Minion b = this.minions.get(i);
			if (b.isABoss()) {
				status = b.isBossAlive();
			}
		}
		return status;
	}

	private double getBossHealth() {
		double health = 0;
		for(int i = 0; i < this.minions.size(); i++) {
			Minion b = this.minions.get(i);
			if (b.isABoss()) {
				health = b.getHealth() * 0.05;
			}
		}
		return health;
	}

	private void moveCollectibles(){
		for(int i = 0; i < this.collectibles.size(); i++){
			PowerUp c = this.collectibles.get(i);
			if(c.isVisible()){;
				c.checkCollision(this.myShip);
			}
			else collectibles.remove(i);
		}
	}

	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
	           	KeyCode code = e.getCode();
	           	if (code != KeyCode.SPACE){
	           		moveMyShip(code);
	           	} else {
	           		myShip.shoot();
	           	}
			}
		});
		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                stopMyShip(code);
            }
        });
    }

	//method that will move the ship depending on the key pressed
	private void moveMyShip(KeyCode ke) {
		if(ke==KeyCode.UP) this.myShip.setDY(-getShipSpeed());
		if(ke==KeyCode.LEFT) this.myShip.setDX(-getShipSpeed());
		if(ke==KeyCode.DOWN) this.myShip.setDY(getShipSpeed());
		if(ke==KeyCode.RIGHT) this.myShip.setDX(getShipSpeed());
		//System.out.println(ke+" key pressed.");
	   	}

	//method that will stop the ship's movement; set the ship's DX or DY to 0
	private void stopMyShip(KeyCode ke){
		if (ke == KeyCode.UP || ke == KeyCode.DOWN) {
			this.myShip.setDY(0);
		} else if (ke == KeyCode.LEFT || ke == KeyCode.RIGHT) {
			this.myShip.setDX(0);
		}
	}
}
