package uiController_Customer;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AccountSettingsScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnChangeInfo;
	@FXML private Button btnChangePass;
	@FXML private Button btnDeleteAccount;
	@FXML private Button btnBack;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleChangeInfoButton();
		handleChangePassButton();
		handleDeleteAccountButton();
		handleBackButton();
	}

	private void handleBackButton() {
		btnBack.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/MainScreen.fxml");
		});
	}
	private void handleDeleteAccountButton() {
		btnDeleteAccount.setOnAction((event) -> {
			JTextField passText = new JTextField();
			JTextField passCheckText = new JTextField();
			Object[] message = {
					"Password:", passText,
					"Confirm Password: ", passCheckText,
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Password Check", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				try {
					if (!passText.getText().equals(passCheckText.getText())){
						JOptionPane.showMessageDialog(null, "Password is not equal to confirm password");
					} else if (SQLDatabase.getInstance().checkPassword(SQLDatabase.getInstance().getUserID(), passText.getText())){
						option = JOptionPane.showConfirmDialog(null, "Are you sure to delete account?", "Delete Account", JOptionPane.OK_CANCEL_OPTION);
						if (option == JOptionPane.OK_OPTION){
							SQLDatabase.getInstance().deleteAccount();
							SceneControl.loadScene(pane, "/ui_General/LoginScreen.fxml");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Password is incorrect");
					}
				} catch (Exception e) {
				}
			}
		});
	}
	private void handleChangePassButton() {
		btnChangePass.setOnAction((event) -> {
			JTextField oldPassText = new JTextField();
			JTextField oldPassCheckText = new JTextField();
			JTextField newPassText = new JTextField();
			JTextField newPassCheckText = new JTextField();
			Object[] message = {
					"Old Password:", oldPassText,
					"Confirm Old Password: ", oldPassCheckText,
					"New Password:", newPassText,
					"Confirm New Password: ", newPassCheckText,
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Password Change", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				try {
					if (!oldPassText.getText().equals(oldPassCheckText.getText())){
						JOptionPane.showMessageDialog(null, "Old Password is not equal to confirm old password");
					} else if (!newPassText.getText().equals(newPassCheckText.getText())){
						JOptionPane.showMessageDialog(null, "New Password is not equal to confirm new password");
					} else if (SQLDatabase.getInstance().checkPassword(SQLDatabase.getInstance().getUserID(), oldPassText.getText())){
						SQLDatabase.getInstance().changePassword(newPassText.getText());
						JOptionPane.showMessageDialog(null, "Password is changed! Please login again");
						SceneControl.loadScene(pane, "/ui_General/LoginScreen.fxml");
					} else {
						JOptionPane.showMessageDialog(null, "Password is incorrect");
					}
				} catch (Exception e) {
				}
			}
		});
	}
	private void handleChangeInfoButton() {
		btnChangeInfo.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/InformationChangeScreen.fxml");
		});
	}

}
