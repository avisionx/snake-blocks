package application;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

class GamePauseHandler implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		GameScene.pauseGame();
	}

	public void handle(KeyEvent e) {
		GameScene.pauseGame();
	}
	
}

class GameResumeHandler implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent e) {
		GameScene.resumeGame();
	}

	public void handle(KeyEvent e) {
		GameScene.resumeGame();
	}
	
}

public class GameScene extends Scene {
	
	private static Pane root;
	private static Snake userSnake;
	private static List<GameObject> tokens = new ArrayList<>();
	private static List<Block> blocks = new ArrayList<>();
	private static List<Wall> walls = new ArrayList<>();
	private static double gameSpeed;	
	private static Button pauseButton;
	private static GamePauseHandler gamePauseHandler;
	private static GameResumeHandler gameResumeHandler;
	private static int occur = 0;
	
	private static AnimationTimer mainFrameTimer = new AnimationTimer() {
		
		@Override
		public void handle(long now) {
			updateEachFrame();
		}
		
	};
	
	private static AnimationTimer populationTimer = new AnimationTimer() {
		
		double lastPopulateTime = System.currentTimeMillis();
		
		@Override
		public void handle(long now) {
			
			double elapsedTimeInSec = (now-lastPopulateTime)/1_000_000_000.0 ;
			if(elapsedTimeInSec >= 1) {
				populateNewItems();
				lastPopulateTime = now;
			}
		}
		
	};
	
	public GameScene() {
		
		super(new Pane(createContent()), Main.getScenewidth(), Main.getSceneheight());	
		
		this.setOnKeyPressed(e -> {	
			if(e.getCode() == KeyCode.A) {
				userSnake.moveLeft();
			}
			else if(e.getCode() == KeyCode.D) {
				userSnake.moveRight();
			}
			else if(e.getCode() == KeyCode.P) {
				gamePauseHandler.handle(e);
			}
			else if(e.getCode() == KeyCode.R) {
				gameResumeHandler.handle(e);
			}
		});
		
		this.setOnKeyReleased(e -> {	
			if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
				userSnake.stopSnake();
			}
		});
		
	}
	
	protected static void pauseGame() {
		stopFallAnimation();
		populationTimer.stop();
		userSnake.setSpeed(0);
	}
	
	private static void stopFallAnimation() {
		for (GameObject token : tokens) {
			token.getFallDownTimer().stop();
		}
		for (Wall wall : walls) {
			wall.getFallDownTimer().stop();
		}
		for (Block block : blocks) {
			block.getFallDownTimer().stop();
		}
	}
	
	protected static void resumeGame() {
		resumeFallAnimation();
		populationTimer.start();
		userSnake.setSpeed(400);
	}
	
	private static void resumeFallAnimation() {
		for (GameObject token : tokens) {
			token.getFallDownTimer().start();
		}
		for (Wall wall : walls) {
			wall.getFallDownTimer().start();
		}
		for (Block block : blocks) {
			block.getFallDownTimer().start();
		}
	}

	private static Parent createContent() {
		
		root = new Pane();
		root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		root.getStyleClass().add("rootBg");
		
		gameSpeed = 4.5;
		
		pauseButton = new Button();
		pauseButton.getStyleClass().add("pauseBtn");
		pauseButton.setTranslateX(Main.getScenewidth() - 45);
		pauseButton.setTranslateY(Main.getSceneheight() - 45);
		
		gamePauseHandler = new GamePauseHandler();
		pauseButton.setOnAction(gamePauseHandler);
		
		gameResumeHandler = new GameResumeHandler();
		
		root.getChildren().add(pauseButton);

		userSnake =  new Snake(10);
		addSnake(userSnake);
		
		mainFrameTimer.start();
		populationTimer.start();
		
		return root;
		
	}
	
	private static void addSnake(Snake snake) {
		root.getChildren().add(snake);
		pauseButton.toFront();
	}
	
	private static void addGameObject(GameObject object) {
		root.getChildren().add(object.getView());
		userSnake.toFront();
		pauseButton.toFront();
	}
	
	private static void updateEachFrame() {
		
		for(GameObject token : tokens) {
			
			if(token.isColliding(userSnake.getSnakeHead())) {
				token.setAlive(false);
				((Interactable) token).collide(userSnake);
				root.getChildren().remove(token.getView());
			}
			if(token.isDead()) {
				root.getChildren().remove(token.getView());
			}
			
		}
		
		for(Block block : blocks) {
			if(block.isColliding(userSnake.getSnakeHead())) {
				block.setAlive(false);
				block.collide(userSnake);
				root.getChildren().remove(block.getView());
			}
			if(block.isDead()) {
				root.getChildren().remove(block.getView());
			}
		}
		
		for(Wall wall : walls) {
			if(wall.isColliding(userSnake.getSnakeHead())) {
				wall.collide(userSnake);
			}
			if(wall.isDead()) {
				root.getChildren().remove(wall.getView());
			}
		}
		
		tokens.removeIf(GameObject::isDead);
		blocks.removeIf(GameObject::isDead);
		walls.removeIf(GameObject::isDead);
		
	}
	
	public static void populateNewItems() {
		
		Ball b = new Ball(Math.random()*(Main.getScenewidth()-40), -20, (int)(1 + Math.floor(Math.random()*10)), gameSpeed);
		DestroyBlocks db = new DestroyBlocks(2 + Math.random()*(Main.getScenewidth()-17), -20, gameSpeed);
		Magnet m = new Magnet(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);
		Shield s = new Shield(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);

		if(isSafe(b)){tokens.add(b); addGameObject(b); occur++;}
		if(isSafe(m) && occur%20 == 0){tokens.add(m); addGameObject(m); }
		if(isSafe(db) && occur%30 == 0){tokens.add(db); addGameObject(db); }
		if(isSafe(s) && occur% 25 == 0){tokens.add(s); addGameObject(s); }

		Block bb = new Block( 2 + Math.random()*(Main.getScenewidth() - 62), -20, (int)(1 + Math.floor(Math.random()*56)), gameSpeed);
		blocks.add(bb); addGameObject(bb);
		
		Wall w = new Wall( 2 + Math.random()*(Main.getScenewidth() - 7), 80 + Math.random()*200, gameSpeed);
		if(isSafe(w)){ walls.add(w); addGameObject(w); }
		
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

	static GameObject collideWall(){
		for(GameObject W : walls){
			if(W.isColliding(userSnake.getSnakeHead())) {
				return W;
			}
		}
		return null;
	}
}