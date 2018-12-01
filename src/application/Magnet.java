package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Magnet extends GameObject implements Token{
	
	private static final ImagePattern magnetImage;
	private static final Color altColor = Color.ALICEBLUE; 
	private double duration;
	protected AnimationTimer magnetTimer;
	
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
		this.duration = 5;
		this.magnetTimer = null;
		
	}
	
	public void addDuration(double d) {
		this.duration += d;
	}

	@Override
	public void collide(Snake snake) {
		
		if(snake.hasMagnet) {
			snake.curMagnet.duration += 5;
		}
		else {
			snake.hasMagnet = true;
			snake.curMagnet = this;
			
			GameScene.setMagnetOn();
			
			magnetTimer = new AnimationTimer() {
				
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
							if(snakePos.distance(ballPos) < 200) {
								ball.attract(snakePos);
							}
						}
						
					}
					else {
						lastUpdateFrameTime = now;
					}
				}
				
			};
			
			magnetTimer.start();
			
		}
		
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), Color.WHITE);
		GameScene.root.getChildren().add(burstAnimation);
		
	}
	
}
