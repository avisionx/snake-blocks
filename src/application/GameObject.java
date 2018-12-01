package application;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;

public class GameObject {

	private Node view;
	private boolean alive;
	private AnimationTimer fallDownMotionTimer;
	private double speed;
	
	public GameObject(Node view) {
		this.view = view;
		this.speed = 0;
		this.alive = true;
		this.fallDownMotionTimer = null;
	}
	
	public GameObject(Node view, double speed) {
		
		this.view = view;
		this.speed = speed;
		this.alive = true;
		this.fallDownMotionTimer = new AnimationTimer() {
			
			@Override
			public void handle(long arg) {
				update();
			}
			
		};
		
	}	
	
	public Node getView() {
		return this.view;
	}
	
	public boolean isDead() {
		return !this.alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public AnimationTimer getFallDownTimer() {
		return this.fallDownMotionTimer;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public boolean isColliding(GameObject other) {
		return this.getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	}
	
	public void update() {
		double oldY = this.view.getTranslateY();
		double newY = oldY + this.speed;
		
		this.view.setTranslateY(newY);
		
		if(Wall.class == this.getClass()) {
			if(newY > 1400) {
				this.alive = false;
			}
		}
		else if(newY > 900) {
			this.alive = false;
		}
	}	
	
}
