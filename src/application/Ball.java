package application;

import javafx.scene.paint.Color;

public class Ball extends GameObject{

	public Ball() {
		super(new circleWithText(200, 0, 15, Color.BISQUE, 10), 5);
	}
	
}
