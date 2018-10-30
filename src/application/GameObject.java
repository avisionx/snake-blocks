package application;

import javafx.scene.Node;

public class GameObject {

	private Node view;
	private float speed;
	
	private boolean alive;
	
	public GameObject(Node view) {
		this.view = view;
		this.speed = 0;
		this.alive = true;
	}
	
	public GameObject(Node view, float speed) {
		this.view = view;
		this.speed = speed;
		this.alive = true;
	}	
	
	public Node getView() {
		return view;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void update() {
		view.setTranslateY(view.getTranslateY() + speed);
	}	
	
	
	
}
