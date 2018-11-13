package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Shield extends GameObject implements Token{

	private static final ImagePattern shieldImage;
	private static final Color altColor = Color.BLANCHEDALMOND; 
	
	static{
		FileInputStream fileUrl = null;
		try {
			fileUrl = new FileInputStream("./img/shieldImage.png");
		}
		catch (FileNotFoundException e) {
			fileUrl = null;
		}
		if(fileUrl != null)
			shieldImage = new ImagePattern(new Image(fileUrl));
		else
			shieldImage = null;
	}
	
	public Shield(double x, double y, double speed) {
		
		super(new Circle(x, y, 16, Color.MAGENTA), speed);
		((Circle)this.getView()).setFill(shieldImage != null ? shieldImage : altColor);
		this.getFallDownTimer().start();
		
	}

	@Override
	public void collide(Snake snake) {
		System.out.println("POWER UP: SHIELD");
	}
	
}
