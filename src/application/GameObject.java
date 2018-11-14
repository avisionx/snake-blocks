package application;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;

public class GameObject {

	private Node view;
	private double speed;
	private AnimationTimer fallDownMotionTimer;
	private boolean alive;
	
	public GameObject(Node view) {
		this.view = view;
		this.speed = 0;
		this.alive = true;
		fallDownMotionTimer = null;
	}
	
	public GameObject(Node view, double speed) {
		
		this.view = view;
		this.speed = speed;
		this.alive = true;
		this.fallDownMotionTimer = new AnimationTimer() {
			
			@Override
			public void handle(long arg0) {
				update();
			}
		};
		
	}	
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public AnimationTimer getFallDownTimer() {
		return fallDownMotionTimer;
	}
	
	public Node getView() {
		return view;
	}
	
	public boolean isDead() {
		return !alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isColliding(GameObject other) {
		return this.getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	}
	
	public void update() {
		
		view.setTranslateY(view.getTranslateY() + speed);
		if(Wall.class == this.getClass()) {
			if(view.getTranslateY() > 1000) {
				this.alive = false;
			}
		}
		else if(view.getTranslateY() > 700) {
			this.alive = false;
		}
	}	
	
}
