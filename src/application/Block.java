package application;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class rectangleWithText extends StackPane {
	
	public rectangleWithText(double x, double y, Color color, int value) {
		super();
		Rectangle rectBody = new Rectangle(x, y, 50, 50);
		rectBody.setFill(color);
		Text rectText = new Text(value+ "");
		rectText .setBoundsType(TextBoundsType.VISUAL); 
		this.getChildren().addAll(rectBody, rectText);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	
}

public class Block extends GameObject{

	public Block(double x, double y, int value, double speed) {
		super(new rectangleWithText(x, y, Color.BLUEVIOLET, value), speed);
	}
	
}
