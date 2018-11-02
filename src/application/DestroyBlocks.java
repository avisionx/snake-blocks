package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class DestroyBlocks extends GameObject implements Token{

	double duration;
	
	public DestroyBlocks(double x, double y, double speed) {
		super(new Circle(x, y, 16), speed);
		try {
			((Circle)this.getView()).setFill(new ImagePattern(new Image(new FileInputStream("./img/destroyBlockImage.png"))));
		} catch (FileNotFoundException e) {
			System.out.println("Destroy Block Image Not Found");
			((Circle)this.getView()).setFill(Color.ALICEBLUE);
			e.printStackTrace();
		}
		this.duration = 10;
	}

	@Override
	public void collide() {
		System.out.println("POWER UP: DESTROY ALL BLOCKS");
	}
	
}
