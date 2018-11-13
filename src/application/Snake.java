package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

class followingCircle extends Circle{
	
	public followingCircle(double x, double y, double radius, Color color) {
		super(x,y,radius, color);
	}
	
	public void update(double parentX, double parentY, followingCircle self) {
		
		AnimationTimer moveAnimation = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				
				Point2D parentPos = new Point2D(parentX, parentY);
				Point2D selfPos = new Point2D(self.getCenterX(), self.getCenterY());
				
				if(parentPos.distance(selfPos) > 18) {
					Point2D orientation = parentPos.subtract(selfPos);
					self.setCenterX(selfPos.getX() + orientation.getX());
				}
				else {
					this.stop();
				}
				
			}
		};
		moveAnimation.start();
		
	}
}

public class Snake extends Group {
	
	private Text snakeText;
	private GameObject snakeHead;
	private List<followingCircle> snakeBody;
	private int length;
	private double xVelocity;
	private double xSpeed = 400;
	
	private AnimationTimer snakeMotion = new AnimationTimer() {
		
		double lastMotionTime;
		
		@Override
		public void handle(long now) {
			if(lastMotionTime > 0) {
				double elapsedTimeInSec = (now-lastMotionTime)/1_000_000_000.0;
				double moveDistanceThisFrame = elapsedTimeInSec * xVelocity;
				moveHead(moveDistanceThisFrame);
			}
			lastMotionTime = now;
		}
		
	};
	
	public GameObject getSnakeHead() {
		return snakeHead;
	}
	
	public void moveLeft() {
		xVelocity = -xSpeed;
	}
	
	public void moveRight() {
		xVelocity = +xSpeed;
	}
	
	public void stopSnake() {
		xVelocity = 0;
	}
	
	public Snake(int length) {
		
		super();
		this.snakeHead = new GameObject(new Circle(Main.getScenewidth()/2, Main.getSceneheight()*8/10, 9, Color.web("#fedc0f")));
		this.snakeText = new Text(length + "");
		this.snakeText.setFill(Color.WHITE);
		this.snakeText.getStyleClass().add("circleFont");
		this.snakeText.setTranslateX(Main.getScenewidth()/2 - this.snakeText.getBoundsInLocal().getWidth()*2/3);
		this.snakeText.setTranslateY(Main.getSceneheight() * 7.7/10);
		this.snakeText.setTextOrigin(VPos.CENTER);
		this.getChildren().addAll(snakeText, snakeHead.getView());
		this.snakeBody = new ArrayList<>();
		this.length = length;
		for(int i = 1; i < Math.min(this.length, 8); i++) {
			followingCircle nextCircle = new followingCircle(Main.getScenewidth()/2, Main.getSceneheight()*8/10 + 18*i, 9, Color.web("#fedc0f"));
			this.snakeBody.add(nextCircle);
			this.getChildren().add(nextCircle);
		}
		snakeMotion.start();
		
	}
	
	public void moveHead(double deltaX) {
		
		double oldHeadX = ((Circle)this.snakeHead.getView()).getCenterX();
		double oldTextX = this.snakeText.getTranslateX();
		double newHeadX = oldHeadX + deltaX;
		double newHeadY = ((Circle)this.snakeHead.getView()).getCenterY();
		this.snakeText.setTranslateX(oldTextX + deltaX);
		((Circle)this.snakeHead.getView()).setCenterX(newHeadX);
		
		if(this.length >= 2) {
			
			this.snakeBody.get(0).update(newHeadX, newHeadY, this.snakeBody.get(0));
			
			for(int i = 1; i < this.snakeBody.size(); i++) {
				followingCircle prevBodyElem = this.snakeBody.get(i-1);
				followingCircle curBodyElem = this.snakeBody.get(i);
				curBodyElem.update(prevBodyElem.getCenterX(), prevBodyElem.getCenterY(), curBodyElem);
			
			}
			
		}
		
	}	
	
}