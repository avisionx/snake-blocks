package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Shield extends GameObject implements Token{

	private static final ImagePattern shieldImage;
	private static final Color altColor = Color.BLANCHEDALMOND; 
	private int duration;
	protected AnimationTimer shieldTimer;
	
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
		this.duration = 5;
		this.shieldTimer = null;
		
	}

	@Override
	public void collide(Snake snake) {
		
		if(snake.hasShield) {
			snake.curShield.duration += 5;
		}
		else {
			
			snake.hasShield = true;
			snake.curShield = this;
			
			GameScene.setShieldOn();
			
			shieldTimer = new AnimationTimer() {
				
				double lastUpdateFrameTime = 0;
				
				@Override
				public void handle(long now) {
					
					if(lastUpdateFrameTime > 0) {
						
						double elapsedTimeInSec = (now-lastUpdateFrameTime)/1_000_000_000.0; 
						
						if(elapsedTimeInSec >= duration) {
							GameScene.setShieldOff();;
							snake.hasShield = false;
							snake.curShield = null;
							this.stop();
						}	

					}
					else {
						lastUpdateFrameTime = now;
					}
				}
				
			};
			
			shieldTimer.start();
			
		}
	}
	
}
