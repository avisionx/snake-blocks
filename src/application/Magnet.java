package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Magnet extends GameObject implements Token{
	
	private static final ImagePattern magnetImage;
	private static final Color altColor = Color.ALICEBLUE; 
	
	static{
		FileInputStream fileUrl = null;
		try {
			fileUrl = new FileInputStream("./img/magnetImage.png");
		}
		catch (FileNotFoundException e) {
			fileUrl = null;
		}
		if(fileUrl != null)
			magnetImage = new ImagePattern(new Image(fileUrl));
		else
			magnetImage = null;
	}
	
	public Magnet(double x, double y, double speed) {
			
		super(new Circle(x, y, 16), speed);
		((Circle)this.getView()).setFill(magnetImage != null ? magnetImage : altColor);
		this.getFallDownTimer().start();
		
	}

	@Override
	public void collide(Snake snake) {
		System.out.println("POWER UP: MAGNET");
	}
	
}
