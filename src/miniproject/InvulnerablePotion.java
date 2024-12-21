package miniproject;

import javafx.scene.image.Image;
import java.util.Timer;
import java.util.TimerTask;

class InvulnerablePotion extends PowerUp{

	public final static int INVUL_UP_WIDTH=30;
	private final static int POTION_DURATION = 4;
	public final static Image GREEN_POTION = new Image("images/GREEN_POTION.png",StrengthPotion.POWER_UP_WIDTH,StrengthPotion.POWER_UP_WIDTH,false,false);


	InvulnerablePotion(int x, int y){
		super(x,y);
		this.loadImage(InvulnerablePotion.GREEN_POTION);
	}

	void checkCollision(Ship ship){
		Timer myTimer = new Timer();
		TimerTask task = new TimerTask(){
			private int duration = InvulnerablePotion.POTION_DURATION; //Duration of 3 seconds
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
		if(ship.getState() != true){
			if(this.collidesWith(ship)){
				System.out.println(ship.getName() + " has collected a invulnerable potion!");
				System.out.println(" Current Strength: " + ship.getStrength());
				this.vanish();

				myTimer.scheduleAtFixedRate(task,0,1000);
			}
		}

	}

}
