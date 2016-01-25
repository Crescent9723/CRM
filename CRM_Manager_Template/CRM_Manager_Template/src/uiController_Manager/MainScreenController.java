package uiController_Manager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnQueryList;
	@FXML private Button btnOrderList;
	@FXML private Button btnStatisticalReview;
	@FXML private Button btnSearchCustomer;
	@FXML private Button btnQuit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			SQLDatabase.getInstance().connectWithMasterUser();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		handleQueryListButton();
		handleOrderListButton();
		handleStatisticalReviewButton();
		handleSearchCustomerButton();
		handleQuitButton();
	}

	private void handleQuitButton() {
		btnQuit.setOnAction((event) -> {
			try {
				SQLDatabase.getInstance().disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.exit();
		});
	}
	
	private void handleSearchCustomerButton() {
		btnSearchCustomer.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Manager/SearchCustomerScreen.fxml");
		});
	}
	private void handleStatisticalReviewButton() {
		btnStatisticalReview.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Manager/StatisticalReviewScreen.fxml");
		});
	}
	
	private void handleOrderListButton() {
		btnOrderList.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Manager/OrderListScreen.fxml");
		});
	}
	private void handleQueryListButton() {
		btnQueryList.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Manager/QueryListScreen.fxml");
		});
	}

}
