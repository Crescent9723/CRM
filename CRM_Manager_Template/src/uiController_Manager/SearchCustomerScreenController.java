package uiController_Manager;

import java.net.URL;
import java.util.ResourceBundle;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SearchCustomerScreenController implements javafx.fxml.Initializable {
	
	@FXML private AnchorPane pane;
	@FXML private Button btnSearch;
	@FXML private Button btnBack;
	@FXML private TextField editUserId;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnSearch.setOnAction((event) -> {
			SQLDatabase.getInstance().setCustomerID(editUserId.getText());
			SceneControl.loadScene(pane, "/ui_Manager/CustomerInformationScreen.fxml");
		});
		btnBack.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Manager/MainScreen.fxml");
		});
	}

}
