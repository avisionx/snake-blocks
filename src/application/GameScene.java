package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class for handling pause event during gameplay using EventHandler
 */

class GamePauseHandler implements EventHandler<ActionEvent> {

	private boolean openedPauseMenu = false;
	
	@Override
	public void handle(ActionEvent e) {
		if(!openedPauseMenu) {
			GameScene.pauseGame();
			openedPauseMenu = true;
		}
		else {
			GameScene.resumeGame();
			openedPauseMenu = false;
		}
	}

	public void handle(KeyEvent e) {
		if(!openedPauseMenu) {
			GameScene.pauseGame();
			openedPauseMenu = true;
		}
		else {
			GameScene.resumeGame();
			openedPauseMenu = false;
		}
	}

}

/**
 * Class for handling resume game event using EventHandler
 */
class GameResumeHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent e) {
		GameScene.resumeGame();
	}

	public void handle(KeyEvent e) {
		GameScene.resumeGame();
	}

}

/**
 * pauseScreenButton creates a buttons for the pause screen, extends Button
 */
class pauseScreenButton extends Button {

	public pauseScreenButton(String name) {
		super(name);
		this.getStyleClass().add("pasueScreenBtn");
	}

}

/**
 * Class for showing the pause screen, extends StackPane
 */
class pauseScreen extends StackPane {

	/**
	 * Constructor for displaying the menu when the game is paused which includes Resume, New Game, Main Menu and Exit.
	 * Actions include saving game in case of exiting the game or system crash are also handled in the same.
	 * @param scoreToDisplay - Current score of the player
	 */
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
		if (corwnImage != null) {
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
			try {
				SaveManager.save(null, "./data/saveData.txt");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			GameScene gameScene = new GameScene(GameScene.primaryStage, GameScene.mainMenuScene);
			GameScene.primaryStage.setScene(gameScene);
			gameScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		});

		goToMenuBtn.setOnAction(e -> {
			GameScene.saveGame();
			GameScene.primaryStage.setScene(GameScene.mainMenuScene);
		});

		exitBtn.setOnAction(e -> {
			GameScene.exit();
		});

		displayItems.getChildren().addAll(scoreWithCrown, resumeBtn, newGameBtn, goToMenuBtn, exitBtn);
		displayItems.setAlignment(Pos.CENTER);
		this.getChildren().addAll(background, displayItems);

		GameScene.primaryStage.setOnCloseRequest(event -> {
			GameScene.saveGame();
		});
		
	}

}

/**
 * Class for the screen displayed after the game ends, extends StackPane
 */
class endScreen extends StackPane {

	/**
	 * Constructor for displaying the menu when the game gets over which includes New Game, Main Menu and Exit.
	 * Player's score is added to the leaderboard
	 * @param scoreToDisplay - Current score of the player
	 */
	public endScreen(int scoreToDisplay) {

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
		if (corwnImage != null) {
			crownImageView = new ImageView(corwnImage);
			crownImageView.setX(0);
			crownImageView.setY(0);
			crownImageView.setFitWidth(45);
			crownImageView.setPreserveRatio(true);
		}
		scoreWithCrown.setAlignment(Pos.CENTER);
		scoreWithCrown.getChildren().addAll(crownImageView, scoreText);

		Button newGameBtn = new pauseScreenButton("New Game");
		Button goToMenuBtn = new pauseScreenButton("Main Menu");
		Button exitBtn = new pauseScreenButton("Exit");

		newGameBtn.setOnAction(e -> {
			GameScene gameScene = new GameScene(GameScene.primaryStage, GameScene.mainMenuScene);
			GameScene.primaryStage.setScene(gameScene);
			gameScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		});

		goToMenuBtn.setOnAction(e -> {
			GameScene.primaryStage.setScene(GameScene.mainMenuScene);
		});

		exitBtn.setOnAction(e -> {
			System.exit(0);
		});

		displayItems.getChildren().addAll(scoreWithCrown, newGameBtn, goToMenuBtn, exitBtn);
		displayItems.setAlignment(Pos.CENTER);
		this.getChildren().addAll(background, displayItems);

	}

}

/**
 * GameScene is the scene during the gameplay, extends Scene
 * Fields -
 * root - root Pane
 * userSnake - snake of the current user
 * tokens - list of GameoObject in the game
 * blocks - List of Blocks in the game
 * walls - list of walls in the game
 * gameSpeed - speed of the game
 * interableCount - integer count for the interactables
 * Button pauseButton
 * curGameScore - Integer current score
 */
public class GameScene extends Scene {

	protected static Pane root;
	
	private static Snake userSnake;
	private static List<GameObject> tokens;
	private static List<Block> blocks;
	private static List<Wall> walls;
	private static double gameSpeed;
	private static int interactablesCount;

	private static Button pauseButton;
	
	private static GamePauseHandler gamePauseHandler;
	protected static GameResumeHandler gameResumeHandler;
	
	private static int curGameScore;
	private static Text scoreOnGame;
	private static HBox scoreOnGameBox;
	
	private static pauseScreen pausedMenu;
	private static endScreen endMenu;
	
	protected static Stage primaryStage;
	protected static Scene mainMenuScene;
	
	private static long pauseTime;
	
	protected static Block collidingWithBlock;
	private static Text shieldText;
	private static Text magnetText;

	/**
	 * collidingBlockTimer - timer for the each block collision.
	 */
	protected static AnimationTimer collidingBlockTimer = new AnimationTimer() {
		
		double lastUpdateTime = System.currentTimeMillis();

		@Override
		public void handle(long now) {
			double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;
			if (elapsedTime > 0.005) {
				if (collidingWithBlock != null) {
					
					if (collidingWithBlock.getValue() <= 0) {
						
						collidingWithBlock.setAlive(false);
						collidingWithBlock = null;
						this.stop();
						
					} 
					else if (userSnake.getSnakeLength() <= 0) {
						this.stop();
					} 
					else {
						collidingWithBlock.collide(userSnake);
					}
					
					lastUpdateTime = now;
				
				}
			}
		}
		
	};

	/**
	 * mainFrameTimer - timer for the game's frame
	 */
	private static AnimationTimer mainFrameTimer = new AnimationTimer() {

		@Override
		public void handle(long now) {
			GameScene.updateEachFrame();
		}

	};

	/**
	 * checks for collisions and update each frame accordingly
	 */
	private static void updateEachFrame() {

		for (GameObject token : GameScene.tokens) {
			
			if (token.isColliding(GameScene.userSnake.getSnakeHead())) {
				
				token.setAlive(false);
				((Interactable) token).collide(GameScene.userSnake);
				GameScene.root.getChildren().remove(token.getView());
			
			}
			if (token.isDead()) {
				
				GameScene.root.getChildren().remove(token.getView());
			
			}
		}

		for (Block block : GameScene.blocks) {
			
			if (block.isColliding(GameScene.userSnake.getSnakeHead())) {
				
				Point2D rectPos = ((rectangleWithText) block.getView()).getRectCenter();
				Point2D snakePos = userSnake.getSnakeHeadPosPoint2D();
				Point2D trianglePoint = new Point2D(rectPos.getX(), snakePos.getY());
				
				if (Math.atan(snakePos.distance(trianglePoint) / trianglePoint.distance(rectPos)) > 1 || snakePos.getY() <= rectPos.getY()) {	
					GameScene.userSnake.setSnakeCollideBlock(true);
				} 
				else {
					if (GameScene.collidingWithBlock != null && GameScene.collidingWithBlock != block) {
						
						GameScene.collidingBlockTimer.stop();
						GameScene.collidingWithBlock = block;
						GameScene.collidingBlockTimer.start();
						
						if (GameScene.collidingWithBlock.isDead()) {
							GameScene.root.getChildren().remove(GameScene.collidingWithBlock.getView());
							GameScene.resumeFallAnimation();
							GameScene.populationTimer.start();
						}
						
					}
					if (GameScene.collidingWithBlock == null) {
						
						GameScene.collidingWithBlock = block;
						GameScene.stopFallAnimation();
						GameScene.populationTimer.stop();
						GameScene.collidingBlockTimer.start();
						
						if (GameScene.collidingWithBlock.isDead()) {
							GameScene.root.getChildren().remove(GameScene.collidingWithBlock.getView());
							GameScene.resumeFallAnimation();
							GameScene.populationTimer.start();
						}
					
					}
				}
			}
			if (block.isDead()) {
				GameScene.root.getChildren().remove(block.getView());
			}
		}

		if (GameScene.collidingWithBlock != null) {
			
			boolean notColliding = true;
			for (Block block : GameScene.blocks) {
				if (GameScene.userSnake.getSnakeHead().isColliding(block) && notColliding) {
					notColliding = false;
				}
			}

			if (notColliding) {
				GameScene.resumeFallAnimation();
				GameScene.populationTimer.start();
				GameScene.collidingBlockTimer.stop();
				GameScene.collidingWithBlock = null;
			}
		}

		for (Wall wall : GameScene.walls) {
			if (wall.isDead()) {
				GameScene.root.getChildren().remove(wall.getView());
			}
		}

		GameScene.tokens.removeIf(GameObject::isDead);
		GameScene.blocks.removeIf(GameObject::isDead);
		GameScene.walls.removeIf(GameObject::isDead);

	}

	/**
	 * saves and exit game
	 */
	protected static void exit() {
		GameScene.saveGame();
		System.exit(0);
	}

	/**
	 *  method for saving game
	 */
	protected static void saveGame() {
		
		int snakeLength = GameScene.userSnake.getSnakeLength();
	    double snakeX = GameScene.userSnake.getSnakeHeadPosPoint2D().getX();
	    double snakeY = GameScene.userSnake.getSnakeHeadPosPoint2D().getY();
	    double snakeSpeed = GameScene.userSnake.getSnakeSpeed();
	    boolean snakeMagnet = GameScene.userSnake.hasMagnet;
	    double magnetDuration = snakeMagnet ? GameScene.userSnake.curMagnet.getDuration() : 0;
	    boolean snakeShield = GameScene.userSnake.hasShield;
	    double shieldDuration = snakeShield ? GameScene.userSnake.curShield.getDuration() : 0;
	    
	    int gameScore = GameScene.curGameScore;
	    double saveGameSpeed = GameScene.gameSpeed;
	    int saveInteractablesCount = GameScene.interactablesCount;
	    
	    ArrayList<Double> positionBlockX = new ArrayList<>();
	    ArrayList<Double> positionBlockY = new ArrayList<>();
	    ArrayList<Integer> blockValue = new ArrayList<>();

	    for(Block block : GameScene.blocks){
			positionBlockX.add(block.getView().getTranslateX());
			positionBlockY.add(block.getView().getTranslateY());
			blockValue.add(block.getValue());
		}
	    
	    ArrayList<Double> positionWallX = new ArrayList<>();
	    ArrayList<Double> positionWallY = new ArrayList<>();
	    ArrayList<Double> wallLength = new ArrayList<>();

	    for(Wall wall : GameScene.walls){
	    	positionWallX.add(wall.getView().getTranslateX());
	    	positionWallY.add(wall.getView().getTranslateY());
	    	wallLength.add(wall.getLength());
		}
	    
	    ArrayList<Double> positionBallX = new ArrayList<>();
	    ArrayList<Double> positionBallY = new ArrayList<>();
	    ArrayList<Integer> ballValue = new ArrayList<>();

	    for(Ball ball : GameScene.getBallList()){
	    	positionBallX.add(ball.getView().getTranslateX());
	    	positionBallY.add(ball.getView().getTranslateY());
	    	ballValue.add(ball.getValue());
		}
	    
	    ArrayList<Double> positionPowerX = new ArrayList<>();
	    ArrayList<Double> positionPowerY = new ArrayList<>();
	    ArrayList<String> powerUpType = new ArrayList<>();

	    for(GameObject token : GameScene.getTokenExceptBallList()){
	    	positionPowerX.add(token.getView().getTranslateX());
	    	positionPowerY.add(token.getView().getTranslateY());
	    	powerUpType.add(token.getClass().toString());
		}
		
	    ContentSaver saveData = new ContentSaver(
	    		snakeLength, snakeX, snakeY, snakeSpeed, 
	    		snakeMagnet, magnetDuration, snakeShield, 
	    		shieldDuration, gameScore, saveGameSpeed, 
	    		saveInteractablesCount, positionBlockX, 
	    		positionBlockY, blockValue, positionWallX, 
	    		positionWallY, wallLength, positionBallX, 
	    		positionBallY, ballValue, positionPowerX,
	    		positionPowerY, powerUpType
	    	);

		try{
			SaveManager.save(saveData, "./data/saveData.txt");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		Button resumeBtn = (Button) ((VBox)GameScene.mainMenuScene.getRoot()).getChildren().get(1);
		resumeBtn.setVisible(true);
		Main.savedData = saveData;
		Main.oldGamePresent = true;
		
		return;
	}

	/**
	 * Timer for populating the game with tokens etc.
	 */
	private static AnimationTimer populationTimer = new AnimationTimer() {
		double lastPopulateTime = System.currentTimeMillis();
		boolean checker = false;

		@Override
		public void handle(long now) {
			double elapsedTimeInSec = (now - lastPopulateTime) / 1_000_000_000.0;
			if (elapsedTimeInSec >= 1.5 && checker) {
				populateNewItems();
				lastPopulateTime = now;
				checker = false;
			} else if (elapsedTimeInSec >= 1.5 && !checker) {
				spawnBlockLayer();
				lastPopulateTime = now;
				checker = true;
			}
		}
	};

	/**
	 * getter for list of blocks
	 * @return List of Block object
	 */
	public static List<Block> getBlockList() {
		return GameScene.blocks;
	}

	/**
	 * getter for the list of balls
	 * @return List of Ball object
	 */
	public static List<Ball> getBallList() {
		List<Ball> ballList = new ArrayList<>();
		for (GameObject object : GameScene.tokens) {
			
			if (object.getClass() == Ball.class) {
				ballList.add((Ball) object);
			}
			
		}	
		return ballList;
	}

	/**
	 * getter for token list which excluding any ball
	 * @return List of GameObject
	 */
	public static List<GameObject> getTokenExceptBallList() {
		List<GameObject> tokenList = new ArrayList<>();
		for (GameObject object : GameScene.tokens) {
			
			if (object.getClass() != Ball.class) {
				if(object.getClass() == Shield.class) {
					tokenList.add((Shield)object);
				}
				else if(object.getClass() == DestroyBlocks.class) {
					tokenList.add((DestroyBlocks)object);
				}
				else if(object.getClass() == Magnet.class) {
					tokenList.add((Magnet)object);
				}
			}
			
		}	
		return tokenList;
	}

	/**
	 * sets the Magnet on
	 */
	public static void setMagnetOn() {
		GameScene.magnetText.setText("Magnet: On");
		GameScene.magnetText.setFill(Color.RED);
	}

	/**
	 * sets the magnet off
	 */
	public static void setMagnetOff() {
		GameScene.magnetText.setText("Magnet: Off");
		GameScene.magnetText.setFill(Color.WHITE);
	}

	/**
	 * sets the shield on
	 */
	public static void setShieldOn() {
		GameScene.shieldText.setText("Shield: On");
		GameScene.shieldText.setFill(Color.GREENYELLOW);
	}

	/**
	 * sets the shield off
	 */
	public static void setShieldOff() {
		GameScene.shieldText.setText("Shield: Off");
		GameScene.shieldText.setFill(Color.WHITE);
	}

	/**
	 * setter for GameScore
	 * @param newScore - score to be updated to
	 */
	public static void setGameScore(int newScore) {
		GameScene.curGameScore = newScore;
		GameScene.scoreOnGame.setText(newScore + "");
	}

	/**
	 * getter for GameScore
	 * @return integer game score
	 */
	public static int getGameScore() {
		return GameScene.curGameScore;
	}

	/**
	 * adds snake to the GameScene
	 * @param snake - Snake Object to be added
	 */
	private static void addSnake(Snake snake) {
		
		GameScene.root.getChildren().add(snake);
		GameScene.pauseButton.toFront();
		GameScene.scoreOnGameBox.toFront();
	
	}

	/**
	 * adds GameObject to the GameScene
	 * @param object GameObject to be added
	 */
	private static void addGameObject(GameObject object) {
		
		GameScene.root.getChildren().add(object.getView());
		GameScene.userSnake.toFront();
		GameScene.pauseButton.toFront();
		GameScene.scoreOnGameBox.toFront();
	
	}

	/**
	 * Constructor for GameScene
	 * @param stage - Stage for the Application
	 * @param mainScreen -  mainScreen Scene
	 */
	public GameScene(Stage stage, Scene mainScreen) {

		super(new Pane(createContent()), Main.getScenewidth(), Main.getSceneheight());

		this.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.A) {
				userSnake.moveLeft();
			} else if (e.getCode() == KeyCode.D) {
				userSnake.moveRight();
			}
			
		});

		this.setOnKeyReleased(e -> {
			
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
				userSnake.stopSnake();
			}
			
		});

		GameScene.primaryStage = stage;
		GameScene.mainMenuScene = mainScreen;

		GameScene.primaryStage.setOnCloseRequest(event -> {
			GameScene.saveGame();
		});
		
	}

	/**
	 * Constructor for the GameScene
	 * @param stage - Stage for the Application
	 * @param mainScreen -  mainScreen Scene
	 * @param savedData - ContentSaver Object, for resuming to any previous game
	 */
	public GameScene(Stage stage, Scene mainScreen, ContentSaver savedData) {
		super(new Pane(createPopulatedContent(savedData)), Main.getScenewidth(), Main.getSceneheight());

		this.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.A) {
				userSnake.moveLeft();
			} else if (e.getCode() == KeyCode.D) {
				userSnake.moveRight();
			}
			
		});

		this.setOnKeyReleased(e -> {
			
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
				userSnake.stopSnake();
			}
			
		});

		GameScene.primaryStage = stage;
		GameScene.mainMenuScene = mainScreen;
		
		GameScene.primaryStage.setOnCloseRequest(event -> {
			GameScene.saveGame();
		});
		
	}

	/**
	 *  createPopulatedContent - method for creating populated content in case for resuming game
	 * @param savedData - ContentSaver Object i.e. data for resuming back to game
	 * @return Parent object i.e. root of the GameScene
	 */
	private static Parent createPopulatedContent(ContentSaver savedData) {
		
		GameScene.resetGame();

		GameScene.root = new Pane();
		
		GameScene.root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		GameScene.root.getStyleClass().add("rootBg");
		
		GameScene.curGameScore = savedData.getGameScore();
		GameScene.scoreOnGameBox = new HBox(7);
		GameScene.scoreOnGame = new Text(GameScene.curGameScore + "");
		GameScene.scoreOnGame.getStyleClass().add("scoreGameText");
		GameScene.scoreOnGame.setFill(Color.WHITE);

		Image corwnImage = null;

		try {
			String pathToImage = "./img/scoreCrown.png";
			corwnImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		ImageView crownImageView = null;
		
		if (corwnImage != null) {
			crownImageView = new ImageView(corwnImage);
			crownImageView.setX(0);
			crownImageView.setY(0);
			crownImageView.setFitWidth(20);
			crownImageView.setPreserveRatio(true);
		}

		GameScene.scoreOnGameBox.setAlignment(Pos.CENTER);
		GameScene.scoreOnGameBox.getChildren().addAll(crownImageView, GameScene.scoreOnGame);
		GameScene.scoreOnGameBox.setTranslateX(10);
		GameScene.scoreOnGameBox.setTranslateY(10);

		GameScene.pauseButton = new Button();
		GameScene.pauseButton.setDefaultButton(false);
		GameScene.pauseButton.getStyleClass().add("pauseBtn");
		GameScene.pauseButton.setTranslateX(Main.getScenewidth() - 40);
		GameScene.pauseButton.setTranslateY(Main.getSceneheight() - 40);
		GameScene.gamePauseHandler = new GamePauseHandler();
		GameScene.pauseButton.setOnAction(GameScene.gamePauseHandler);

		GameScene.gameResumeHandler = new GameResumeHandler();

		GameScene.root.getChildren().addAll(GameScene.scoreOnGameBox, GameScene.pauseButton);

		GameScene.magnetText = new Text("Magnet: Off");
		GameScene.magnetText.setFill(Color.WHITE);
		GameScene.magnetText.setFont(Font.font("Arial", 15));
		GameScene.magnetText.setX(310);
		GameScene.magnetText.setY(20);
		GameScene.root.getChildren().add(GameScene.magnetText);
		
		GameScene.shieldText = new Text("Shield: Off");
		GameScene.shieldText.setFill(Color.WHITE);
		GameScene.shieldText.setFont(Font.font("Arial", 15));
		GameScene.shieldText.setX(230);
		GameScene.shieldText.setY(20);
		GameScene.root.getChildren().add(GameScene.shieldText);

		GameScene.userSnake = new Snake(10, savedData.getSnakeX());
		GameScene.addSnake(GameScene.userSnake);
		GameScene.userSnake.setSnakeLength(savedData.getSnakelength());
		resumeSavedGame(savedData);
		
		GameScene.mainFrameTimer.start();
		GameScene.populationTimer.start();
		
		return GameScene.root;
	}

	/**
	 * resumeSavedGame method for resuming back to the saved game
	 * @param savedData - ContentSaver Object i.e. data for resuming game
	 */
	private static void resumeSavedGame(ContentSaver savedData) {
		
		GameScene.gameSpeed = savedData.getGameSpeed();
		GameScene.userSnake.setSpeed(400);
		if(savedData.getSnakeMagnet()) {
			userSnake.hasMagnet = false;
			Magnet m = new Magnet(savedData.getSnakeX(), savedData.getSnakeY(), gameSpeed);
			m.collide(userSnake);
			m.addDuration(-5+savedData.getMagnetDuration());
		}
		if(savedData.getSnakeShield()) {
			userSnake.hasShield = false;
			Shield s = new Shield(savedData.getSnakeX(), savedData.getSnakeY(), gameSpeed);
			s.collide(userSnake);
			s.addDuration(-5+savedData.getMagnetDuration());
		}
		
		GameScene.interactablesCount = savedData.getInteractablesCount();
		
		for(int i = 0; i < savedData.getPositionBlockX().size(); i++) {
			Block b = new Block(savedData.getPositionBlockX().get(i), savedData.getPositionBlockY().get(i), savedData.getBlockValue().get(i), gameSpeed);
			blocks.add(b);
			GameScene.addGameObject(b);
		}
		
		for(int i = 0; i < savedData.getPositionBallX().size(); i++) {
			Ball b = new Ball(savedData.getPositionBallX().get(i), savedData.getPositionBallY().get(i), savedData.getBallValue().get(i), gameSpeed);
			tokens.add(b);
			GameScene.addGameObject(b);
		}
		
		for(int i = 0; i < savedData.getPositionWallX().size(); i++) {
			Wall w = new Wall(savedData.getPositionWallX().get(i), savedData.getPositionWallY().get(i), savedData.getWallLength().get(i), gameSpeed, true);
			walls.add(w);
			GameScene.addGameObject(w);
		}
		
		for(int i = 0; i < savedData.getPowerUpType().size(); i++) {
			String className = savedData.getPowerUpType().get(i);
			GameObject o = null;
			if(className == Shield.class.toString()) {
				o = new Shield(savedData.getPositionPowerX().get(i), savedData.getPositionPowerY().get(i), gameSpeed);
			}
			else if(className == Magnet.class.toString()) {
				o = new Magnet(savedData.getPositionPowerX().get(i), savedData.getPositionPowerY().get(i), gameSpeed);
			}
			else if(className == DestroyBlocks.class.toString()) {
				o = new DestroyBlocks(savedData.getPositionPowerX().get(i), savedData.getPositionPowerY().get(i), gameSpeed);
			}
			if(o != null) {
				tokens.add(o);
				GameScene.addGameObject(o);
			}
		}
		
	}

	/**
	 * creates contents of the gameplay
	 * @return Parent object i.e. the root of the GameScene
	 */
	private static Parent createContent() {
		
		GameScene.resetGame();
		
		GameScene.root = new Pane();
		
		GameScene.root.setPrefSize(Main.getScenewidth(), Main.getSceneheight());
		GameScene.root.getStyleClass().add("rootBg");

		GameScene.scoreOnGameBox = new HBox(7);
		GameScene.scoreOnGame = new Text(GameScene.curGameScore + "");
		GameScene.scoreOnGame.getStyleClass().add("scoreGameText");
		GameScene.scoreOnGame.setFill(Color.WHITE);

		Image corwnImage = null;

		try {
			String pathToImage = "./img/scoreCrown.png";
			corwnImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		ImageView crownImageView = null;
		
		if (corwnImage != null) {
			crownImageView = new ImageView(corwnImage);
			crownImageView.setX(0);
			crownImageView.setY(0);
			crownImageView.setFitWidth(20);
			crownImageView.setPreserveRatio(true);
		}

		GameScene.scoreOnGameBox.setAlignment(Pos.CENTER);
		GameScene.scoreOnGameBox.getChildren().addAll(crownImageView, GameScene.scoreOnGame);
		GameScene.scoreOnGameBox.setTranslateX(10);
		GameScene.scoreOnGameBox.setTranslateY(10);

		GameScene.pauseButton = new Button();
		GameScene.pauseButton.setDefaultButton(false);
		GameScene.pauseButton.getStyleClass().add("pauseBtn");
		GameScene.pauseButton.setTranslateX(Main.getScenewidth() - 40);
		GameScene.pauseButton.setTranslateY(Main.getSceneheight() - 40);
		GameScene.gamePauseHandler = new GamePauseHandler();
		GameScene.pauseButton.setOnAction(GameScene.gamePauseHandler);

		GameScene.gameResumeHandler = new GameResumeHandler();

		GameScene.root.getChildren().addAll(GameScene.scoreOnGameBox, GameScene.pauseButton);

		GameScene.magnetText = new Text("Magnet: Off");
		GameScene.magnetText.setFill(Color.WHITE);
		GameScene.magnetText.setFont(Font.font("Arial", 15));
		GameScene.magnetText.setX(310);
		GameScene.magnetText.setY(20);
		GameScene.root.getChildren().add(GameScene.magnetText);
		
		GameScene.shieldText = new Text("Shield: Off");
		GameScene.shieldText.setFill(Color.WHITE);
		GameScene.shieldText.setFont(Font.font("Arial", 15));
		GameScene.shieldText.setX(230);
		GameScene.shieldText.setY(20);
		GameScene.root.getChildren().add(GameScene.shieldText);

		GameScene.userSnake = new Snake(10, Main.getScenewidth()/2);
		
		GameScene.addSnake(GameScene.userSnake);
		
		GameScene.mainFrameTimer.start();
		GameScene.populationTimer.start();

		return GameScene.root;

	}

	/**
	 * resetGame - method used to reset the game i.e. new game
	 */
	private static void resetGame() {
		GameScene.userSnake = null;
		GameScene.tokens = new ArrayList<>();
		GameScene.blocks = new ArrayList<>();
		GameScene.walls = new ArrayList<>();
		GameScene.gameSpeed = 4.5;
		GameScene.curGameScore = 0;
		GameScene.collidingWithBlock = null;
		GameScene.interactablesCount = 0;
	}

	/**
	 * stops the game i.e. falling down animation
	 */
	private static void stopFallAnimation() {
		for (GameObject token : GameScene.tokens) {
			token.getFallDownTimer().stop();
		}
		for (Wall wall : GameScene.walls) {
			wall.getFallDownTimer().stop();
		}
		for (Block block : GameScene.blocks) {
			block.getFallDownTimer().stop();
		}
	}

	/**
	 * stops the powerups downward motion while pausing the game
	 */
	private static void stopPowerUps() {
		if (GameScene.userSnake.hasMagnet) {
			GameScene.userSnake.curMagnet.stopMagnetTimer();
		}
		if (GameScene.userSnake.hasShield) {
			GameScene.userSnake.curShield.shieldTimer.stop();
		}
	}

	/**
	 * method for pausing game
	 */
	protected static void pauseGame() {
		
		GameScene.pauseTime = System.currentTimeMillis();
		GameScene.stopFallAnimation();
		GameScene.stopPowerUps();
		GameScene.populationTimer.stop();
		GameScene.userSnake.setSpeed(0);
		GameScene.pausedMenu = new pauseScreen(GameScene.curGameScore);
		GameScene.root.getChildren().add(GameScene.pausedMenu);
	
	}

	/**
	 * method to resume back the Fall Animation of tokens, walls and block i.e. restarting the game
	 */
	private static void resumeFallAnimation() {
		for (GameObject token : GameScene.tokens) {
			token.getFallDownTimer().start();
		}
		for (Wall wall : GameScene.walls) {
			wall.getFallDownTimer().start();
		}
		for (Block block : GameScene.blocks) {
			block.getFallDownTimer().start();
		}
	}

	/**
	 * method to resume the motion of powerUps
	 */
	private static void resumePowerUps() {
		if (GameScene.userSnake.hasMagnet) {
			double addDuration = ((long) System.currentTimeMillis() - GameScene.pauseTime) / 1000;
			GameScene.userSnake.curMagnet.addDuration(addDuration);
			GameScene.userSnake.curMagnet.startMagnetTimer();
		}
		if (GameScene.userSnake.hasShield) {
			double addDuration = ((long) System.currentTimeMillis() - GameScene.pauseTime) / 1000;
			GameScene.userSnake.curShield.addDuration(addDuration);
			GameScene.userSnake.curShield.shieldTimer.start();
		}
	}

	/**
	 * method to resume the game
	 */
	protected static void resumeGame() {
		
		GameScene.resumeFallAnimation();
		GameScene.resumePowerUps();
		GameScene.populationTimer.start();
		GameScene.userSnake.setSpeed(400);
		GameScene.root.getChildren().remove(GameScene.pausedMenu);
	
	}

	/**
	 * method called when the game gets over
	 */
	public static void gameOver() {
		
		Button resumeBtn = (Button) ((VBox)GameScene.mainMenuScene.getRoot()).getChildren().get(1);
		resumeBtn.setVisible(false);
		try {
			SaveManager.save(null, "./data/saveData.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameScene.gameSpeed = 0;
		GameScene.setGameSpeed(0);
		GameScene.pauseButton.setOnAction(null);
		GameScene.stopFallAnimation();
		GameScene.populationTimer.stop();
		GameScene.stopPowerUps();
		GameScene.userSnake.setSpeed(0);
		GameScene.endMenu = new endScreen(curGameScore);
		Score newScore = new Score(curGameScore, new Date());
		Leaderboard leaderboard = Main.getLeaderboard();
		leaderboard.addScore(newScore);
		ArrayList<Score> scoreList = leaderboard.getLeaderboard();
		
		ObjectOutputStream out = null;
		 
		try {
			out = new ObjectOutputStream(new FileOutputStream("./data/leaderboard.txt"));
			out.writeObject(scoreList);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GameScene.root.getChildren().add(GameScene.endMenu);
	
	}

	/**
	 * collideWall method returns the wall colliding else null
	 * @return the wall colliding the snake else null
	 */
	static GameObject collideWall() {
		for (GameObject wall : GameScene.walls) {
			
			if (wall.isColliding(GameScene.userSnake.getSnakeHead())) {
				return wall;
			}
			
		}
		return null;
	}

	/**
	 * method to populate the game with new items i.e. tokens, balls, wall, magnet etc
	 */
	public static void populateNewItems() {

		Random posR = new Random();
		
		for (int i = 0; i < 3; i++) {
			Ball newBall = new Ball(posR.nextInt(360) + 10, -100 + posR.nextFloat() * -1 * 300, 1 + posR.nextInt(20), GameScene.gameSpeed);
			if (isSafe(newBall)) {
				GameScene.tokens.add(newBall);
				GameScene.addGameObject(newBall);
				GameScene.interactablesCount++;
				interactablesCount %= 1000;
			}
		}

		DestroyBlocks newDestroyBlock = new DestroyBlocks(posR.nextInt(360) + 20, -70 + posR.nextFloat() * -1 * 300, GameScene.gameSpeed);
		if (isSafe(newDestroyBlock) && GameScene.interactablesCount % 5 == 0) {
			tokens.add(newDestroyBlock);
			addGameObject(newDestroyBlock);
		}

		Magnet newMagnet = new Magnet(posR.nextInt(360) + 20, -50 + posR.nextFloat() * -1 * 300, GameScene.gameSpeed);
		if (isSafe(newMagnet) && GameScene.interactablesCount % 4 == 0) {
			GameScene.tokens.add(newMagnet);
			GameScene.addGameObject(newMagnet);
		}

		Shield newShield = new Shield(posR.nextInt(360) + 20, -50 + posR.nextFloat() * -1 * 300, GameScene.gameSpeed);
		if (isSafe(newShield) && GameScene.interactablesCount % 4 == 0) {
			GameScene.tokens.add(newShield);
			GameScene.addGameObject(newShield);
		}

	}

	/**
	 * generates a new block layer with at least one block with value less than snake's length
	 */
	private static void spawnBlockLayer() {
		
		Random r1 = new Random();
		int howManyWallsFront = r1.nextInt(6);
		ArrayList<Integer> usedPoolFront = new ArrayList<>();
		for (int i = 0; i < howManyWallsFront; i++) {
			int nextLocation = r1.nextInt(5) + 1;
			while (usedPoolFront.contains(nextLocation)) {
				nextLocation = r1.nextInt(5) + 1;
			}
			Wall newWall = new Wall(1 + nextLocation * 65, -202, 100 + r1.nextInt(100), GameScene.gameSpeed);
			GameScene.walls.add(newWall);
			GameScene.addGameObject(newWall);
		}

		Random r2 = new Random();
		int whichOneLess = (int) Math.floor(r2.nextInt(6));
		int lessRange = (int) Math.floor(r2.nextInt(GameScene.userSnake.getSnakeLength() <= 0 ? 1 : GameScene.userSnake.getSnakeLength()));
		lessRange = lessRange > 60 ? 60 : lessRange;
		if (lessRange >= 1) {
			Block newBlock = new Block(6 + whichOneLess * 65, -262, lessRange, GameScene.gameSpeed);
			GameScene.blocks.add(newBlock);
			GameScene.addGameObject(newBlock);
		}

		for (int i = 0; i < 6; i++) {
			if (i != whichOneLess) {
				int randomValue = (int) Math.floor(Math.random() * 56);
				if (randomValue != 0) {
					Block newBlock = new Block(6 + i * 65, -262, randomValue, GameScene.gameSpeed);
					GameScene.blocks.add(newBlock);
					GameScene.addGameObject(newBlock);
				}
			}
		}
		
		Random r3 = new Random();
		int howManyWallsBack = r3.nextInt(6);
		ArrayList<Integer> usedPoolBack = new ArrayList<>();
		for (int i = 0; i < howManyWallsBack; i++) {
			int nextLocation = r3.nextInt(5) + 1;
			while (usedPoolBack.contains(nextLocation)) {
				nextLocation = r3.nextInt(5) + 1;
			}
			Wall newWall = new Wall(1 + nextLocation * 65, -262, 100 + r1.nextInt(200), GameScene.gameSpeed);
			GameScene.walls.add(newWall);
			GameScene.addGameObject(newWall);
		}
	}

	/**
	 * isSafe returns true if no GameObject collides with the one in parameter
	 * @param object - GameObject with which the collision is to be checkde
	 * @return boolean value, true if any object collides
	 */
	static boolean isSafe(GameObject object) {

		for (GameObject token : GameScene.tokens) {
			if (token.isColliding(object)) {
				return false;
			}
		}
		for (GameObject wall : GameScene.walls) {
			if (wall.isColliding(object)) {
				return false;
			}
		}
		for (GameObject block : GameScene.blocks) {
			if (block.isColliding(object)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * changes GameSpeed for every token and object according to its type
	 */
	public static void setGameSpeed() {
		if (GameScene.userSnake.getSnakeLength() <= 10) {
			GameScene.gameSpeed = 4.5;
		} else {
			if (GameScene.userSnake.getSnakeLength() >= 100) {
				GameScene.gameSpeed = 6;
			} else {
				GameScene.gameSpeed = 4.5 + 1.5 / 90 * (GameScene.userSnake.getSnakeLength() - 10);
			}
		}

		for (GameObject token : GameScene.tokens) {
			token.setSpeed(GameScene.gameSpeed);
		}
		for (Wall wall : GameScene.walls) {
			wall.setSpeed(GameScene.gameSpeed);
		}
		for (Block block : GameScene.blocks) {
			block.setSpeed(GameScene.gameSpeed);
		}

	}

	/**
	 * sets the game speed to value
	 * @param value - double value to which game speed is to be set on.
	 */
	public static void setGameSpeed(double value) {
		for (GameObject token : GameScene.tokens) {
			token.setSpeed(value);
		}
		for (Wall wall : GameScene.walls) {
			wall.setSpeed(value);
		}
		for (Block block : GameScene.blocks) {
			block.setSpeed(value);
		}

	}

}