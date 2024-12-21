package miniproject;

abstract class PowerUp extends Sprite{
	PowerUp(int x, int y){
		super(x,y);
	}

	abstract void checkCollision(Ship myShip);
}
