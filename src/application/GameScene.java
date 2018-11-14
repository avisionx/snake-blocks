package application;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

class pauseScreenButton extends Button{
	
	public pauseScreenButton(String name){
		super(name);
		this.getStyleClass().add("pasueScreenBtn");
	}
	
}

class pauseScreen extends StackPane {
	
	public pauseScreen(int scoreToDisplay) {
		
		super();
		
		Rectangle background = new Rectangle(0, 0, Main.getScenewidth(), Main.getSceneheight());
		background.setFill(Color.rgb(0, 0, 0, 0.5));
		
		VBox displayItems = new VBox(30);
		
		HBox scoreWithCrown = new HBox(10);
		Text scoreText = new Text(scoreToDisplay + "");
		scoreText.getStyleClass().add("pauseScoreText");
		scoreText.setFill(Color.WHITE);
		Image corwnImage = null;
		try {
			String pathToImage = "./img/scoreCrown.png";
			corwnImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		ImageView crownImageView = null;
		if(corwnImage != null) {
			crownImageView = new ImageView(corwnImage);
			crownImageView.setX(0);
			crownImageView.setY(0);
			crownImageView.setFitWidth(45);
			crownImageView.setPreserveRatio(true);  
		}
		scoreWithCrown.setAlignment(Pos.CENTER);
		scoreWithCrown.getChildren().addAll(crownImageView, scoreText);
		
		Button resumeBtn = new pauseScreenButton("Resume");
		Button newGameBtn = new pauseScreenButton("New Game");
		Button goToMenuBtn = new pauseScreenButton("Main Menu");
		Button exitBtn = new pauseScreenButton("Exit");
		
		resumeBtn.setOnAction(e -> {
			GameScene.gameResumeHandler.handle(e);
		});
		
		newGameBtn.setOnAction(e -> {
			GameScene gameScene = new GameScene(GameScene.primaryStage, GameScene.mainMenuScene);
			GameScene.primaryStage.setScene(gameScene);
			gameScene.getStylesheets().add(
	        		getClass().getResource("application.css").toExternalForm()
	        	);
		});
		
		goToMenuBtn.setOnAction(e -> {
			GameScene.primaryStage.setScene(GameScene.mainMenuScene);
		});
		
		exitBtn.setOnAction(e -> {
			System.exit(0);
		});
		
		displayItems.getChildren().addAll(scoreWithCrown, resumeBtn, newGameBtn, goToMenuBtn, exitBtn);
		displayItems.setAlignment(Pos.CENTER);
		this.getChildren().addAll(background, displayItems);
	
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
	protected static GameResumeHandler gameResumeHandler;
	private static int curGameScore;
	private static Text scoreOnGame;
	private static HBox scoreOnGameBox;
	private static pauseScreen pausedMenu;
	private static boolean openedPauseMenu = false;
	protected static Stage primaryStage;
	protected static Scene mainMenuScene;
	
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
	
	public GameScene(Stage stage, Scene mainScreen) {
		
		super(new Pane(createContent()), Main.getScenewidth(), Main.getSceneheight());	
		
		this.setOnKeyPressed(e -> {	

			if(e.getCode() == KeyCode.A) {
				userSnake.moveLeft();
			}
			else if(e.getCode() == KeyCode.D) {
				userSnake.moveRight();
			}
			else if(e.getCode() == KeyCode.P) {
				if(!openedPauseMenu)
					gamePauseHandler.handle(e);
				else
					gameResumeHandler.handle(e);
			}
		});
		
		this.setOnKeyReleased(e -> {	
			if(e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
				userSnake.stopSnake();
			}
		});
		
		primaryStage = stage;
		mainMenuScene = mainScreen;
		
	}
	
	protected static void pauseGame() {
		
		openedPauseMenu = true;
		stopFallAnimation();
		populationTimer.stop();
		userSnake.setSpeed(0);
		pausedMenu = new pauseScreen(curGameScore);
		root.getChildren().add(pausedMenu);
	
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
		
		openedPauseMenu = false;
		resumeFallAnimation();
		populationTimer.start();
		userSnake.setSpeed(400);
		root.getChildren().remove(pausedMenu);
	
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
		
		resetGame();
		root = new Pane();
		root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		root.getStyleClass().add("rootBg");
		
		scoreOnGameBox = new HBox(7);
		scoreOnGame = new Text(curGameScore + "");
		
		scoreOnGame.getStyleClass().add("scoreGameText");
		scoreOnGame.setFill(Color.WHITE);
		
		Image corwnImage = null;
		
		try {
			String pathToImage = "./img/scoreCrown.png";
			corwnImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		ImageView crownImageView = null;
		if(corwnImage != null) {
			crownImageView = new ImageView(corwnImage);
			crownImageView.setX(0);
			crownImageView.setY(0);
			crownImageView.setFitWidth(20);
			crownImageView.setPreserveRatio(true);  
		}
		
		scoreOnGameBox.setAlignment(Pos.CENTER);
		scoreOnGameBox.getChildren().addAll(crownImageView, scoreOnGame);
		
		scoreOnGameBox.setTranslateX(10);
		scoreOnGameBox.setTranslateY(10);
		
		pauseButton = new Button();
		pauseButton.getStyleClass().add("pauseBtn");
		pauseButton.setTranslateX(Main.getScenewidth() - 40);
		pauseButton.setTranslateY(Main.getSceneheight() - 40);
		
		gamePauseHandler = new GamePauseHandler();
		pauseButton.setOnAction(gamePauseHandler);
		
		gameResumeHandler = new GameResumeHandler();
		
		root.getChildren().addAll(scoreOnGameBox, pauseButton);

		userSnake =  new Snake(10);
		addSnake(userSnake);
		
		mainFrameTimer.start();
		populationTimer.start();
		
		return root;
		
	}
	
	private static void resetGame() {
		userSnake = null;
		tokens = new ArrayList<>();
		blocks = new ArrayList<>();
		walls = new ArrayList<>();
		gameSpeed = 4.5;	
		curGameScore = 0;
		openedPauseMenu = false;
	}

	private static void addSnake(Snake snake) {
		root.getChildren().add(snake);
		pauseButton.toFront();
		scoreOnGameBox.toFront();
	}
	
	private static void addGameObject(GameObject object) {
		root.getChildren().add(object.getView());
		userSnake.toFront();
		pauseButton.toFront();
		scoreOnGameBox.toFront();
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

	static GameObject collideWall(){
		for(GameObject wall : walls){
			if(wall.isColliding(userSnake.getSnakeHead())) {
				return wall;
			}
		}
		return null;
	}
	
	public static void populateNewItems() {
		
		Ball b = new Ball(Math.random()*(Main.getScenewidth()-40), -20, (int)(1 + Math.floor(Math.random()*10)), gameSpeed);
		DestroyBlocks db = new DestroyBlocks(2 + Math.random()*(Main.getScenewidth()-17), -20, gameSpeed);
		Magnet m = new Magnet(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);
		Shield s = new Shield(2 + Math.random()*(Main.getScenewidth() - 17), -20, gameSpeed);

		if(isSafe(b)){tokens.add(b); addGameObject(b); occur++;}
		if(isSafe(m) && occur%2 == 0){tokens.add(m); addGameObject(m); }
		if(isSafe(db) && occur%3 == 0){tokens.add(db); addGameObject(db); }
		if(isSafe(s) && occur% 2 == 0){tokens.add(s); addGameObject(s); }

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
	
}