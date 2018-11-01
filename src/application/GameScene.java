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
	private static List<GameObject> tokens = new ArrayList<>();
	private static List<Block> blocks = new ArrayList<>();
	private static List<Wall> walls = new ArrayList<>();
	
	private static double lastUpdateTime;
	private static double gameSpeed;
	
	public static double getGameSpeed() {
		return gameSpeed;
	}

	
	public static void setGameSpeed(double newGameSpeed) {
		GameScene.gameSpeed = newGameSpeed;
		tokens.forEach(GameObject::setSpeed);
		blocks.forEach(GameObject::setSpeed);
		walls.forEach(GameObject::setSpeed);
	}
	
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
		
		root.getStyleClass().add("rootBg");
		gameSpeed = 5;
		userSnake =  new Snake(10);
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
		
		for(GameObject object : tokens) {
			if(object.isColliding(userSnake.getSnakeHead())) {
				object.setAlive(false);
				((Interactable) object).collide();
				root.getChildren().remove(object.getView());
			}
			if(object.isDead()) {
				root.getChildren().remove(object.getView());
			}
		}
		
		for(GameObject object : blocks) {
			if(object.isColliding(userSnake.getSnakeHead())) {
				object.setAlive(false);
				((Block)object).collide();
				root.getChildren().remove(object.getView());
			}
			if(object.isDead()) {
				root.getChildren().remove(object.getView());
			}
		}
		
		for(GameObject object : walls) {
			if(object.isColliding(userSnake.getSnakeHead())) {
				((Wall)object).collide();
			}
		}
		
		tokens.removeIf(GameObject::isDead);
		blocks.removeIf(GameObject::isDead);
		tokens.forEach(GameObject::update);
		blocks.forEach(GameObject::update);
		walls.forEach(GameObject::update);
		
		if(Math.random() < 0.02) {
			Ball b = new Ball(Math.random()*(Main.getScenewidth()-40), -10, 4, gameSpeed);
			DestroyBlocks db = new DestroyBlocks(Math.random()*(Main.getScenewidth()-10), -10, gameSpeed);
			Magnet m = new Magnet(Math.random()*(Main.getScenewidth()-10), -10, gameSpeed);
			Shield s = new Shield(Math.random()*(Main.getScenewidth()-10), -10, gameSpeed);
			tokens.add(b);
			tokens.add(db);
			tokens.add(m);
			tokens.add(s);
			addGameObject(b);
			addGameObject(m);
			addGameObject(db);
			addGameObject(s);
		}
		
		if(Math.random() < 0.02) {
			Block b = new Block(Math.random()*(Main.getScenewidth()-50), -10, 9, gameSpeed);
			blocks.add(b);
			addGameObject(b);
		}
		
		if(Math.random() < 0.02) {
			Wall w = new Wall(Math.random()*(Main.getScenewidth()-10), 80 + Math.random()*200, gameSpeed);
			walls.add(w);
			addGameObject(w);
		}
		
	}
	
}