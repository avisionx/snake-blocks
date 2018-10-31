package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall extends GameObject implements Interactable{

	public Wall(double x, double length, double speed) {
		super(new Rectangle(10, length, Color.CORNFLOWERBLUE), speed);
		((Rectangle)this.getView()).setX(x);
		((Rectangle)this.getView()).setY(-10 - length);
	}

	@Override
	public void collide() {
		System.out.println("COLLIDE WITH WALL");
	}
	
}
