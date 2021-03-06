package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * Shield Class is used to represent shield powerup objects in game
 * extends GameObject
 * implements Token
 * Field - Duration - Duration of shield
 */
public class Shield extends GameObject implements Token{

	private static final ImagePattern SHIELD_IMAGE;
	private static final Color ALT_COLOR = Color.BLANCHEDALMOND; 
	
	private double duration;
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
			SHIELD_IMAGE = new ImagePattern(new Image(fileUrl));
		else
			SHIELD_IMAGE = null;
	}

	/**
	 * Constructor for Shield
	 * @param x - x coordinate for shield
	 * @param y - y coordinate for shield
	 * @param speed - speed of game
	 */
	
	public Shield(double x, double y, double speed) {
		
		super(new Circle(x, y, 16, Color.MAGENTA), speed);
		((Circle)this.getView()).setFill(SHIELD_IMAGE != null ? SHIELD_IMAGE : ALT_COLOR);
		
		this.getFallDownTimer().start();
		this.duration = 5;
		this.shieldTimer = null;
		
	}

	/**
	 * increases the duration by delta
	 * @param addDuration - value to be added to duration
	 */
	public void addDuration(double addDuration) {
		this.duration += addDuration;
	}

	/**
	 * getter for Duration
	 * @return - double value i.e. the duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * handles collision with snake
	 * @param snake - current user snake
	 */
	@Override
	public void collide(Snake snake) {
		
		if(snake.hasShield) {
			snake.curShield.duration += 5;
		}
		else {
			
			snake.hasShield = true;
			snake.curShield = this;
			
			GameScene.setShieldOn();
			
			this.shieldTimer = new AnimationTimer() {
				
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
			
			this.shieldTimer.start();
			
		}
		
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.WHITE);
		GameScene.root.getChildren().add(burstAnimation);
		
	}
	
}
