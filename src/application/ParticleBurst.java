package application;

import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * This class describes the Animation for bursting of particles.
 * extends Group
 */

public class ParticleBurst extends Group {

	/**
	 * Constructor which creates the ParticleBurst Object which creates the burst animation using methods of java.util.Random.
	 * @param x - x coordinate of the particle to be burst
	 * @param y - y coordinate of the particle to be burst
	 * @param c - Color of the particle to be burst.
	 */
	public ParticleBurst(double x, double y, Color c) {
		
		super();
		
		Random r = new Random();
		
		for(int i = 0; i < r.nextInt(5)+10; i++) {
			
			Circle particle = new Circle(x, y, 3 + r.nextInt(3), c);
			TranslateTransition t = new TranslateTransition();
			FadeTransition f = null;
			double xSign = r.nextBoolean() ? +1 : -1;
			double ySign = r.nextBoolean() ? +1 : -1;
			
			t.setDuration(Duration.seconds(r.nextFloat()));
			t.setCycleCount(1);
			t.setToX(particle.getCenterX() + xSign*r.nextInt(20));
			t.setToY(particle.getCenterY() + ySign*r.nextInt(20));
			t.setNode(particle);
			t.play();
			
			f = new FadeTransition(t.getDuration(), particle);
			f.setFromValue(1.0);
		    f.setToValue(0);
			f.play();
			
			this.getChildren().add(particle);
		
		}
		
	}
	
}
