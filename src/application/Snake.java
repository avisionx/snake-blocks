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

/**
 * followingCircle is used to describe the remaining body of the snake excluding the head
 * extends Circle
 */
class followingCircle extends Circle{

	/**
	 * constructor for followingCircle
	 * @param x - x coordinate of the center of circle
	 * @param y - y coordinate of the center of circle
	 * @param radius - radius of circle
	 * @param color - color of the circle
	 */
	public followingCircle(double x, double y, double radius, Color color) {
		super(x,y,radius, color);
	}

	/**
	 * updates the position of each circle to get smooth motion by following the path of the parent circle
	 * @param parentX - X coordinate of parent circle (next circle towards the head of snake)
	 * @param parentY - Y coordinate of the parent circle
	 * @param self - followingCircle object for self
	 */
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

/**
 * Snake Class to describe the snake in the game
 * extends Group
 * Fields -
 * length - length of snake
 * snakeText - text on top of the snake denoting the length of it
 * snakeHead - points to the head of the snake
 * snakeBody - List of followingCircle which forms the body of Snake
 * snakeSpeed - speed of snake
 * xVelocity - velocity across x direction of snake
 * snakeSideCollideBlock - boolean true if snake's side collides with block
 * snakeMotion AnimationTimer
 * hasMagnet - boolean
 * curMagnet - Magnet object for the current Magnet
 * hasShield - boolean
 * curShield - Shield object for the current Shield
 */
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

	/**
	 * setter for snakeSideCollideBlock
	 * @param snakeSideCollideBlock - boolean value to set on.
	 */
	public void setSnakeCollideBlock(boolean snakeSideCollideBlock) {
		this.snakeSideCollideBlock = snakeSideCollideBlock;
	}

	/**
	 * returns the head of snake
	 * @return GameObject i.e. the head of snake
	 */
	public GameObject getSnakeHead() {
		return this.snakeHead;
	}

	/**
	 * getter for SnakeSpeed
	 * @return double value denoting snake speed
	 */
	public double getSnakeSpeed() {
		return this.snakeSpeed;
	}

	/**
	 * gives the current position of the snake head
	 * @return Point2D object having the current x and y coordinate for the snake
	 */
	public Point2D getSnakeHeadPosPoint2D() {
		double x = ((Circle)this.snakeHead.getView()).getCenterX();
		double y = ((Circle)this.snakeHead.getView()).getCenterY();
		return new Point2D(x, y);
	}

	/**
	 * move left
	 */
	public void moveLeft() {
		this.xVelocity = -this.snakeSpeed;
	}

	/**
	 * move right
	 */
	public void moveRight() {
		this.xVelocity = +this.snakeSpeed;
	}

	/**
	 * stops the motion of snake
	 */
	public void stopSnake() {
		this.xVelocity = 0;
	}

	/**
	 * setter for snake speed
	 * @param snakeSpeed - double value denoting the speed of snake to be set to
	 */
	public void setSpeed(double snakeSpeed) {
		this.snakeSpeed = snakeSpeed;
	}

	/**
	 * getter for length of  snake
	 * @return integer denoting length of snake
	 */
	public int getSnakeLength() {
		return this.length;
	}

	/**
	 * constructor for snake
	 * @param length - length of snake
	 * @param x - x coordinate of the snake
	 */
	public Snake(int length, double x) {
		
		super();
		
		this.length = length;
		this.snakeSpeed = 400;
		this.snakeSideCollideBlock = false;
		this.hasMagnet = false;
		this.curMagnet = null;
		this.hasShield = false;
		this.curShield = null;
		
		double sceneHeight = Main.getSceneheight();
		
		this.snakeHead = new GameObject(new Circle(x, sceneHeight*8/10, 9, Color.web("#fedc0f")));
		
		this.snakeText = new Text(this.length + "");
		this.snakeText.setFill(Color.WHITE);
		this.snakeText.getStyleClass().add("circleFont");
		
		this.snakeText.setTranslateX(x - this.snakeText.getBoundsInLocal().getWidth()*2/3);
		this.snakeText.setTranslateY(sceneHeight * 7.7 / 10);
		this.snakeText.setTextOrigin(VPos.CENTER);
		
		this.getChildren().addAll(this.snakeText, this.snakeHead.getView());
		
		this.snakeBody = new ArrayList<>();
		
		for(int i = 1; i < Math.min(this.length, 8); i++) {
			
			followingCircle nextCircle = new followingCircle(x, sceneHeight*8/10 + 18*i, 9, Color.web("#fedc0f"));
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

	/**
	 * setter for snake length
	 * @param newLength - value to which the snake's length is to be changed to
	 */
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

	/**
	 * method responsible for the movement of the head of snake
	 * @param deltaX - change in the X coordinate of the snake
	 */
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