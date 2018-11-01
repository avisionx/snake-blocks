package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Shield extends GameObject implements Token{

	double duration;
	
	public Shield(double x, double y, double speed) {
		super(new Circle(x, y, 10, Color.MAGENTA), speed);
		this.duration = 8;
	}

	@Override
	public void collide() {
		System.out.println("POWER UP: SHIELD");
	}
	
}
