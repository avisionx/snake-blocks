package application;

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

public class Snake extends GameObject{

	private int length;
	private double xCord;
	private double yCord;
	
	public Snake(int length) {
		
		super(new circleWithText(Main.getScenewidth()/2, Main.getSceneheight()*9/10, 10, Color.AQUA, length));
		this.length = length;
		this.xCord = this.getView().getTranslateX();
		this.yCord = this.getView().getTranslateY();
		
	}

	public void update() {
		return;
	}
	
}