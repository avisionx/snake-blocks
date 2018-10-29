package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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

public class Main extends Application {
	
	private Scene createHomeScreen() {
		Pane root = new Pane();
		Button play = new Button("Play");
		play.setOnAction(e -> {
			GameScene g = new GameScene();
			g.start();
		});
		Button exit = new Button("exit");
		exit.setOnAction(e -> {
			System.out.println("Exit Clicked");
		});
		play.getStyleClass().add("btn");
		
		root.getChildren().addAll(play, exit);
		return new Scene(root, 400, 600);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Scene scene = createHomeScreen();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
