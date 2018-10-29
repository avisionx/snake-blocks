package application;
	
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class GameScene extends Thread{
	
	public void run() {
		try
        { 
            // Displaying the thread that is running 
            System.out.println ("Thread " + 
                  Thread.currentThread().getId() + 
                  " is running"); 
  
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            System.out.println ("Exception is caught"); 
        } 
	}
	
}

class menuButton extends Button{
	
	public menuButton(String name){
		super(name);
		this.getStyleClass().add("menuBtn");
	}
	
}

public class Main extends Application {
	
	private static final int sceneWidth = 400;
	private static final int sceneHeight = 600;
	
	private Scene createMainScreen(Stage primaryStage) {
		
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
			System.out.println("Play new game!");
		});
		
		resumeBtn.setOnAction(e -> {
			System.out.println("Resume game!");
		});
		
		leaderBoardBtn.setOnAction(e -> {
			System.out.println("Show leaderboard!");
		});
		
		exitBtn.setOnAction(e -> {
			System.out.println("Exit game!");
		});
		
		if(menuImage != null) {
			root.getChildren().addAll(menuImageView, playBtn, resumeBtn, leaderBoardBtn, exitBtn);
		}
		else {
			root.getChildren().addAll(playBtn, resumeBtn, leaderBoardBtn, exitBtn);
		}
		
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-color: #000;");
		
		return new Scene(root, sceneWidth, sceneHeight);
	
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = createMainScreen(primaryStage);
			scene.getStylesheets().add(
				getClass().getResource("application.css").toExternalForm()
			);
			primaryStage.setScene(scene);
			primaryStage.setTitle("HomeScreen");
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
