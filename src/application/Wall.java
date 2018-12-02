package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Wall class to represent the walls in the game
 */
public class Wall extends GameObject implements Interactable{

	/**
	 * constructor for wall
	 * @param x - x coordinate of the wall
	 * @param y - y coordinate of the top end of the wall
	 * @param length - length of the wall
	 * @param speed - speed of the game
	 */
	public Wall(double x, double y, double length, double speed) {
		
		super(new Rectangle(5, length, Color.WHITE), speed);
		
		((Rectangle)this.getView()).setArcHeight(7);
		((Rectangle)this.getView()).setArcWidth(7);
		((Rectangle)this.getView()).setX(x);
		
		y += y == -202 ? length : 0;
		((Rectangle)this.getView()).setY(y - length);
		
		this.getFallDownTimer().start();
		
	}

	/**
	 * Constructor for wall
	 * @param x - x coordinate of the wall
	 * @param y - y coordinate of the top end of the wall
	 * @param length - length of the wall
	 * @param speed - speed of the game
	 */
	public Wall(Double x, Double y, Double length, double speed, boolean b) {
		super(new Rectangle(5, length, Color.WHITE), speed);
		
		((Rectangle)this.getView()).setArcHeight(7);
		((Rectangle)this.getView()).setArcWidth(7);
		((Rectangle)this.getView()).setX(x);
		((Rectangle)this.getView()).setY(y);
		this.getFallDownTimer().start();
	}

	/**
	 * getter for the length of wall
	 * @return double type value denoting the length of wall
	 */
	public double getLength() {
		return ((Rectangle)this.getView()).getHeight();
	}

	/**
	 * Overridden collide method
	 * @param snake - current user snake
	 */
	@Override
	public void collide(Snake snake) {
		return;
	}
	
}
