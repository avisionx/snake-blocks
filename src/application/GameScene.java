package application;

import java.io.FileInputStream;
import java.util.ArrayList;
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

class GamePauseHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent e) {
		GameScene.pauseGame();
	}

	public void handle(KeyEvent e) {
		GameScene.pauseGame();
	}

}

class GameResumeHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle(ActionEvent e) {
		GameScene.resumeGame();
	}

	public void handle(KeyEvent e) {
		GameScene.resumeGame();
	}

}

class pauseScreenButton extends Button {

	public pauseScreenButton(String name) {
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

		displayItems.getChildren().addAll(scoreWithCrown, resumeBtn, newGameBtn, goToMenuBtn, exitBtn);
		displayItems.setAlignment(Pos.CENTER);
		this.getChildren().addAll(background, displayItems);

	}

}

class endScreen extends StackPane {

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

public class GameScene extends Scene {

	protected static Pane root;
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
	private static endScreen endMenu;
	private static boolean openedPauseMenu = false;
	protected static Stage primaryStage;
	protected static Scene mainMenuScene;
	private static long pauseTime;
	protected static Block collidingWithBlock;
	private static Text shieldText;
	private static Text magnetText;
	
	protected static AnimationTimer collidingBlockTimer = new AnimationTimer() {
		double lastUpdateTime = System.currentTimeMillis();
		@Override
		public void handle(long now) {
			double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;
			if(elapsedTime > 0.05) {
				if(collidingWithBlock != null) {
					if(collidingWithBlock.getValue() <= 0) {
						collidingWithBlock.setAlive(false);
						collidingWithBlock = null;
						this.stop();
					}
					else if(userSnake.getSnakeLength() <= 0) {
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
	private static int interactablesCount = 0;

	private static AnimationTimer mainFrameTimer = new AnimationTimer() {

		@Override
		public void handle(long now) {
			updateEachFrame();
		}

	};
	
	public static List<Block> getBlockList() {
		return blocks;
	}
	
	public static List<Ball> getBallList() {
		List<Ball> ballList = new ArrayList<>();
		for(GameObject object : tokens) {
			if(object.getClass() == Ball.class) {
				ballList.add((Ball)object);
			}
		}
		return ballList;
	}
	
	public static void setMagnetOn() {
		magnetText.setText("Magnet: On");
		magnetText.setFill(Color.RED);
	}
	
	public static void setMagnetOff() {
		magnetText.setText("Magnet: Off");
		magnetText.setFill(Color.WHITE);
	}
	
	public static void setShieldOn() {
		shieldText.setText("Shield: On");
		shieldText.setFill(Color.GREENYELLOW);
	}
	
	public static void setShieldOff() {
		shieldText.setText("Shield: Off");
		shieldText.setFill(Color.WHITE);
	}

	private static AnimationTimer populationTimer = new AnimationTimer() {

		double lastPopulateTime = System.currentTimeMillis();
		boolean checker = false;
		
		@Override
		public void handle(long now) {
			double elapsedTimeInSec = (now - lastPopulateTime) / 1_000_000_000.0;
			if(elapsedTimeInSec >= 1.5 && checker) {
				populateNewItems();
				lastPopulateTime = now;
				checker = false;
			}
			else if (elapsedTimeInSec >= 1.5 && !checker) {
				spawnBlockLayer();
				lastPopulateTime = now;
				checker = true;
			}
		}

	};

	public static void setGameScore(int newScore) {
		curGameScore = newScore;
		scoreOnGame.setText(newScore + "");
	}

	public static int getGameScore() {
		return curGameScore;
	}

	public GameScene(Stage stage, Scene mainScreen) {

		super(new Pane(createContent()), Main.getScenewidth(), Main.getSceneheight());

		this.setOnKeyPressed(e -> {

			if (e.getCode() == KeyCode.A) {
				userSnake.moveLeft();
			} else if (e.getCode() == KeyCode.D) {
				userSnake.moveRight();
			} else if (e.getCode() == KeyCode.P) {
				if (!openedPauseMenu)
					gamePauseHandler.handle(e);
				else
					gameResumeHandler.handle(e);
			}
		});

		this.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.D) {
				userSnake.stopSnake();
			}
		});

		primaryStage = stage;
		mainMenuScene = mainScreen;

	}

	protected static void pauseGame() {
		pauseTime = System.currentTimeMillis();
		openedPauseMenu = true;
		stopFallAnimation();
		stopPowerUps();
		populationTimer.stop();
		userSnake.setSpeed(0);
		pausedMenu = new pauseScreen(curGameScore);
		root.getChildren().add(pausedMenu);
	}

	private static void stopPowerUps() {
		if(userSnake.hasMagnet) {
			userSnake.curMagnet.magnetTimer.stop();
		}
		if(userSnake.hasShield) {
			userSnake.curShield.shieldTimer.stop();
		}
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
		resumePowerUps();
		root.getChildren().remove(pausedMenu);
	}

	private static void resumePowerUps() {
		if(userSnake.hasMagnet) {
			userSnake.curMagnet.addDuration(((long)System.currentTimeMillis() - pauseTime)/1000);
			userSnake.curMagnet.magnetTimer.start();
		}
		if(userSnake.hasShield) {
			userSnake.curShield.addDuration(((long)System.currentTimeMillis() - pauseTime)/1000);
			userSnake.curShield.shieldTimer.start();
		}
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
		if (corwnImage != null) {
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

		magnetText = new Text("Magnet: Off");
		magnetText.setFill(Color.WHITE);
		magnetText.setFont(Font.font("Arial", 15));
		magnetText.setX(310);
		magnetText.setY(20);
		root.getChildren().add(magnetText);
		shieldText = new Text("Shield: Off");
		shieldText.setFill(Color.WHITE);
		shieldText.setFont(Font.font("Arial", 15));
		shieldText.setX(230);
		shieldText.setY(20);
		root.getChildren().add(shieldText);
		
		userSnake = new Snake(10);
		addSnake(userSnake);
		mainFrameTimer.start();
		populationTimer.start();
		
		return root;

	}

	public static void gameOver() {
		System.out.println("GAME OVER");
//		TODO
		endGame();
	}

	private static void endGame() {
		stopFallAnimation();
		populationTimer.stop();
		stopPowerUps();
		userSnake.setSpeed(0);
		endMenu = new endScreen(curGameScore);
		root.getChildren().add(endMenu);
	}

	private static void resetGame() {
		userSnake = null;
		tokens = new ArrayList<>();
		blocks = new ArrayList<>();
		walls = new ArrayList<>();
		gameSpeed = 4.5;
		curGameScore = 0;
		collidingWithBlock = null;
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

		for (GameObject token : tokens) {

			if (token.isColliding(userSnake.getSnakeHead())) {
				token.setAlive(false);
				((Interactable) token).collide(userSnake);
				root.getChildren().remove(token.getView());
			}
			if (token.isDead()) {
				root.getChildren().remove(token.getView());
			}
		}

		for (Block block : blocks) {
			if (block.isColliding(userSnake.getSnakeHead())) {
				Point2D rectPos = ((rectangleWithText) block.getView()).getRectCenter();
				Point2D snakePos = userSnake.getSnakeHeadPosPoint2D();
				Point2D trianglePoint = new Point2D(rectPos.getX(), snakePos.getY());
				if (Math.atan(snakePos.distance(trianglePoint) / trianglePoint.distance(rectPos)) > 1
						|| snakePos.getY() <= rectPos.getY()) {
					userSnake.setSnakeCollideBlock(true);
				} else {
					if(collidingWithBlock != null && collidingWithBlock != block) {
						collidingBlockTimer.stop();
						collidingWithBlock = block;
						collidingBlockTimer.start();
						if(collidingWithBlock.isDead()) {
							root.getChildren().remove(collidingWithBlock.getView());
							resumeFallAnimation();
							populationTimer.start();
						}
					}
					if(collidingWithBlock == null) {
						collidingWithBlock = block;
						stopFallAnimation();
						populationTimer.stop();
						collidingBlockTimer.start();
						if(collidingWithBlock.isDead()) {
							root.getChildren().remove(collidingWithBlock.getView());
							resumeFallAnimation();
							populationTimer.start();
						}
					}
				}
			}
			if (block.isDead()) {
				root.getChildren().remove(block.getView());
			}
		}
		
		if(collidingWithBlock != null){
			boolean notColliding = true;
			for (Block block : blocks) {
				if(userSnake.getSnakeHead().isColliding(block) && notColliding) {
					notColliding = false;
				}
			}
			
			if(notColliding) {
				resumeFallAnimation();
				populationTimer.start();
				collidingBlockTimer.stop();
				collidingWithBlock = null;
			}
		}

		for (Wall wall : walls) {
			if (wall.isDead()) {
				root.getChildren().remove(wall.getView());
			}
		}

		tokens.removeIf(GameObject::isDead);
		blocks.removeIf(GameObject::isDead);
		walls.removeIf(GameObject::isDead);

	}

	static GameObject collideWall() {
		for (GameObject wall : walls) {
			if (wall.isColliding(userSnake.getSnakeHead())) {
				return wall;
			}
		}
		return null;
	}

	public static void populateNewItems() {

		Random posR = new Random();
		for(int i = 0; i < 8; i++) {
			Ball newBall = new Ball(posR.nextInt(360) + 20, -100 + posR.nextFloat()*-1*300, 1 + posR.nextInt(20), gameSpeed);
			if (isSafe(newBall)) {
				tokens.add(newBall);
				addGameObject(newBall);
				interactablesCount++;
			}
		}
		
		DestroyBlocks newDestroyBlock = new DestroyBlocks(posR.nextInt(360) + 20, -50 + posR.nextFloat()*-1*300, gameSpeed);
		if (isSafe(newDestroyBlock) && interactablesCount % 5 == 0) {
			tokens.add(newDestroyBlock);
			addGameObject(newDestroyBlock);
		}
		
		Magnet newMagnet = new Magnet(posR.nextInt(360) + 20, -50 + posR.nextFloat()*-1*300, gameSpeed);
		if (isSafe(newMagnet) && interactablesCount % 4 == 0) {
			tokens.add(newMagnet);
			addGameObject(newMagnet);
		}
		
		Shield newShield = new Shield(posR.nextInt(360) + 20, -50 + posR.nextFloat()*-1*300, gameSpeed);
		if (isSafe(newShield) && interactablesCount % 4 == 0) {
			tokens.add(newShield);
			addGameObject(newShield);
		}

	}

	private static void spawnBlockLayer() {
		Random r1 = new Random();
		int howManyWallsFront = r1.nextInt(6);
		ArrayList<Integer> usedPoolFront = new ArrayList<>();
		for (int i = 0; i < howManyWallsFront; i++) {
			int nextLocation = r1.nextInt(5)+1;
			while(usedPoolFront.contains(nextLocation)) {
				nextLocation = r1.nextInt(5)+1;
			}
			Wall newWall = new Wall(1 + nextLocation*65, -202, 100 + r1.nextInt(100), gameSpeed);
			walls.add(newWall);
			addGameObject(newWall);
		}
		
		Random r2 = new Random();
		int whichOneLess = (int)Math.floor(r2.nextInt(6));
		int lessRange = (int)Math.floor(r2.nextInt(userSnake.getSnakeLength()));
		lessRange = lessRange > 60 ? 60 : lessRange;
		if(lessRange >= 1) {
			Block newBlock = new Block(6 + whichOneLess*65, -262, lessRange, gameSpeed);
			blocks.add(newBlock);
			addGameObject(newBlock);	
		}
		
		for (int i = 0; i < 6; i++) {
			if(i != whichOneLess) {
				int randomValue = (int)Math.floor(Math.random()*56);
				if(randomValue != 0) {
					Block newBlock = new Block(6 + i*65, -262, randomValue, gameSpeed);
					blocks.add(newBlock);
					addGameObject(newBlock);	
				}
			}
		}
		Random r = new Random();
		int howManyWallsBack = r.nextInt(6);
		ArrayList<Integer> usedPoolBack = new ArrayList<>();
		for (int i = 0; i < howManyWallsBack; i++) {
			int nextLocation = r.nextInt(5)+1;
			while(usedPoolBack.contains(nextLocation)) {
				nextLocation = r.nextInt(5)+1;
			}
			Wall newWall = new Wall(1 + nextLocation*65, -262, 100 + r1.nextInt(200), gameSpeed);
			walls.add(newWall);
			addGameObject(newWall);
		}
	}

	static boolean isSafe(GameObject object) {

		for (GameObject token : tokens) {
			if (token.isColliding(object)) {
				return false;
			}
		}
		for (GameObject wall : walls) {
			if (wall.isColliding(object)) {
				return false;
			}
		}
		for (GameObject block : blocks) {
			if (block.isColliding(object)) {
				return false;
			}
		}

		return true;
	}

	public static void setGameSpeed() {
		if(userSnake.getSnakeLength() <= 10) {
			gameSpeed = 4.5;
		}
		else {
			if(userSnake.getSnakeLength() >= 100) {
				gameSpeed = 6;
			}
			else {
				gameSpeed = 4.5 + 1.5/90 * (userSnake.getSnakeLength()-10);
			}
		}
		
		for (GameObject token : tokens) {
			token.setSpeed(gameSpeed);
		}
		for (Wall wall : walls) {
			wall.setSpeed(gameSpeed);
		}
		for (Block block : blocks) {
			block.setSpeed(gameSpeed);
		}
		
	}

}