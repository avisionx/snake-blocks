package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Snake extends GameObject{

	private int length;
	private double xCord;
	private double yCord;
	
	public Snake(int length) {
		
		super(new Circle(Main.getScenewidth()/2, Main.getSceneheight()*9/10, 10, Color.AQUA));
		this.length = length;
		this.xCord = this.getView().getTranslateX();
		this.yCord = this.getView().getTranslateY();
		
	}

	public void update() {
		return;
	}
	
}