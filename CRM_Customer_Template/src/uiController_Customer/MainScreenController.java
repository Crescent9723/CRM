package uiController_Customer;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnMakeOrder;
	@FXML private Button btnShowOrder;
	@FXML private Button btnMakeQuery;
	@FXML private Button btnShowQuery;
	@FXML private Button btnAccountSettings;
	@FXML private Button btnLogout;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handleMakeOrderButton();
		handleShowOrderButton();
		handleMakeQueryButton();
		handleShowQueryButton();
		handleAccountSettingsButton();
		handleLogoutButton();
	}

	private void handleAccountSettingsButton() {
		btnAccountSettings.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/AccountSettingsScreen.fxml");
		});		
	}
	private void handleLogoutButton() {
		btnLogout.setOnAction((event) -> {
			try {
				SQLDatabase.getInstance().disconnect();
				SceneControl.loadScene(pane, "/ui_General/LoginScreen.fxml");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private void handleShowQueryButton() {
		btnShowQuery.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/ShowQueryScreen.fxml");
		});
	}
	private void handleMakeQueryButton() {
		btnMakeQuery.setOnAction((event) -> {
			JList<String> categoryChoice = new JList<String>(new String[]{"Product", "Support", "Refund"});
			JTextArea descriptionText = new JTextArea(30, 30);
			Object[] message = {
					"Category: ", categoryChoice,
					"Description:", descriptionText
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Make Order", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				try {
					SQLDatabase.getInstance().makeQuery((String) categoryChoice.getSelectedValue(), descriptionText.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	private void handleShowOrderButton() {
		btnShowOrder.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/ShowOrderScreen.fxml");
		});
	}
	private void handleMakeOrderButton() {
		btnMakeOrder.setOnAction((event) -> {
			JTextField productText = new JTextField();
			JList<String> paymentChoice = new JList<String>(new String[]{"Visa", "Paypal", "Cash"});
			JTextField descriptionText = new JTextField();
			Object[] message = {
					"Product:", productText,
					"Payment: ", paymentChoice,
					"Description:", descriptionText
			};
			int option = JOptionPane.showConfirmDialog(null, message, "Make Order", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION){
				try {
					SQLDatabase.getInstance().makeOrder(Integer.parseInt(productText.getText()), (String) paymentChoice.getSelectedValue(), descriptionText.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
