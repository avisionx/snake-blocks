package application;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;


/**
 * GameObject is the parent class for all the objects of the game.
 * Field -
 * view - Node view
 * alive - boolean value tells whether the object is alive or not
 * fallDownMotionTimer - AnimationTimer object for the gameplay
 * speed - speed of the gameplay
 */
public class GameObject {

	private Node view;
	private boolean alive;
	private AnimationTimer fallDownMotionTimer;
	private double speed;

	/**
	 * Constructor for GameObject with only one parameter
	 * @param view - Node view
	 */
	public GameObject(Node view) {
		this.view = view;
		this.speed = 0;
		this.alive = true;
		this.fallDownMotionTimer = null;
	}

	/**
	 * Constructor for GameObject with view and speed as parameter
	 * @param view - Node view
	 * @param speed - speed of the gameplay
	 */
	
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

	/**
	 * getter for view
	 * @return Node view
	 */
	public Node getView() {
		return this.view;
	}

	/**
	 * returns whether the GameObject is dead or not.
	 * @return
	 */
	public boolean isDead() {
		return !this.alive;
	}

	/**
	 * Setter for boolean value alive for the GameObject
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * getter for the AnimationTimer associated to the object
	 * @return AnimationTimer
	 */
	public AnimationTimer getFallDownTimer() {
		return this.fallDownMotionTimer;
	}

	/**
	 * setter for the speed of the gameplay
	 * @param speed - speed of the gameplay i.e. the object
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * returns true if the GameObject is colliding to the one in the parameter.
	 * @param other - GameObject to check the collision with
	 * @return boolean true if its colliding, false otherwise.
	 */
	public boolean isColliding(GameObject other) {
		return this.getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
	}

	/**
	 * update method is used for the forward motion of the gameplay i.e. updates the speed  and sets translation of the objects.
	 */
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
