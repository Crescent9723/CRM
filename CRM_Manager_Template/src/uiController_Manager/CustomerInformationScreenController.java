package uiController_Manager;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CustomerInformationScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Label lblLastName;
	@FXML private Label lblFirstName;
	@FXML private Label lblAddress;
	@FXML private Label lblCity;
	@FXML private Label lblPostalCode;
	@FXML private Label lblCountry;
	@FXML private Label lblPhone;
	@FXML private Button btnShowOrder;
	@FXML private Button btnShowQuery;
	@FXML private Button btnDeleteAccount;
	@FXML private Button btnReactivateAccount;
	@FXML private Button btnBack;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			setInitialValues();
			btnShowOrder.setOnAction((event) -> {
				SceneControl.loadScene(pane, "/ui_Manager/CustomerOrderScreen.fxml");
			});
			btnShowQuery.setOnAction((event) -> {
				SceneControl.loadScene(pane, "/ui_Manager/CustomerQueryScreen.fxml");
			});
			btnDeleteAccount.setOnAction((event) -> {
				int option = JOptionPane.showConfirmDialog(null, "Do you really want to delete this account?", "Account Deletion", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION){
					try {
						SQLDatabase.getInstance().deleteAccount();
						SceneControl.loadScene(pane, "/ui_Manager/MainScreen.fxml");
					} catch (Exception e) {
					}
				}
			});
			btnReactivateAccount.setOnAction((event) -> {
				int option = JOptionPane.showConfirmDialog(null, "Do you really want to reactivate this account?", "Account Reactivation", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION){
					try {
						SQLDatabase.getInstance().reactivateAccount();
					} catch (Exception e) {
					}
				}
			});
			btnBack.setOnAction((event) -> {
				SceneControl.loadScene(pane, "/ui_Manager/SearchCustomerScreen.fxml");
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setInitialValues() throws SQLException {
		ResultSet infoSet = SQLDatabase.getInstance().getAccountInfo();
		lblLastName.setText(infoSet.getString("LastName"));
		lblFirstName.setText(infoSet.getString("FirstName"));
		lblAddress.setText(infoSet.getString("Address"));
		lblCity.setText(infoSet.getString("City"));
		lblPostalCode.setText(infoSet.getString("PostalCode"));
		lblCountry.setText(infoSet.getString("Country"));
		lblPhone.setText(infoSet.getString("Phone"));
	}
}
