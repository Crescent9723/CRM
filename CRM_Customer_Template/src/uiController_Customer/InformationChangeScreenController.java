package uiController_Customer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class InformationChangeScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private TextField editLastName;
	@FXML private TextField editFirstName;
	@FXML private TextField editAddress;
	@FXML private TextField editCity;
	@FXML private TextField editPostalCode;
	@FXML private TextField editCountry;
	@FXML private TextField editPhone;
	@FXML private Button btnOK;
	@FXML private Button btnCancel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			initializeTextField();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handleOKButton();
		handleCancelButton();
	}
	private void initializeTextField() throws SQLException {
		ResultSet rs = SQLDatabase.getInstance().getAccountInfo();
		editLastName.setText(rs.getString(3));
		editFirstName.setText(rs.getString(4));
		editAddress.setText(rs.getString(5));
		editCity.setText(rs.getString(6));
		editPostalCode.setText(rs.getString(7));
		editCountry.setText(rs.getString(8));
		editPhone.setText(rs.getString(9));
	}
	
	private void handleOKButton() {
		btnOK.setOnAction((event) -> {
			try {
				SQLDatabase.getInstance().changeInformation(editLastName.getText(), editFirstName.getText(), editAddress.getText(), editCity.getText(),
						editPostalCode.getText(), editCountry.getText(), editPhone.getText());
				SceneControl.loadScene(pane, "/ui_Customer/AccountSettingsScreen.fxml");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	private void handleCancelButton() {
		btnCancel.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/AccountSettingsScreen.fxml");
		});
	}

}
