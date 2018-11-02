package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Magnet extends GameObject implements Token{

	double duration;
	
	public Magnet(double x, double y, double speed) {
		super(new Circle(x, y, 16), speed);
		try {
			((Circle)this.getView()).setFill(new ImagePattern(new Image(new FileInputStream("./img/magnetImage.png"))));
		} catch (FileNotFoundException e) {
			System.out.println("Magnet Image Not Found");
			((Circle)this.getView()).setFill(Color.BEIGE);
			e.printStackTrace();
		}
		this.duration = 5;
	}

	@Override
	public void collide() {
		System.out.println("POWER UP: MAGNET");
	}
	
}
