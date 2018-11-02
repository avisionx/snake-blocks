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
	private static int occur = 0;
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
				double before = oldLocation;
				double after = newLocation;
				userSnake.moveHead(oldLocation , newLocation);
				if(collideWall()){ userSnake.moveHead(after , before); }
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
		gameSpeed = 4.5;
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
			Ball b = new Ball(Math.random()*(Main.getScenewidth()-40), -20, (int)(1 + Math.floor(Math.random()*10)), gameSpeed);
			DestroyBlocks db = new DestroyBlocks(2 + Math.random()*(Main.getScenewidth()-17), -20, gameSpeed);
			Magnet m = new Magnet(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);
			Shield s = new Shield(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);

			if(isSafe(b)){tokens.add(b); addGameObject(b); occur++;}
			if(isSafe(m) && occur%20 == 0){tokens.add(m); addGameObject(m); }
			if(isSafe(db) && occur%30 == 0){tokens.add(db); addGameObject(db); }
			if(isSafe(s) && occur% 25 == 0){tokens.add(s); addGameObject(s); }

		}
		
		if(Math.random() < 0.02) {
			Block b = new Block( 2 + Math.random()*(Main.getScenewidth() - 62), -20, (int)(1 + Math.floor(Math.random()*56)), gameSpeed);
			if(isSafe(b)) {blocks.add(b); addGameObject(b);}
		}
		
		if(Math.random() < 0.02) {
			Wall w = new Wall( 2 + Math.random()*(Main.getScenewidth() - 7), 80 + Math.random()*200, gameSpeed);
			if(isSafe(w)){ walls.add(w); addGameObject(w); }
		}
		
	}

	static boolean isSafe(GameObject G){
		for(GameObject T : tokens){
			if(T.isColliding(G)){return false;}
		}
		for(GameObject W : walls){
			if(W.isColliding(G)){return false;}
		}
		for(GameObject B : blocks){
			if(B.isColliding(G)){return false;}
		}
		return true;
	}

	static boolean collideWall(){
		for(GameObject W : walls){
			if(W.isColliding(userSnake.getSnakeHead())){return true;}
		}
		return false;
	}
	
}