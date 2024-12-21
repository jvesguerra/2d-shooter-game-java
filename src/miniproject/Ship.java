package miniproject;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;

public class Ship extends Sprite{
	private String name;

	private int minionsKilled; //Number of minions killed
	private final static int SHIP_WIDTH = 100; //Change back to 50
	public final static int HUD_LIMITS = 50;
	public final static int MAX_STRENGTH = 150;
	public final static int SHIP_SPEED = 10;
	private int strength; //health of the ship

	private boolean alive;
	private boolean state;


	private ArrayList<Bullet> bullets;
	public final static Image SHIP_IMAGE = new Image("images/JINX.png",Ship.SHIP_WIDTH,Ship.SHIP_WIDTH,false,false);


	public Ship(String name, int x, int y){
		super(x,y);
		this.name = name;

		this.minionsKilled = 0;
		Random r = new Random();
		this.strength = r.nextInt(51)+100; //Randomizes the strength of the ship

		this.alive = true;
		state = false; // Makes the ship vulnerable

		this.bullets = new ArrayList<Bullet>();
		this.loadImage(Ship.SHIP_IMAGE);
	}

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}

	//method that will decrease the strength of the ship until it dies
	public void die(int damage){
		System.out.println("Current Strength: " + this.strength);
		if(this.strength - damage > 0){
			this.strength -= damage;
		}else{
			this.alive = false;
			this.vanish();
		}
    }

	//ADD
	//method that will add strength to the ship
	void AddStrength(int num){
		if ((this.getStrength() + num) >= 150) {
			this.strength = MAX_STRENGTH;
		} else {
			this.strength += num;
		}
	}

	//method that will increase the number of minions killed by the ship
	public void addMinionKilled(){
		this.minionsKilled++;
	}

	//method that will make the ship vulnerable or invulnerable to damage
	public boolean setState(boolean newState){
		return this.state = newState;
	}

	//GETTERS
	public boolean getState(){
		return this.state;
	}

	public int getStrength(){
		return this.strength;
	}

	public String getName(){
		return this.name;
	}

	public int getMinionKilled(){
		return this.minionsKilled;
	}

	public int getSpeed(){
		return SHIP_SPEED;
	}

	//method that will get the bullets 'shot' by the ship
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	//method called if spacebar is pressed
	public void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width+20);
		int y = (int) (this.y + this.height/2);
		Bullet b = new Bullet(x,y);
		this.bullets.add(b);
    }

	//method called if up/down/left/right arrow key is pressed.
//	Added condition of instead of >= 0, it is >= HUD_LIMITS, so the player will not exceed HUD limits, also minions will not spawn above HUD and not below too much on the screen also
	public void move() {
		if(this.x+this.dx >= 0 && this.x+this.dx <= SplashScreen.WINDOW_WIDTH-this.width && this.y+this.dy >= HUD_LIMITS && this.y+this.dy <= SplashScreen.WINDOW_HEIGHT-this.height){
			this.x += this.dx;
			this.y += this.dy;
		}

	}

}
