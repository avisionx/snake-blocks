package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class circleWithText extends StackPane {
	
	public circleWithText(int x, int y, int radius, Color color, int length) {
		super();
		Circle headCircle = new Circle(x, y, radius, color);
		Text headText = new Text(length + "");
		headText .setBoundsType(TextBoundsType.VISUAL); 
		this.getChildren().addAll(headCircle, headText);
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
	
}

class followingCircle extends Circle{
	
	public followingCircle(double x, double y, double radius, Color color) {
		super(x,y,radius, color);
	}
	
	public void update(double rateX, double finalX, followingCircle bodyElem) {
		
		AnimationTimer moveAnimation = new AnimationTimer() {
			
			double reachedPos = bodyElem.getTranslateX();
			
			@Override
			public void handle(long now) {
				double moveDistanceThisFrame = rateX;
				double oldLocation = reachedPos;
				double newLocation = oldLocation + moveDistanceThisFrame;
				if(Math.abs(newLocation) > Math.abs(finalX)) {
					newLocation = finalX;
				}
				bodyElem.setTranslateX(newLocation);
				if(Math.abs(reachedPos) >= Math.abs(newLocation)) {
					this.stop();
				}
				reachedPos = newLocation;
			}
		};
		
		moveAnimation.start();
		
	}
}

public class Snake extends Group {
	
	private GameObject snakeHead;
	private List<followingCircle> snakeBody;
	private int length;
	private double xCord;
	private double yCord;
	public double xVelocity = 0;
	public double speed = 300;
	public static double minDist = -185;
	public static double maxDist = +185;
	
	public Snake(int length) {
		
		super();
		snakeHead = new GameObject(new Circle(Main.getScenewidth()/2, Main.getSceneheight()*8.7/10, 9, Color.RED));
		this.length = length;
		this.xCord = this.snakeHead.getView().getTranslateX();
		this.yCord = this.snakeHead.getView().getTranslateY();
		this.getChildren().add(snakeHead.getView());
		this.snakeBody = new ArrayList<>();
		for(int i = 1; i < Math.min(this.length, 6); i++) {
			followingCircle nextCircle = new followingCircle(Main.getScenewidth()/2, Main.getSceneheight()*8.7/10 + 18*i, 9, Color.AQUA);
			this.snakeBody.add(nextCircle);
			this.getChildren().add(nextCircle);
		}
		
		
	}
	
	public double getxCord() {
		return xCord;
	}
	
	public double getyCord() {
		return yCord;
	}
	
	public GameObject getSnakeHead() {
		return snakeHead;
	}
	
	public List<followingCircle> getSnakeBody() {
		return snakeBody;
	}
	
	public void moveHead(double oldX, double newX) {
	
		this.snakeHead.getView().setTranslateX(newX);
		
		if(this.length >= 2) {
			double delta = newX/1.4;
			this.snakeBody.get(0).update(delta, newX, this.snakeBody.get(0));
			for(int i = 1; i < this.snakeBody.size(); i++) {
				delta/=1.4;
				followingCircle bodyElem = this.snakeBody.get(i);
				bodyElem.update(delta, newX, bodyElem);				
			}
		}
		
	}	
	
}