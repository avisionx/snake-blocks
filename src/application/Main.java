package application;
	
import java.io.FileInputStream;
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


@SuppressWarnings("serial")
class ImageNotFound extends Exception{
	
	public ImageNotFound(String pathToImage) {
		super("ImageNotFound! Path: " + pathToImage);
	}
	
}

class menuButton extends Button{
	
	public menuButton(String name){
		super(name);
		this.getStyleClass().add("menuBtn");
	}
	
}

class backButton extends Button{
	
	public backButton(){
		super("");
		this.getStyleClass().add("backBtn");
	}
	
}


public class Main extends Application {
	
	class backEventHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent e) {
			stage.setScene(createMainScene());
		}
		
	}
	
	private static final int sceneWidth = 400;
	private static final int sceneHeight = 600;
	private Stage stage;
	private backEventHandler backEventBtn = new backEventHandler();
	
	public static int getScenewidth() {
		return sceneWidth;
	}
	
	public static int getSceneheight() {
		return sceneHeight;
	}
	
	private Scene createMainScene() {
		
		Scene menuScene = null;
		VBox root = new VBox();
		
		Image menuImage = null;
		try {
			String pathToImage = "./img/logo.jpg";
			menuImage = new Image(new FileInputStream(pathToImage));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		ImageView menuImageView = null;
		
		if(menuImage != null) {
			menuImageView = new ImageView(menuImage);
			menuImageView.setX(0);
			menuImageView.setY(0);
			menuImageView.setFitWidth(sceneWidth);
			menuImageView.setPreserveRatio(true);  
		}
		
		Button playBtn = new menuButton("Play");
		Button resumeBtn = new menuButton("Resume");
		Button leaderBoardBtn = new menuButton("Leaderboard");
		Button exitBtn = new menuButton("Exit");
		
		playBtn.setOnAction(e -> {
			stage.setScene(createGameScene());
		});
		
		resumeBtn.setOnAction(e -> {
			stage.setScene(resumeGameScene());
		});
		
		leaderBoardBtn.setOnAction(e -> {
			stage.setScene(showLeaderboardScene());
		});
		
		exitBtn.setOnAction(e -> {
			System.exit(0);
		});
		
		if(menuImage != null) {
			root.getChildren().addAll(menuImageView, playBtn, resumeBtn, leaderBoardBtn, exitBtn);
		}
		else {
			root.getChildren().addAll(playBtn, resumeBtn, leaderBoardBtn, exitBtn);
		}
		
		root.setAlignment(Pos.CENTER);
		root.getStyleClass().add("rootBg");
		
		menuScene = new Scene(root, sceneWidth, sceneHeight);
		menuScene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm()
			);
		
		return menuScene;
	
	}
	
	private Scene createGameScene() {
		
		Scene gameScene = new GameScene();
		
		gameScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);

        return gameScene;
	
	}
	
	private Scene resumeGameScene() {
		
		Scene resumeScene = null;
		
		VBox root = new VBox();
        
		Button backBtn = new backButton();
		backBtn.setOnAction(backEventBtn);
		
		Text randomText = new Text("Game Will Resume!");
		randomText.setFill(Color.WHITE);
		randomText.getStyleClass().add("placeholderText");
		
		root.getChildren().addAll(backBtn, randomText);
		root.getStyleClass().add("rootBg");
        
        resumeScene = new Scene(root, sceneWidth, sceneHeight);
        resumeScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);
        		
        return resumeScene;
	
	}

	
	private Scene showLeaderboardScene() {
		
//		Some Static Random LeaderBoard Data
		Leaderboard l = new Leaderboard();
		Score s1 = new Score(6, new Date());
		Score s2 = new Score(2, new Date());
		Score s3 = new Score(7, new Date());
		Score s4 = new Score(9, new Date());
		Score s5 = new Score(10, new Date());
		l.addScore(s1);
		l.addScore(s2);
		l.addScore(s3);
		l.addScore(s4);
		l.addScore(s5);
		
//		Final LeaderBoard Array

		ArrayList<Score> leaderboards = l.getLeaderboard();
		
		Scene leaderboardScene = null;

		VBox root = new VBox();
		
		HBox headingWrapper = new HBox();
		HBox backBtnWrapper = new HBox();
		
		Button backBtn = new backButton();
		backBtn.setOnAction(backEventBtn);
		
		backBtnWrapper.getChildren().add(backBtn);
		backBtnWrapper.setAlignment(Pos.CENTER_LEFT);
		backBtnWrapper.setPadding(new Insets(5,0,0,5));

		Text heading = new Text("LEADERBOARD");
		
		heading.setFill(Color.WHITE);
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 35));
		
		headingWrapper.getChildren().add(heading);
		headingWrapper.setAlignment(Pos.CENTER);
		headingWrapper.setPadding(new Insets(0,0,20,0));
		
		VBox ranks = new VBox();
		VBox scores = new VBox();
		VBox dates = new VBox();

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


		HBox leadTable = new HBox();
		
		for(int i = 0; i < leaderboards.size(); i++) {
			
			Text rankText = new Text((i+1) + "");
			rankText.setFill(Color.WHITE);
			rankText.setFont(Font.font("Courier New", 19));

			Text scoreText = new Text(leaderboards.get(i).getScore() + "");
			scoreText.setFill(Color.WHITE);
			scoreText.setFont(Font.font("Courier New", 19));

			Text dateText = new Text(leaderboards.get(i).getDate());
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
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			this.stage = primaryStage;
			Scene scene = createMainScene();
			primaryStage.setScene(scene);
	        primaryStage.setTitle("Sanke VS Blocks");
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
