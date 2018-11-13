package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class DestroyBlocks extends GameObject implements Token{
	
	private static final ImagePattern destroyImage;
	private static final Color altColor = Color.RED; 
	static{
		FileInputStream fileUrl = null;
		try {
			fileUrl = new FileInputStream("./img/destroyBlockImage.png");
		}
		catch (FileNotFoundException e) {
			fileUrl = null;
		}
		if(fileUrl != null)
			destroyImage = new ImagePattern(new Image(fileUrl));
		else
			destroyImage = null;
	}
	
	public DestroyBlocks(double x, double y, double speed) {
		
		super(new Circle(x, y, 16), speed);
		((Circle)this.getView()).setFill(destroyImage != null ? destroyImage : altColor);
		this.getFallDownTimer().start();
	
		
	}
	

	@Override
	public void collide(Snake snake) {
		System.out.println("POWER UP: DESTROY ALL BLOCKS");
	}
	
}
