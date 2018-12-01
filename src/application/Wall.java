package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends GameObject implements Interactable{

	public Wall(double x, double y, double length, double speed) {
		
		super(new Rectangle(5, length, Color.WHITE), speed);
		((Rectangle)this.getView()).setArcHeight(7);
		((Rectangle)this.getView()).setArcWidth(7);
		((Rectangle)this.getView()).setX(x);
		y += y == -202 ? length : 0;
		((Rectangle)this.getView()).setY(y - length);
		this.getFallDownTimer().start();
		
	}

	@Override
	public void collide(Snake snake) {
		// TODO Auto-generated method stub
	}
	
}
