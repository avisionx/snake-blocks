package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Magnet extends GameObject implements Token{

	double duration;
	
	public Magnet(double x, double y, double speed) {
		super(new Circle(x, y, 10, Color.YELLOW), speed);
		this.duration = 5;
	}

	@Override
	public void collide() {
		System.out.println("POWER UP: MAGNET");
	}
	
}
