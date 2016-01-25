package uiController_General;

import java.net.URL;
import java.util.ResourceBundle;







import javax.swing.JOptionPane;

import sqlDatabase.SQLDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CreateAccountScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnOK;
	@FXML private Button btnCancel;
	@FXML private TextField editUserId;
	@FXML private TextField editUserPass;
	@FXML private TextField editLastName;
	@FXML private TextField editFirstName;
	@FXML TextField editAddress;
	@FXML TextField editCity;
	@FXML TextField editCountry;
	@FXML TextField editPostalCode;
	@FXML TextField editPhoneNumber;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleCancelButton();
		handleOKButton();
	}

	private void handleOKButton() {
		btnOK.setOnAction((event) -> {
			try {
				if (SQLDatabase.getInstance().createNewAccount(editUserId.getText(), editUserPass.getText(), editLastName.getText(), editFirstName.getText(), editAddress.getText(),
						editCity.getText(), editCountry.getText(), editPostalCode.getText(), editPhoneNumber.getText())){
					JOptionPane.showMessageDialog(null, "Account is created");
					SceneControl.loadScene(pane, "/ui_General/LoginScreen.fxml");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	private void handleCancelButton() {
		btnCancel.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_General/LoginScreen.fxml");
		});
	}

}
