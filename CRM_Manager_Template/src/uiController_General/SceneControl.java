package uiController_General;

import java.io.IOException;

import uiController_Manager.CustomerInformationScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneControl {
	static final SceneControl instance = new SceneControl();
	static Scene scene = new Scene(new Parent() { });
	
	public static SceneControl getInstance(){
		return instance;
	}
	public Scene getScene(){
		return scene;
	}
	
	public static void loadScene(Pane pane, String fxmlFilename) {
		try {
			Parent root = loadFXMLResource(fxmlFilename);
			scene.setRoot(root);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	private static Parent loadFXMLResource(String fxmlFilename)
			throws IOException {
		FXMLLoader loader = new FXMLLoader(SceneControl.class.getResource(fxmlFilename));
		return loader.load();
	}
	public static void setScene(Parent root) {
		scene.setRoot(root);
	}
}
