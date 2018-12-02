package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * Magnet class to represent Magnet in game
 * Fields -
 * duration - duration for which the magnet lasts
 */
public class Magnet extends GameObject implements Token{
	
	private static final ImagePattern MAGNET_IMAGE;
	private static final Color ALT_COLOR = Color.ALICEBLUE; 
	private static final int MAGNET_RANGE = 200;
	
	private double duration;
	private AnimationTimer magnetTimer;
	
	static{
		
		FileInputStream fileUrl = null;
		
		try {
			fileUrl = new FileInputStream("./img/magnetImage.png");
		}
		catch (FileNotFoundException e) {
			fileUrl = null;
		}
		if(fileUrl != null)
			MAGNET_IMAGE = new ImagePattern(new Image(fileUrl));
		else
			MAGNET_IMAGE = null;
	
	}

	/**
	 * Constructor for Magnet
	 * @param x - x coordinate of magnet
	 * @param y - y coordinate of magnet
	 * @param speed - speed of game
	 */
	public Magnet(double x, double y, double speed) {
			
		super(new Circle(x, y, 16), speed);
		((Circle)this.getView()).setFill(MAGNET_IMAGE != null ? MAGNET_IMAGE : ALT_COLOR);
		this.getFallDownTimer().start();
		this.duration = 5;
		this.magnetTimer = null;
		
	}

	/**
	 * method to increase duration
	 * @param addDuration - value to be added to duration
	 */
	public void addDuration(double addDuration) {
		this.duration += addDuration;
	}

	/**
	 * getter for duration
	 * @return double type value i.e. duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * method to start the magnetTimer i.e. AnimationTimer for magnet
	 */
	public void startMagnetTimer() {
		this.magnetTimer.start();
	}
	/**
	 * method to stop the magnetTimer i.e. AnimationTimer for magnet
	 */
	public void stopMagnetTimer() {
		this.magnetTimer.stop();
	}

	/**
	 * method to handle collision with snake
	 * @param snake - current user snake
	 */
	@Override
	public void collide(Snake snake) {
		
		if(snake.hasMagnet) {
			snake.curMagnet.duration += 5;
		}
		else {
			
			snake.hasMagnet = true;
			snake.curMagnet = this;
			
			GameScene.setMagnetOn();
			
			this.magnetTimer = new AnimationTimer() {
				
				double lastUpdateFrameTime = 0;
				
				@Override
				public void handle(long now) {
					
					if(lastUpdateFrameTime > 0) {
						
						double elapsedTimeInSec = (now-lastUpdateFrameTime)/1_000_000_000.0; 
						if(elapsedTimeInSec >= duration) {
							GameScene.setMagnetOff();
							snake.hasMagnet = false;
							snake.curMagnet = null;
							this.stop();
						}	
						
						for (Ball ball : GameScene.getBallList()) {
							
							Point2D snakePos = snake.getSnakeHeadPosPoint2D(); 
							Point2D ballPos = ball.getPos2D();
							if(snakePos.distance(ballPos) < MAGNET_RANGE) {
								ball.attract(snakePos);
							}
							
						}
						
					}
					else {
						lastUpdateFrameTime = now;
					}
				}
				
			};
			
			this.magnetTimer.start();
			
		}
		
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.WHITE);
		GameScene.root.getChildren().add(burstAnimation);
		
	}
	
}
