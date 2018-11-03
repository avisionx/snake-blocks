package application;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class circleWithText extends VBox {
	
	public circleWithText(double x, double y, double radius, Color color, int value) {
		super();
		Circle circleBody = new Circle(x, y, radius, color);
		Text circleText = new Text(value + "");
		circleText.setFill(Color.WHITE);
		circleText.getStyleClass().add("circleFont");
		circleText .setBoundsType(TextBoundsType.VISUAL); 
		this.getChildren().addAll(circleText, circleBody);
		this.setSpacing(5);
		this.setAlignment(Pos.CENTER);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	
}

public class Ball extends GameObject implements Token{

	private int value;
	
	public Ball(double x, double y, int value, double speed) {
		super(new circleWithText(x, y, 13, Color.web("#ffdd0c"), value), speed);
		this.value = value;
	}

	@Override
	public void collide(Snake snake) {
		System.out.println("COLLIDE WITH BALL OF VALUE: " + this.value);
	}
	
}
