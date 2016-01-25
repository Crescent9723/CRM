package uiController_General;

import java.net.URL;
import java.util.ResourceBundle;












import javax.swing.JOptionPane;

import data.ConnectionStatus;
import sqlDatabase.SQLDatabase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class LoginScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnLogin;
	@FXML private Button btnCreateAccount;
	@FXML private Button btnQuit;
	@FXML private TextField editLoginId;
	@FXML private TextField editLoginPass;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleLoginButton();
		handleQuitButton();
		handleCreateAccountButton();
	}

	private void handleCreateAccountButton() {
		btnCreateAccount.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_General/CreateAccountScreen.fxml");
		});
	}
	private void handleQuitButton() {
		btnQuit.setOnAction((event) -> {
			Platform.exit();
		});
	}
	private void handleLoginButton() {
		btnLogin.setOnAction((event) -> {
			String userId = editLoginId.getText();
			String userPass = editLoginPass.getText();
			try {
				ConnectionStatus status = SQLDatabase.getInstance().connect(userId, userPass);
				if (status == ConnectionStatus.SUCCESS_WITH_CUSTOMER){
					SceneControl.loadScene(pane, "/ui_Customer/MainScreen.fxml");
				}
				else if (status == ConnectionStatus.SUCCESS_WITH_MANAGER){
					SceneControl.loadScene(pane, "/ui_Manager/MainScreen.fxml");
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "SQL Connection is not established");
			}
		});
	}

}
