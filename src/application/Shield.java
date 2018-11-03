package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Shield extends GameObject implements Token{

	double duration;
	
	public Shield(double x, double y, double speed) {
		super(new Circle(x, y, 16, Color.MAGENTA), speed);
		try {
			((Circle)this.getView()).setFill(new ImagePattern(new Image(new FileInputStream("./img/shieldImage.png"))));
		} catch (FileNotFoundException e) {
			System.out.println("Shield Image Not Found");
			((Circle)this.getView()).setFill(Color.DARKMAGENTA);
			e.printStackTrace();
		}
		this.duration = 8;
	}

	@Override
	public void collide(Snake snake) {
		System.out.println("POWER UP: SHIELD");
	}
	
}
