package application;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * rectangleWithText class creates an object which is a rectagular block with text on it.
 * Fields include -
 * COLOR_SET - String of colors, the color on block depends on the value associated to it.
 * rectText - Text on the Rectangle (rectBody)
 * rectBody - Rectangle Object
 */
class rectangleWithText extends StackPane {
	
	private static final String[] COLOR_SET = {"#2cffff", "#2bffa3", "#74ff2d", "#f8fd2e", "#fd6f2d", "#fe552c"}; 
	
	private Text rectText;
	private Rectangle rectBody;

	/**
	 * getter for text on rectangle
	 * @return String i.e. the text on rectangle
	 */
	public Text getRectText() {
		return rectText;
	}

	/**
	 * setter for text on rectangle
	 * @param newText String i.e. the text on rectangle
	 */
	public void setRectText(String newText) {
		this.rectText.setText(newText);
	}

	/**
	 * getter for the Rectangle Object
	 * @return Rectangle
	 */
	public Rectangle getRectBody() {
		return rectBody;
	}

	/**
	 * returns the centre of the Rectangle Object
	 * @return
	 */
	public Point2D getRectCenter() {
		return new Point2D(this.getTranslateX() + 30, this.getTranslateY() + 30);
	}

	/**
	 * Constructor for the rectangleWithText, creates a rectangle of length and breadth = 60, with text on it.
	 * @param x  X coordinate of the upper-left corner of the Rectangle.
	 * @param y  Y coordinate of the upper-left corner of the Rectangle.
	 * @param value  Value associated (or the text) on the Rectangle
	 */
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

/**
 * Block is a rectangle with value (or text) associated to it, destroying block increments the score and changes the snake length accordingly.
 * Field -
 * value - integer value associated to each Block.
 */
public class Block extends GameObject implements Interactable{

	private int value;

	/**
	 * Constructor for Block creates a rectanglewithText, where text is the value associated to the block.
	 * @param x  X coordinate of the upper-left corner of the rectanglewithText.
	 * @param y  Y coordinate of the upper-left corner of the rectanglewithText.
	 * @param value Integer value associated to each block
	 * @param speed speed of the gameplay
	 */
	public Block(double x, double y, int value, double speed) {
		
		super(new rectangleWithText(x, y, value), speed);
		this.value = value;
		this.getFallDownTimer().start();
		
	}

	/**
	 * getter for value of block
	 * @return Integer Value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * collide method describes collision of user snake with the block
	 * <p>
	 *     Collision depends on the presence of shield by the snake, decrementing block value corresponds to incrementing the same value to the score.
	 *     In case of Shield the block is destroyed without any change in length of snake,
	 *     otherwise the snake's length and block value are decremented gradually by one step at a time, if the block's value is <= 5.
	 * </p>
	 * @param snake
	 */
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

	/**
	 * changeValue is used for changing the value of block by a certain delta. If block value gets to zero the block gets destroyed and burstAnimation is used for the same.
	 * @param minus - minus is the value to be to decremented by of the block.
	 */
	private void changeValue(int minus) {
		
		if(minus > 1) {
			ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
			GameScene.root.getChildren().add(burstAnimation);
		}
		this.value -= minus;
		((rectangleWithText) this.getView()).setRectText(this.value + "");
	
	}

	/**
	 *  destroy method destroy's the block and increments the score by the value of the block.
	 * @param snake - User Snake
	 */

	public void destroy(Snake snake) {
		
		setAlive(false);
		GameScene.setGameScore(GameScene.getGameScore() + this.value);
		ParticleBurst burstAnimation = new ParticleBurst(this.getView().getTranslateX(), this.getView().getTranslateY(), (Color) ((Rectangle)((rectangleWithText) this.getView()).getRectBody()).getFill());
		GameScene.root.getChildren().add(burstAnimation);
	
	}
	
}
