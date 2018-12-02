package application;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * circleWithText class is to describe a Circle object(circleBody) with a value or text associated to it.
 * The class extends VBox to do the same.
 */

class circleWithText extends VBox {
	
	Circle circleBody;

	/**
	 * Constructor which creates a circle with text floating on top of it
	 * @param x - x coordinate position in which the Circle is to be placed
	 * @param y - y coordinate position
	 * @param radius - radius of the circle (circleBody)
	 * @param color - color of the circle (circleBody)
	 * @param value - value associated to the circle (White Color)
	 */
	public circleWithText(double x, double y, double radius, Color color, int value) {
		
		super();
		
		this.circleBody = new Circle(x, y, radius, color);
		Text circleText = new Text(value + "");
		
		circleText.setFill(Color.WHITE);
		circleText.getStyleClass().add("circleFont");
		circleText .setBoundsType(TextBoundsType.VISUAL); 
		
		this.getChildren().addAll(circleText, this.circleBody);
		
		this.setAlignment(Pos.CENTER);
		this.setSpacing(5);
		this.setTranslateX(x);
		this.setTranslateY(y);
	
	}
}

/**
 * Ball class describe the ball (or coins) to be collected by the user, extends GameObject and implements Token interface
 * Each ball has a integer value ("value") associated to it.
 */
public class Ball extends GameObject implements Token { 


	private int value;
	
	public Ball(double x, double y, int value, double speed) {
		super(new circleWithText(x, y, 13, Color.web("#ffdd0c"), value), speed);
		this.value = value;
		this.getFallDownTimer().start();
	}

	/**
	 * collide function is used to handle collision between snake and ball.
	 * In such case the snake's length is incremented by the value associated to the ball.
	 * ParticleBurst object is also used to show the burst animation.
	 * @param snake - Current user snake
	 */
	@Override
	public void collide(Snake snake) {
		snake.setSnakeLength(snake.getSnakeLength() + this.value);
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.web("#ffdd0c"));
		GameScene.root.getChildren().add(burstAnimation);	
	}

	/**
	 * getter for value associated to the ball
	 * @return integer value, i.e. Ball Value
	 */

	public int getValue() {
		return this.value;
	}

	/**
	 * attract method is used for the functioning of the magnet powerup.
	 * In this we set the translation of the ball according to the position of the snake.
	 * @param snakePos - Point2D object which gives the current position of the user snake.
	 */
	public void attract(Point2D snakePos) {
		
		this.getFallDownTimer().stop();
		
		new AnimationTimer() {
			
			@Override
			public void handle(long arg) {

				Node view = getView();
				Point2D newPosVector = snakePos.subtract(getPos2D()).normalize().multiply(2);
				
				view.setTranslateX(view.getTranslateX() + newPosVector.getX());
				view.setTranslateY(view.getTranslateY() + newPosVector.getY());
				
			}
			
		}.start();
	
	}

	/**
	 *
	 * @return Point2D object is returned which gives the current position of the ball
	 */

	public Point2D getPos2D() {
		return new Point2D(this.getView().getTranslateX(), this.getView().getTranslateY());
	}
	
}
