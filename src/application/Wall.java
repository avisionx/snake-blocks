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
	
	public Wall(Double x, Double y, Double length, double speed, boolean b) {
		super(new Rectangle(5, length, Color.WHITE), speed);
		
		((Rectangle)this.getView()).setArcHeight(7);
		((Rectangle)this.getView()).setArcWidth(7);
		((Rectangle)this.getView()).setX(x);
		((Rectangle)this.getView()).setY(y);
		this.getFallDownTimer().start();
	}

	public double getLength() {
		return ((Rectangle)this.getView()).getHeight();
	}

	@Override
	public void collide(Snake snake) {
		return;
	}
	
}
