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

class circleWithText extends VBox {
	
	Circle circleBody;
	
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

public class Ball extends GameObject implements Token { 

	private int value;
	
	public Ball(double x, double y, int value, double speed) {
		super(new circleWithText(x, y, 13, Color.web("#ffdd0c"), value), speed);
		this.value = value;
		this.getFallDownTimer().start();
	}

	@Override
	public void collide(Snake snake) {
		snake.setSnakeLength(snake.getSnakeLength() + this.value);
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.web("#ffdd0c"));
		GameScene.root.getChildren().add(burstAnimation);	
	}

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

	public Point2D getPos2D() {
		return new Point2D(this.getView().getTranslateX(), this.getView().getTranslateY());
	}
	
}
