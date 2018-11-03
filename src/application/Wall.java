package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends GameObject implements Interactable{

	public Wall(double x, double length, double speed) {
		super(new Rectangle(5, length, Color.WHITE), speed);
		((Rectangle)this.getView()).setArcHeight(7);
		((Rectangle)this.getView()).setArcWidth(7);
		((Rectangle)this.getView()).setX(x);
		((Rectangle)this.getView()).setY(-10 - length);
	}

	@Override
	public void collide(Snake snake) {
		// TODO Auto-generated method stub
		
	}
	
}
