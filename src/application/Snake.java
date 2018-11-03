package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class followingCircle extends Circle{
	
	public followingCircle(double x, double y, double radius, Color color) {
		super(x,y,radius, color);
	}
	
	public void update(double parentX, double parentY, followingCircle bodyElem) {
		
		AnimationTimer moveAnimation = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				Point2D parentPos = new Point2D(parentX, parentY);
				Point2D selfPos = new Point2D(bodyElem.getCenterX(), bodyElem.getCenterY());
				if(parentPos.distance(selfPos) > 18) {
					Point2D orientation = parentPos.subtract(selfPos);
					bodyElem.setCenterX(selfPos.getX() + orientation.getX());
				}
			}
		};
		
		moveAnimation.start();
		
	}
}

public class Snake extends Group {
	
	private GameObject snakeHead;
	private List<followingCircle> snakeBody;
	private int length;
	public double xVelocity = 0;
	public double speed = 300;
	public static double minDist = -185;
	public static double maxDist = +185;
	
	public Snake(int length) {
		
		super();
		snakeHead = new GameObject(new Circle(Main.getScenewidth()/2, Main.getSceneheight()*8/10, 9, Color.web("#fedc0f")));
		this.length = length;
		this.getChildren().add(snakeHead.getView());
		this.snakeBody = new ArrayList<>();
		for(int i = 1; i < Math.min(this.length, 6); i++) {
			followingCircle nextCircle = new followingCircle(Main.getScenewidth()/2, Main.getSceneheight()*8/10 + 18*i, 9, Color.web("#fedc0f"));
			this.snakeBody.add(nextCircle);
			this.getChildren().add(nextCircle);
		}
		
	}
	
	public GameObject getSnakeHead() {
		return snakeHead;
	}
	
	public List<followingCircle> getSnakeBody() {
		return snakeBody;
	}
	
	public void moveHead(double newX) {

		((Circle)this.snakeHead.getView()).setCenterX(((Circle)this.snakeHead.getView()).getCenterX() + newX);
		
		if(this.length >= 2) {
			this.snakeBody.get(0).update(((Circle)this.snakeHead.getView()).getCenterX(), ((Circle)this.snakeHead.getView()).getCenterY(), this.snakeBody.get(0));
			for(int i = 1; i < this.snakeBody.size(); i++) {
				followingCircle bodyElem = this.snakeBody.get(i);
				bodyElem.update(this.snakeBody.get(i-1).getCenterX(), this.snakeBody.get(i-1).getCenterY(), bodyElem);
			}
		}
		
	}	
	
}