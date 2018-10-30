package application;
	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


@SuppressWarnings("serial")
class Leaderboard implements Serializable {

    PriorityQueue<Score> scoreList = new PriorityQueue<>(new Comparator<Score>() {
        
    	@Override
        public int compare(Score o1, Score o2) {
    		return o2.getScore() - o1.getScore();
        }
        
    });

    public void addScore(Score score){
        scoreList.add(score);
    }

    public ArrayList<Score> getLeaderboard(){

        ArrayList<Score> top10 = new ArrayList<>();
        int l = scoreList.size();
        
        for(int i = 0; i < Math.min(10, l); i++){
            top10.add(scoreList.poll());
        }

        for(int i = 0; i < top10.size(); i++){
        	
            scoreList.add(top10.get(i));
        
        }
        
        return top10;
    }

}
 
@SuppressWarnings("serial")
class Score implements Serializable{

	private int score;
    private Date date;

    public Score(int score, Date date) {
    	this.score = score;
    	this.date = date;
    }
    
    public int getScore() {
        return score;
    }

    public String getDate() {
    	return date.toString().substring(0, 19);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setScore(int score) {
        this.score = score;
    }

}

class Snake extends Node{
	
	public Snake() {
		super();
	}	
	
}

class menuButton extends Button{
	
	public menuButton(String name){
		super(name);
		this.getStyleClass().add("menuBtn");
	}
	
}

class GameScene extends Scene{
	
	private static Pane root = new Pane();
	
	public GameScene(int scenewidth, int sceneheight) {
		super(root, scenewidth, sceneheight);
		
	}
	
}

class backButton extends Button{
	
	public backButton(){
		super("");
		this.getStyleClass().add("backBtn");
	}
	
}


public class Main extends Application {
	
	private static final int sceneWidth = 400;
	private static final int sceneHeight = 600;
	private Stage stage;
	private backEventHandler backEventBtn = new backEventHandler();
	
	class backEventHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent e) {
			stage.setScene(createMainScene());
		}
		
	}
	
	private Scene createMainScene() {
		
		VBox root = new VBox();
		Image menuImage = null;
		try {
			menuImage = new Image(new FileInputStream("./img/logo.jpg"));
		} catch (FileNotFoundException e) {
			System.out.println("Logo Image Not Found!");
			e.printStackTrace();
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
		root.setStyle("-fx-background-color: #000;");
		Scene menuScene = new Scene(root, sceneWidth, sceneHeight);
		
		menuScene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm()
			);
		
		return menuScene;
	
	}
	
	protected Scene createGameScene() {
		
		GameScene gameScene = null;
		
		gameScene = new GameScene(sceneWidth, sceneHeight);
        
		gameScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);
        		
        return gameScene;
	
	}
	
	protected Scene resumeGameScene() {
		
		Scene resumeScene = null;
		
		VBox root = new VBox();
        
		Button backBtn = new backButton();
		backBtn.setOnAction(backEventBtn);
		
		Text randomText = new Text("Game Will Resume!");
		randomText.setFill(Color.WHITE);
		randomText.getStyleClass().add("placeholderText");
		root.getChildren().addAll(backBtn, randomText);
        
		root.setStyle("-fx-background-color: #000;");
        
        resumeScene = new Scene(root, sceneWidth, sceneHeight);
        resumeScene.getStylesheets().add(
        		getClass().getResource("application.css").toExternalForm()
        	);
        		
        return resumeScene;
	
	}

	
	protected Scene showLeaderboardScene() {
		
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
		HBox top = new HBox();
		HBox backB = new HBox();
		Button backButton = new Button("Back");
		backB.getChildren().add(backButton);
		root.getChildren().add(backB);
		backB.setAlignment(Pos.CENTER_LEFT);
		backB.setPadding(new Insets(5,5,5,5));

		Text heading = new Text("Leaderboards");
		heading.setFill(Color.WHITE);
		heading.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
		root.getChildren().add(heading);
		top.setAlignment(Pos.CENTER);


		VBox ranks = new VBox();
		VBox score = new VBox();
		VBox date = new VBox();

		Text rankL = new Text("Rank");
		rankL.setFill(Color.WHITE);
		rankL.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		ranks.getChildren().add(rankL);


		Text scoreL = new Text("Score");
		scoreL.setFill(Color.WHITE);
		scoreL.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		score.getChildren().add(scoreL);


		Text dateL = new Text("Date");
		dateL.setFill(Color.WHITE);
		dateL.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
		date.getChildren().add(dateL);


		HBox boardColumns = new HBox();
		
		for(int i = 0; i < leaderboards.size(); i++) {
			Text rankT = new Text((i+1) + "");
			rankT.setFill(Color.WHITE);
			rankT.setFont(Font.font("Courier New", 22));

			Text scoreT = new Text(leaderboards.get(i).getScore() + "");
			scoreT.setFill(Color.WHITE);
			scoreT.setFont(Font.font("Courier New", 22));

			Text dateT = new Text(leaderboards.get(i).getDate());
			dateT.setFill(Color.WHITE);
			dateT.setFont(Font.font("Courier New", 22));

			ranks.getChildren().add(rankT);
			score.getChildren().add(scoreT);
			date.getChildren().add(dateT);
		}
		ranks.setAlignment(Pos.CENTER);
		score.setAlignment(Pos.CENTER);
		date.setAlignment(Pos.CENTER);

		boardColumns.getChildren().add(ranks);
		boardColumns.getChildren().add(score);
		boardColumns.getChildren().add(date);

		root.getChildren().add(boardColumns);

		root.setAlignment(Pos.CENTER);
		
		root.setAlignment(Pos.TOP_CENTER);

		root.setStyle("-fx-background-color: #000;");
        
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
