package application;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class GameObject {

	private Node view;
	private double speed;
	
	private boolean alive;
	
	public void setSpeed() {
		this.speed = GameScene.getGameSpeed();
	}
	
	public GameObject(Node view) {
		this.view = view;
		this.speed = 0;
		this.alive = true;
	}
	
	public GameObject(Node view, double speed) {
		this.view = view;
		this.speed = speed;
		this.alive = true;
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
		if(view.getClass() != Rectangle.class) {
			if(view.getTranslateY() > 700) {
				this.alive = false;
			}
		}
		else {
			if(view.getTranslateY() > 900) {
				this.alive = false;
			}
		}
	}	
	
}
