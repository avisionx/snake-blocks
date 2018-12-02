package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * DestroyBlocks class describes the powerup which destroys all the blocks on the screen at that moment.
 * Extends GameObject and implements Token interface
 * extends GameObject, implements Token
 */
public class DestroyBlocks extends GameObject implements Token{
	
	private static final ImagePattern DESTROY_IMAGE;
	private static final Color ALT_COLOR = Color.RED; 
	
	static{
		
		FileInputStream fileUrl = null;
		
		try {
			fileUrl = new FileInputStream("./img/destroyBlockImage.png");
		}
		catch (FileNotFoundException e) {
			fileUrl = null;
		}
		if(fileUrl != null)
			DESTROY_IMAGE = new ImagePattern(new Image(fileUrl));
		else
			DESTROY_IMAGE = null;
	
	}

	/**
	 * DestroyBlocks Constructor, creates the powerup at the desired position and given speed
	 * @param x - x coordinate of the powerup
	 * @param y - y coordinate of the powerup
	 * @param speed - speed of the gameplay
	 */
	public DestroyBlocks(double x, double y, double speed) {
		
		super(new Circle(x, y, 16), speed);
		((Circle)this.getView()).setFill(DESTROY_IMAGE != null ? DESTROY_IMAGE : ALT_COLOR);
		this.getFallDownTimer().start();
		
	}

	/**
	 * collide method describes the collision of the powerup with a snake i.e. destroys all the block at the moment and add the value to the score
	 * @param snake - Current User Snake
	 */

	@Override
	public void collide(Snake snake) {
		
		List<Block> blockList = GameScene.getBlockList();
		
		for(Block block : blockList) {
			block.destroy(snake);
		}
		
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.WHITE);
		GameScene.root.getChildren().add(burstAnimation);
	
	}
	
}
