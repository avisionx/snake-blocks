package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class GameScene extends Scene {
	
	private static Pane root;
	private static Snake userSnake;
	static List<Ball> balls = new ArrayList<>();
	
	private static AnimationTimer mainTimer = new AnimationTimer() {
		
		@Override
		public void handle(long now) {
			onUpdate();
		}
		
	};
	
	public GameScene() {
		super(new Pane(createContent()), Main.getScenewidth(), Main.getSceneheight());
		setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.P) {
				mainTimer.stop();
			}
			else if(e.getCode() == KeyCode.R) {
				mainTimer.start();
			}
			System.out.println(e.getCode());
		});
		
	}

	public static Parent createContent() {

		root = new Pane();
		root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		root.getStyleClass().add("rootBg");
		
		userSnake =  new Snake(5);
		
		addGameObject(userSnake);
		
		mainTimer.start();
		
		return root;
	}

	private static void addGameObject(GameObject object) {
		root.getChildren().add(object.getView());
	}
	
	protected static void onUpdate() {
		
		for(GameObject object : balls) {
			if(object.isColliding(userSnake)) {
				System.out.println("Collide");
				object.setAlive(false);
				root.getChildren().remove(object.getView());
			}
		}
		
		balls.removeIf(GameObject::isDead);
		balls.forEach(GameObject::update);
		userSnake.update();
		
		if(Math.random() < 0.02) {
			Ball b = new Ball();
			balls.add(b);
			addGameObject(b);
		}
	}
	
}