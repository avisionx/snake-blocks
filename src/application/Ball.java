package application;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class circleWithText extends StackPane {
	
	public circleWithText(double x, double y, double radius, Color color, int value) {
		super();
		Circle circleBody = new Circle(x, y, radius, color);
		Text circleText = new Text(value + "");
		circleText .setBoundsType(TextBoundsType.VISUAL); 
		this.getChildren().addAll(circleBody, circleText);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	
}

public class Ball extends GameObject{

	public Ball(double x, double y, int value, double speed) {
		super(new circleWithText(x, y, 20, Color.BISQUE, value), speed);
	}
	
}
