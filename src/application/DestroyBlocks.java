package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DestroyBlocks extends GameObject implements Token{

	double duration;
	
	public DestroyBlocks(double x, double y, double speed) {
		super(new Circle(x, y, 10, Color.RED), speed);
		this.duration = 10;
	}

	@Override
	public void collide() {
		System.out.println("POWER UP: DESTROY ALL BLOCKS");
	}
	
}
