package application;

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

class snakeBody extends Group{
	
	public snakeBody(int x, int y, int radius, Color color, int length) {
		super();
		this.getChildren().add(new Circle(0, 0, radius, color));
		for (int i = 1; i < Math.min(length, 7); i++) {
			this.getChildren().add(new Circle(0, 2*i*radius, radius, color));
		}
		this.setTranslateX(x + radius);
		this.setTranslateY(y + radius);
	}
	
	public void moveHead(double oldX, double newX) {
		System.out.println(newX);
		this.setTranslateX(newX);
	}
}

public class Snake extends GameObject{

	private int length;
	private double xCord;
	private double yCord;
	public double xVelocity = 0;
	public double speed = 300;
	public static double minVelocity = 0;
	public static double maxVelocity = 600;
	
	public Snake(int length) {
		super(new snakeBody(Main.getScenewidth()/2, Main.getSceneheight()*8/10, 15, Color.AQUA, length));
		this.length = length;
		this.xCord = this.getView().getTranslateX();
		this.yCord = this.getView().getTranslateY();
		
	}
	
	public void moveLeft() {
		this.getView().setTranslateX(this.getView().getTranslateX() - 10);
	}
	
	public void moveRight() {
		this.getView().setTranslateX(this.getView().getTranslateX() + 10);
	}

	public void update() {
		return;
	}	
	
}