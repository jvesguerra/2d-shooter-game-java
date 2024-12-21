package miniproject;

import javafx.scene.image.Image;

class StrengthPotion extends PowerUp{

	public final static int POWER_UP_WIDTH=30;
	public final static Image RED_POTION = new Image("images/RED_POTION.png",StrengthPotion.POWER_UP_WIDTH,StrengthPotion.POWER_UP_WIDTH,false,false);
	public final static int STRENGTH_BUFF=50;

	StrengthPotion(int x, int y){
		super(x,y);
		this.loadImage(StrengthPotion.RED_POTION);
	}

	void checkCollision(Ship ship){
		if(this.collidesWith(ship)){
			System.out.println(ship.getName() + " has collected a strength potion!");
			ship.AddStrength(STRENGTH_BUFF);
			System.out.println(" Current Strength: " + ship.getStrength());
			this.vanish();
		}
	}
}
