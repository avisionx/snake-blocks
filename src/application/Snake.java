package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
	
	private int length;
	private Text snakeText;
	private GameObject snakeHead;
	private List<followingCircle> snakeBody;
	private double snakeSpeed;
	private double xVelocity;
	private boolean snakeSideCollideBlock;
	private AnimationTimer snakeMotion;
	
	protected boolean hasMagnet;
	protected Magnet curMagnet;
	protected boolean hasShield;
	protected Shield curShield;
	
	public void setSnakeCollideBlock(boolean snakeSideCollideBlock) {
		this.snakeSideCollideBlock = snakeSideCollideBlock;
	}
	
	public GameObject getSnakeHead() {
		return this.snakeHead;
	}
	
	public Point2D getSnakeHeadPosPoint2D() {
		double x = ((Circle)this.snakeHead.getView()).getCenterX();
		double y = ((Circle)this.snakeHead.getView()).getCenterY();
		return new Point2D(x, y);
	}
	
	public void moveLeft() {
		this.xVelocity = -this.snakeSpeed;
	}
	
	public void moveRight() {
		this.xVelocity = +this.snakeSpeed;
	}
	
	public void stopSnake() {
		this.xVelocity = 0;
	}
	
	public void setSpeed(double snakeSpeed) {
		this.snakeSpeed = snakeSpeed;
	}

	public int getSnakeLength() {
		return this.length;
	}
	
	public Snake(int length) {
		
		super();
		
		this.length = length;
		this.snakeSpeed = 400;
		this.snakeSideCollideBlock = false;
		this.hasMagnet = false;
		this.curMagnet = null;
		this.hasShield = false;
		this.curShield = null;
		
		double sceneWidth = Main.getScenewidth();
		double sceneHeight = Main.getSceneheight();
		
		this.snakeHead = new GameObject(new Circle(sceneWidth/2, sceneHeight*8/10, 9, Color.web("#fedc0f")));
		
		this.snakeText = new Text(this.length + "");
		this.snakeText.setFill(Color.WHITE);
		this.snakeText.getStyleClass().add("circleFont");
		
		this.snakeText.setTranslateX(sceneWidth / 2 - this.snakeText.getBoundsInLocal().getWidth()*2/3);
		this.snakeText.setTranslateY(sceneHeight * 7.7 / 10);
		this.snakeText.setTextOrigin(VPos.CENTER);
		
		this.getChildren().addAll(this.snakeText, this.snakeHead.getView());
		
		this.snakeBody = new ArrayList<>();
		
		for(int i = 1; i < Math.min(this.length, 8); i++) {
			
			followingCircle nextCircle = new followingCircle(sceneWidth/2, sceneHeight*8/10 + 18*i, 9, Color.web("#fedc0f"));
			this.snakeBody.add(nextCircle);
			this.getChildren().add(nextCircle);
		}
		
		
		this.snakeMotion = new AnimationTimer() {
			
			double lastMotionTime;
			
			@Override
			public void handle(long now) {
				
				if(lastMotionTime > 0) {
					
					double elapsedTimeInSec = (now-lastMotionTime)/1_000_000_000.0;
					double moveDistanceThisFrame = elapsedTimeInSec * xVelocity;
					double snakeHeadPos = ((Circle)snakeHead.getView()).getCenterX();
					
					GameObject collidingWall = GameScene.collideWall();
					
					if(collidingWall != null){
						
						Rectangle collidingRect = ((Rectangle)((Wall)collidingWall).getView());
						
						double wallCenterX = collidingRect.getX() + 2.5;
						boolean moved = false;
						
						if(snakeHeadPos < wallCenterX - 2.5) {
							
							if(moveDistanceThisFrame == 0) {
								
								moveDistanceThisFrame = wallCenterX - 11.5 - snakeHeadPos;
								moved = true;
							
							}else if(snakeHeadPos == wallCenterX - 11.5 && moveDistanceThisFrame > 0) {
								
								moveDistanceThisFrame = 0;
								moved = true;
							
							}else if(snakeHeadPos + moveDistanceThisFrame >= wallCenterX - 2.5) {
								
								moveDistanceThisFrame = wallCenterX - 11.5 - snakeHeadPos;
								moved = true;
							
							}
						}
						else if(snakeHeadPos > wallCenterX + 2.5) {
							
							if(moveDistanceThisFrame == 0) {
								
								moveDistanceThisFrame = wallCenterX + 11.5 - snakeHeadPos;
								moved = true;
							
							}else if(snakeHeadPos == wallCenterX + 11.5  && moveDistanceThisFrame < 0) {
								
								moveDistanceThisFrame = 0;
								moved = true;
							
							}
							else if(snakeHeadPos + moveDistanceThisFrame <= wallCenterX + 2.5) {
								
								moveDistanceThisFrame = wallCenterX + 11.5 - snakeHeadPos;
								moved = true;
							
							}
						}
						else {
							
							if(snakeHeadPos > wallCenterX) {
								
								moveDistanceThisFrame = wallCenterX + 11.5 - snakeHeadPos;
							
							}
							else {
								
								moveDistanceThisFrame = wallCenterX - 11.5 - snakeHeadPos;
							
							}
							
							moved = true;
						
						}
						if(moved) {
							
							moveHead(moveDistanceThisFrame);
							moveDistanceThisFrame = 0;
						
						}
						else {
							
							if(snakeHeadPos + moveDistanceThisFrame > 390) {
								moveDistanceThisFrame = 390 - snakeHeadPos;
							}
							else if(snakeHeadPos + moveDistanceThisFrame < 10) {
								moveDistanceThisFrame = 10 - snakeHeadPos;
							}
							
							moveHead(moveDistanceThisFrame);
						
						}
					}
					else if(snakeSideCollideBlock && GameScene.collidingWithBlock == null) {
						
						moveDistanceThisFrame = 0;
						moveHead(moveDistanceThisFrame);
						snakeSideCollideBlock = false;
					
					}
					else {					
						
						moveDistanceThisFrame = elapsedTimeInSec * xVelocity;
						
						if(snakeHeadPos + moveDistanceThisFrame > 390) {
							moveDistanceThisFrame = 390 - snakeHeadPos;
						}
						else if(snakeHeadPos + moveDistanceThisFrame < 10) {
							moveDistanceThisFrame = 10 - snakeHeadPos;
						}
						
						moveHead(moveDistanceThisFrame);
					
					}
				}
				
				lastMotionTime = now;
			
			}
			
		};
		
		this.snakeMotion.start();
		
	}
	
	public void setSnakeLength(int newLength) {
		
		int oldLength = this.length;
		this.length = newLength;
		
		this.snakeText.setText(newLength + "");
		
		if(this.length <= 0) {
			
			this.length = 0;
			this.getChildren().remove(2, this.getChildren().size());
			this.snakeText.setText("0");
			
			GameScene.gameOver();
			return;
		
		}
		
		if(oldLength > newLength) {
			
			if(newLength < 8)
				this.getChildren().remove(newLength+1, this.getChildren().size());
		}
		else {
			
			if(oldLength < 8)
				this.getChildren().addAll(this.snakeBody.subList(oldLength-1, Math.min(newLength-1, 7)));
		
		}
		
		GameScene.setGameSpeed();
	
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