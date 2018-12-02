package application;
	
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * ImageNotFound custom exception class.
 * extends Exception
 */
@SuppressWarnings("serial")
class ImageNotFound extends Exception{

	/**
	 * constructor for custom exception ImageNotFound
	 * @param pathToImage - denotes the path to image
	 */
	public ImageNotFound(String pathToImage) {
		super("ImageNotFound! Path: " + pathToImage);
	}
	
}

/**
 * menuButton class for creation of Menu Button
 * extends Button
 */
class menuButton extends Button{

	/**
	 * constructor for the menuButton class
	 * @param name - Text for Menu Button
	 */
	public menuButton(String name){
		super(name);
		this.getStyleClass().add("menuBtn");
	}
	
}

/**
 * backButton class for creation of back button
 * extends Button
 */
class backButton extends Button{

	/**
	 * constructor for backButton
	 */
	public backButton(){
		super("");
		this.getStyleClass().add("backBtn");
	}
	
}

// Public Main Class Definition

/**
 * Main class which initiates the entire application.
 * extends Application
 * Fields -
 * sceneWidth - width of the scene
 * sceneHeight - height of the scene
 * leaderboard - Leaderboard object for the Leaderboard of the game
 * stage - Stage Object for the primary stage
 * mainMenuScene - Scene for main menu
 * oldGamePresent - boolean for checking whether previous data exist to be loaded
 * savedData - ContentSaver Object to saved the game data
 */
public class Main extends Application {
	
//	Variable Definitions

	private static final int sceneWidth = 400;
	private static final int sceneHeight = 600;
	private static Leaderboard leaderboard;
	
	private Stage stage;
	private Scene mainMenuScene;
	protected static boolean oldGamePresent;
	protected static ContentSaver savedData;

	/**
	 * backEventHandler class handles the event when user
	 */
	class backEventHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent e) {
			stage.setScene(mainMenuScene);
		}
		
	}
	private backEventHandler backEventBtn = new backEventHandler();

//	Getter Setter For Width And Height

	/**
	 * getter for scene width
	 * @return integer value i.e. the width of the scene
	 */
	public static int getScenewidth() {
		return Main.sceneWidth;
	}

	/**
	 * getter for height of the scene
	 * @return integer value i.e. the height of the scene
	 */
	public static int getSceneheight() {
		return Main.sceneHeight;
	}

	/**
	 * getter for leaderboard
	 * @return Leaderboard object i.e. the leaderboard for the game
	 */
	public static Leaderboard getLeaderboard() {
		return Main.leaderboard;
	}
		
//	Creates MainScene

	/**
	 * Creates the MainScene i.e. the main menu of the applicaton, which includes play, resume [depends on if there is data to load], leaderboard and exit
	 * @return Scene
	 */
	@SuppressWarnings("unchecked")
	private Scene createMainScene() {
		
		Scene menuScene = null;
		VBox root = new VBox();
		Image menuImage = null;
		ImageView menuImageView = null;
		Button playBtn = new menuButton("Play");
		Button resumeBtn = new menuButton("Resume");
		Button leaderBoardBtn = new menuButton("Leaderboard");
		Button exitBtn = new menuButton("Exit");
		
		playBtn.setOnAction(e -> {
			stage.setScene(createGameScene(this.mainMenuScene));
		});
		
		resumeBtn.setOnAction(e -> {
			stage.setScene(resumeGameScene(this.mainMenuScene));
		});
		
		leaderBoardBtn.setOnAction(e -> {
			stage.setScene(showLeaderboardScene());
		});
		
		exitBtn.setOnAction(e -> {
			System.exit(0);
		});
		
		savedData = null;
		Object obj = null;
		try {
			obj = SaveManager.load("./data/saveData.txt");
		} catch (EOFException e){
			savedData = null;
			oldGamePresent = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(obj != null) {
			savedData = (ContentSaver) obj;
		}
		if(savedData != null) {
			Main.oldGamePresent = true;
		}
		else {
			Main.oldGamePresent = false;
		}

		try {
			String pathToImage = "./img/logo.jpg";
			menuImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		if(menuImage != null) {
			menuImageView = new ImageView(menuImage);
			menuImageView.setX(0);
			menuImageView.setY(0);
			menuImageView.setFitWidth(sceneWidth);
			menuImageView.setPreserveRatio(true);  
		}
				
		if(menuImage != null) {
			root.getChildren().addAll(menuImageView, resumeBtn, playBtn, leaderBoardBtn, exitBtn);
		}
		else {
			root.getChildren().addAll(resumeBtn, playBtn, leaderBoardBtn, exitBtn);
		}
		if(!Main.oldGamePresent) {
			resumeBtn.setVisible(false);
		}
		else {
			resumeBtn.setVisible(true);
		}
		
		root.setAlignment(Pos.CENTER);
		root.getStyleClass().add("rootBg");
		menuScene = new Scene(root, sceneWidth, sceneHeight);
		menuScene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm()
			);
		
		leaderboard = new Leaderboard();
		
		ArrayList<Score> retrieveScore = null;
		ObjectInputStream in = null;
		obj = null;
		try {
			in = new ObjectInputStream(new FileInputStream("./data/leaderboard.txt"));	
			obj = in.readObject();
			if(obj != null){
				if(obj instanceof ArrayList)
					retrieveScore = (ArrayList<Score>) obj;
				for(int i = 0; i < retrieveScore.size(); i++) {
					leaderboard.addScore(retrieveScore.get(i));
				}
			}
			in.close();
		} catch (EOFException e) {
			leaderboard = new Leaderboard();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return menuScene;
	}
	
//	Plays the game creates gameScene

	/**
	 * creates the gameScene i.e. new game
	 * @param mainScene - main scene
	 * @return Scene i.e. the game scene
	 */
	private Scene createGameScene(Scene mainScene) {
		
		try {
			SaveManager.save(null, "./data/saveData.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Scene gameScene = new GameScene(stage, mainScene);
		
		gameScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);

        return gameScene;
	
	}
	
//	Resume previous any closed game creates gameScene

	/**
	 * creates the resum game scene which shows on resuming the game
	 * @param mainScene - main scene
	 * @return Scene i.e. the resume scene
	 */
	private Scene resumeGameScene(Scene mainScene) {
		
		
		Scene resumeScene = new GameScene(stage, mainScene, Main.savedData);
		
		resumeScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);

        return resumeScene;
	
	}

//	Display Leaderboard

	/**
	 * showLeaderBoardScene method is used to display the leaderboard, which consists of the top 10 scores of the game
	 * @return Scene i.e. the leaderboard scene
	 */
	private Scene showLeaderboardScene() {

		Scene leaderboardScene = null;
		VBox root = new VBox();
		HBox headingWrapper = new HBox();
		HBox backBtnWrapper = new HBox();
		ArrayList<Score> leaderboardList = null;
		Button backBtn = new backButton();
		Text heading = new Text("LEADERBOARD");
		VBox ranks = new VBox();
		VBox scores = new VBox();
		VBox dates = new VBox();
		HBox leadTable = new HBox();
				
		leaderboardList = leaderboard.getLeaderboard();		
		
		backBtn.setOnAction(backEventBtn);
		
		backBtnWrapper.getChildren().add(backBtn);
		backBtnWrapper.setAlignment(Pos.CENTER_LEFT);
		backBtnWrapper.setPadding(new Insets(5,0,0,5));

		heading.setFill(Color.WHITE);
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 35));
		
		headingWrapper.getChildren().add(heading);
		headingWrapper.setAlignment(Pos.CENTER);
		headingWrapper.setPadding(new Insets(0,0,20,0));
		
		ranks.setSpacing(12);
		scores.setSpacing(12);
		dates.setSpacing(12);
		
		Text rankHead = new Text("Rank");
		rankHead.setFill(Color.WHITE);
		rankHead.setFont(Font.font("Helvetica", 22));
		ranks.getChildren().add(rankHead);

		Text scoreHead = new Text("Score");
		scoreHead.setFill(Color.WHITE);
		scoreHead.setFont(Font.font("Helvetica", 22));
		scores.getChildren().add(scoreHead);

		Text dateHead = new Text("Date");
		dateHead.setFill(Color.WHITE);
		dateHead.setFont(Font.font("Helvetica", 22));
		dates.getChildren().add(dateHead);

		for(int i = 0; i < leaderboardList.size(); i++) {
			
			Text rankText = new Text((i+1) + "");
			rankText.setFill(Color.WHITE);
			rankText.setFont(Font.font("Courier New", 19));

			Text scoreText = new Text(leaderboardList.get(i).getScore() + "");
			scoreText.setFill(Color.WHITE);
			scoreText.setFont(Font.font("Courier New", 19));

			Text dateText = new Text(leaderboardList.get(i).getDate());
			dateText.setFill(Color.WHITE);
			dateText.setFont(Font.font("Courier New", 19));

			ranks.getChildren().add(rankText);
			scores.getChildren().add(scoreText);
			dates.getChildren().add(dateText);
			
		}
		
		ranks.setAlignment(Pos.CENTER);
		scores.setAlignment(Pos.CENTER);
		dates.setAlignment(Pos.CENTER);
		
		leadTable.getChildren().addAll(ranks, scores, dates);
		leadTable.setSpacing(15);
		leadTable.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(backBtnWrapper, headingWrapper, leadTable);
		root.setAlignment(Pos.TOP_CENTER);
		root.getStyleClass().add("rootBg");
        
		leaderboardScene = new Scene(root, sceneWidth, sceneHeight);
 
        leaderboardScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);
       
		return leaderboardScene;
	
	}
	
//	Start method overridden

	/**
	 * overridden start method to initiate the application
	 * @param primaryStage - Stage for the application
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			this.stage = primaryStage;
			this.mainMenuScene = createMainScene();
			primaryStage.setScene(this.mainMenuScene);
	        primaryStage.setTitle("Sanke VS Blocks");
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Main method to run the app which thereby calls start() method
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
