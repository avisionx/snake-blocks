package application;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameScene extends Scene {
	
	private static Pane root;
	private static Snake userSnake;
	private static List<Ball> balls = new ArrayList<>();
	static double lastUpdateTime;
	
	private static AnimationTimer mainTimer = new AnimationTimer() {
		
		@Override
		public void handle(long now) {
			
			double moveDistanceThisFrame;
			if(lastUpdateTime > 0) {
				double elapsedTimeInSec = (now-lastUpdateTime)/1_000_000_000.0 ;
				moveDistanceThisFrame = elapsedTimeInSec * userSnake.xVelocity;
				double oldLocation = userSnake.getSnakeHead().getView().getTranslateX();
				double newLocation = oldLocation + moveDistanceThisFrame;
				if(Math.abs(newLocation) > 185) {
					newLocation = newLocation/Math.abs(newLocation)*185;
				}
				userSnake.moveHead(oldLocation , newLocation);
			}
			
			lastUpdateTime = now;
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
			else if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
				userSnake.xVelocity = -userSnake.speed;
			}
			else if(e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
				userSnake.xVelocity = +userSnake.speed;
			}
		});
		
		setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
			      userSnake.xVelocity = 0;
			}
		});
		
	}

	public static Parent createContent() {

		root = new Pane();
		root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		root.getChildren().add(new Circle(0,0,5, Color.WHITE));
		root.getStyleClass().add("rootBg");
		
		userSnake =  new Snake(12);
		
		addSnake(userSnake);
		
		mainTimer.start();
		
		return root;
		
	}
	
	private static void addSnake(Snake snake) {
		root.getChildren().add(snake);
	}
	
	private static void addGameObject(GameObject object) {
		root.getChildren().add(object.getView());
	}
	
	protected static void onUpdate() {
		
		for(GameObject object : balls) {
			if(object.isColliding(userSnake.getSnakeHead())) {
				System.out.println("Collide");
				object.setAlive(false);
				root.getChildren().remove(object.getView());
			}
		}
		
		balls.removeIf(GameObject::isDead);
		balls.forEach(GameObject::update);
		
		if(Math.random() < 0.02) {
			Ball b = new Ball();
			balls.add(b);
			addGameObject(b);
		}
		
	}
	
}