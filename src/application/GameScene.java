package application;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class GameScene extends Scene{
	
	private static Pane root;
	private static Snake userSnake;
	private static AnimationTimer mainTimer;
	public GameScene(int scenewidth, int sceneheight) {
		super(createContent(scenewidth, sceneheight), scenewidth, sceneheight);
		
	}

	private static Parent createContent(int scenewidth, int sceneheight) {
		root = new Pane();
		root.setPrefSize(scenewidth, sceneheight);
//		root.setStyle("-fx-background-color: #000;");
		userSnake =  new Snake(5);
		addGameObject(userSnake, 35, 35);
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				onUpdate();
			}
		};
		mainTimer = timer;
		timer.start();
		return root;
	}

	private static void addGameObject(Snake object, double x, double y) {
		root.getChildren().add(object.getView());
	}
	
	protected static void onUpdate() {
		userSnake.update();
		
	}
	
}