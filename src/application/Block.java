package application;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class rectangleWithText extends StackPane {
	
	private static final String[] COLOR_SET = {"#2cffff", "#2bffa3", "#74ff2d", "#f8fd2e", "#fd6f2d", "#fe552c"}; 
	
	private Text rectText;
	private Rectangle rectBody;
	
	public Text getRectText() {
		return rectText;
	}
	
	public void setRectText(String newText) {
		this.rectText.setText(newText);
	}
	
	public Rectangle getRectBody() {
		return rectBody;
	}
	
	public Point2D getRectCenter() {
		return new Point2D(this.getTranslateX() + 30, this.getTranslateY() + 30);
	}
	
	public rectangleWithText(double x, double y, int value) {
		
		super();
		this.rectBody = new Rectangle(x, y, 60, 60);
		
		if(value <= 5) {
			this.rectBody.setFill(Color.web(COLOR_SET[0]));
		}
		else if(value <= 15) {
			this.rectBody.setFill(Color.web(COLOR_SET[1]));
		}
		else if(value <= 30) {
			this.rectBody.setFill(Color.web(COLOR_SET[2]));
		}
		else if(value <= 38) {
			this.rectBody.setFill(Color.web(COLOR_SET[3]));
		}
		else if(value <= 45) {
			this.rectBody.setFill(Color.web(COLOR_SET[4]));
		} 
		else {
			this.rectBody.setFill(Color.web(COLOR_SET[5]));
		}
		
		this.rectBody.setArcHeight(10);
		this.rectBody.setArcWidth(10);
		
		this.rectText = new Text(value+ "");
		this.rectText.getStyleClass().add("boxFont");
		this.rectText.setBoundsType(TextBoundsType.VISUAL); 
		
		this.getChildren().addAll(rectBody, rectText);
		this.setTranslateX(x);
		this.setTranslateY(y);
		
	}
	
}

public class Block extends GameObject implements Interactable{

	private int value;
	
	public Block(double x, double y, int value, double speed) {
		
		super(new rectangleWithText(x, y, value), speed);
		this.value = value;
		this.getFallDownTimer().start();
		
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public void collide(Snake snake) {
		
		int snakeLength = snake.getSnakeLength();
		
		if(snakeLength > 0)
			if(!snake.hasShield) {
				if(snakeLength >= this.value && this.value <= 5) {
					
					GameScene.setGameScore(GameScene.getGameScore() + this.value);
					snake.setSnakeLength(snakeLength - this.value);
					
					changeValue(this.value);
					ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
					GameScene.root.getChildren().add(burstAnimation);
				
				}
				else {
					
					GameScene.setGameScore(GameScene.getGameScore() + 1);
					changeValue(1);
					snake.setSnakeLength(snakeLength - 1);
				
				}
			}
			else {
				
				GameScene.setGameScore(GameScene.getGameScore() + this.value);
				this.value = 0;
				ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
				GameScene.root.getChildren().add(burstAnimation);
			
			}
		else {
			GameScene.gameOver();
		}
	}
	
	private void changeValue(int minus) {
		
		if(minus > 1) {
			ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
			GameScene.root.getChildren().add(burstAnimation);
		}
		this.value -= minus;
		((rectangleWithText) this.getView()).setRectText(this.value + "");
	
	}

	public void destroy(Snake snake) {
		
		setAlive(false);
		GameScene.setGameScore(GameScene.getGameScore() + this.value);
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
		GameScene.root.getChildren().add(burstAnimation);
	
	}
	
}
