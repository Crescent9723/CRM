package uiController_Customer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import data.Order;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class ShowOrderScreenController implements javafx.fxml.Initializable {

	@FXML private AnchorPane pane;
	@FXML private Button btnBack;
	@FXML private TableView<Order> orderListTable;
	@FXML private TableColumn<Order, Integer> orderIDColumn;
	@FXML private TableColumn<Order, String> productIDColumn;
	@FXML private TableColumn<Order, String> productNameColumn;
	@FXML private TableColumn<Order, String> descriptionColumn;
	@FXML private TableColumn<Order, Boolean> deliveredColumn;
	private ObservableList<Order> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateData();
		handleBackButton();
	}

	private void setCellValueFactory() {
		orderIDColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
		productIDColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("productID"));
		productNameColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("productName"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("description"));
		deliveredColumn.setCellFactory(new Callback<TableColumn<Order, Boolean>, TableCell<Order, Boolean>>(){

			@Override
			public TableCell<Order, Boolean> call(TableColumn<Order, Boolean> param) {
				final TableCell<Order, Boolean> cell = new TableCell<Order, Boolean>() {

					@SuppressWarnings("rawtypes")
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						CheckBox check = new CheckBox();
						final TableCell<Order, Boolean> c = this;
						TableRow row = c.getTableRow();
						if (row.isEmpty()){
							check.setVisible(false);
						} else {
							if (row.getIndex() != -1){
								check.setDisable(true);
								check.setSelected(data.get(row.getIndex()).isDelivered());
							}
						}
						setGraphic(check);
					}
					
				};
				return cell;
			}
			
		});
	}
	
	private void handleBackButton() {
		btnBack.setOnAction((event) -> {
			SceneControl.loadScene(pane, "/ui_Customer/MainScreen.fxml");
		});
	}

	private void updateData() {
		try {
			ResultSet rs = SQLDatabase.getInstance().getOrderList(false);
			data.clear();
			while (rs.next()){
				String productName = SQLDatabase.getInstance().getProductName(rs.getString(4));
				Order newOrder = new Order(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getString(4), productName, rs.getString(5), rs.getString(6), rs.getBoolean(7));
				data.add(newOrder);
			}
			setCellValueFactory();
			orderListTable.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
