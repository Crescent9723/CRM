package application;
	
import java.io.IOException;

import uiController_General.SceneControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root;
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui_Manager/MainScreen.fxml"));
				root = loader.load();
				SceneControl.setScene(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
			primaryStage.setScene(SceneControl.getInstance().getScene());
			primaryStage.setTitle("Customer Relationship Model(Manager Ver.)");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
