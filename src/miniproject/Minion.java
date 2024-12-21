package miniproject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import everwing.Guardian;
import javafx.scene.image.Image;

public class Minion extends Sprite {
	//General Attributes
	private boolean alive;
	private boolean bossAlive;
	private boolean aBoss;
	private boolean moveRight;
	private int speed;
	private int type;
	public static final int MAX_SPEED = 5;

	//Minions
	public final static Image MINION_IMAGE = new Image("images/MINION.png",Minion.MINION_WIDTH,Minion.MINION_WIDTH,false,false);
	public final static int MINION_WIDTH=50;
	public static final int MINION_DAMAGE = 30;

	//Boss
	public final static Image BOSS_IMAGE = new Image("images/BARON.png",Minion.BOSS_WIDTH,Minion.BOSS_WIDTH,false,false);
	public final static int BOSS_WIDTH=100;
	private int health;
	public static final int BOSS_HEALTH = 3000;
	public static final int BOSS_DAMAGE = 50;

	Minion(int x, int y, int type){
		super(x,y);
		if(type == 1){
			this.loadImage(BOSS_IMAGE);
			this.health = Minion.BOSS_HEALTH;
			this.bossAlive = true;
			this.aBoss = true;
		}else{
			this.loadImage(Minion.MINION_IMAGE);
			this.aBoss = false;
		}
		this.alive = true;
		this.type = type;
		Random r = new Random();
		speed = r.nextInt(Minion.MAX_SPEED)+1; //Randomize Initial Speed
		moveRight = false;
	}

	//method that changes the x position of the fish
	void move(){
		if (this.x + this.speed >= SplashScreen.WINDOW_WIDTH || this.x - this.speed <= 0) {
			moveRight = !moveRight;
		}
		if(moveRight){
			this.x += this.speed;
		}else if(!moveRight){
			this.x -= this.speed;
		}
	}

	//getter
	public boolean isAlive() {
		return this.alive;
	}

	public boolean isBossAlive() {
		return this.bossAlive;
	}

	public boolean isABoss() {
		return this.aBoss;
	}

	public int getHealth() {
		return this.health;
	}

	//BOSS METHODS
	private void die(){
		this.alive = false;
		this.vanish();
	}

	private void getHit(int damage){ //damages the boss fish
		this.health -= damage;
		if(this.health<=0){
			this.die();
		}
	}

	//Checks the interaction between the ship and the minion or boss
	void checkCollision(Ship ship){
		//TIMER TASK FOR INVULNERIBILITY
		Timer shipTimer = new Timer();
		TimerTask shipTask = new TimerTask(){
			private int duration = 2; //Duration of 2 seconds
			public void run(){
				ship.setState(true);
				this.duration --;
				System.out.println("Duration: " + this.duration);
				if(this.duration == 0){ //If duration is equal to 0, the ship will be vulnerable again
					ship.setState(false);
					cancel();
				}
			}
		};


		if(type == 0){
			for	(int i = 0; i < ship.getBullets().size(); i++)	{
				if (this.collidesWith(ship.getBullets().get(i))){ //If minion is hit by a bullet, it dies
					this.alive = false;
					this.vanish();
					ship.addMinionKilled();
					ship.getBullets().get(i).setVisible(false);
					System.out.println("Number of creeps killed: " + ship.getMinionKilled()); //remove
				}
			}
			if(this.collidesWith(ship)){ //If state is equal to true, ship cant be damaged
				if(ship.getState() != true){ //If minion gets hit by ship, it inflicts damage to the ship and dies
					this.alive = false;
					this.vanish();
					ship.addMinionKilled();
					ship.die(Minion.MINION_DAMAGE);
					System.out.println("Number of creeps killed: " + ship.getMinionKilled()); // remove
				}

			}
		}else{ //This is the condition for the boss
			for	(int i = 0; i < ship.getBullets().size(); i++)	{
				if (this.collidesWith(ship.getBullets().get(i))){ //Reduces the health of the boss fish if hit by a bullet
					System.out.println("Boss Health: " + this.health); //remove
					this.getHit(ship.getStrength());
					ship.getBullets().get(i).setVisible(false);
				}
			}
			if(this.collidesWith(ship)){ //If boss gets hit by ship, it inflicts damage to the ship and dies
				if(ship.getState() != true){
					System.out.println("Boss Health: " + this.health); //remove
					ship.die(Minion.BOSS_DAMAGE);
					this.getHit(ship.getStrength());

					if (this.health <= 0) {
						this.bossAlive = false;
					}

					shipTimer.scheduleAtFixedRate(shipTask,0,1000); //After getting hit, the ship will become invulnerable for a while
				}
			}
		}
	}
}
