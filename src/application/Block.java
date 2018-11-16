package application;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

class rectangleWithText extends StackPane {
	
	private Text rectText;
	private Rectangle rectBody;
	private static final String[] colorSet = {"#2cffff", "#2bffa3", "#74ff2d", "#f8fd2e", "#fd6f2d", "#fe552c"}; 
	
	public Rectangle getRectBody() {
		return rectBody;
	}
	
	public Text getRectText() {
		return rectText;
	}
	
	public Point2D getRectCenter() {
		return new Point2D(this.getTranslateX() + 30, this.getTranslateY() + 30);
	}
	
	public rectangleWithText(double x, double y, int value) {
		
		super();
		
		rectBody = new Rectangle(x, y, 60, 60);
		
		if(value <= 5) {
			rectBody.setFill(Color.web(colorSet[0]));
		}
		else if(value <= 15) {
			rectBody.setFill(Color.web(colorSet[1]));
		}
		else if(value <= 30) {
			rectBody.setFill(Color.web(colorSet[2]));
		}
		else if(value <= 38) {
			rectBody.setFill(Color.web(colorSet[3]));
		}
		else if(value <= 45) {
			rectBody.setFill(Color.web(colorSet[4]));
		} 
		else {
			rectBody.setFill(Color.web(colorSet[5]));
		}
		
		rectBody.setArcHeight(10);
		rectBody.setArcWidth(10);
		
		this.rectText = new Text(value+ "");
		rectText.getStyleClass().add("boxFont");
		rectText.setBoundsType(TextBoundsType.VISUAL); 
		
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

	@Override
	public void collide(Snake snake) {
		if(snake.getSnakeLength() >= this.value)
			GameScene.setGameScore(GameScene.getGameScore() + this.value);
		else
			GameScene.setGameScore(GameScene.getGameScore() + snake.getSnakeLength());
		snake.setSnakeLength(snake.getSnakeLength() - this.value);
	}
	
	public void destroy(Snake snake) {
		GameScene.setGameScore(GameScene.getGameScore() + this.value);
		setAlive(false);
	}
	
}
