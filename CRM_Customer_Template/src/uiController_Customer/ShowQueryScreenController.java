package uiController_Customer;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import sqlDatabase.SQLDatabase;
import uiController_General.SceneControl;
import data.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class ShowQueryScreenController implements javafx.fxml.Initializable {

	@FXML AnchorPane pane;
	@FXML private Button btnBack;
	@FXML private TableView<Query> queryListTable;
	@FXML private TableColumn<Query, Integer> queryIDColumn;
	@FXML private TableColumn<Query, String> categoryColumn;
	@FXML private TableColumn<Query, Date> dateColumn;
	@FXML private TableColumn<Query, String> textColumn;
	@FXML private TableColumn<Query, String> answerColumn;
	private ObservableList<Query> data = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateData();
		handleBackButton();
	}
	
	private void setCellProperty() {
		queryIDColumn.setCellValueFactory(new PropertyValueFactory<Query, Integer>("queryID"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<Query, String>("category"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<Query, Date>("date"));
	}
	private void setAnswerButtonCellProperty() {
		answerColumn.setCellFactory(new Callback<TableColumn<Query, String>, TableCell<Query, String>>(){

			@Override
			public TableCell<Query, String> call(TableColumn<Query, String> param) {
				final TableCell<Query, String> cell = new TableCell<Query, String>() {

					@SuppressWarnings("rawtypes")
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						Button button = new Button("Show");
						button.setAlignment(Pos.CENTER);
						button.setStyle("-fx-background-color: lightblue");
						final TableCell<Query, String> c = this;
						TableRow row = c.getTableRow();
						if (row.isEmpty()){
							button.setVisible(false);
						} else {
							if(row.getIndex() != -1){
								if(data.get(row.getIndex()).getAnswer() == null) {
									button.setVisible(false);
								} else {
									if (data.get(row.getIndex()).getAnswer().equals("0")){
										button.setVisible(false);
									}
								}
							}
						}
						button.setOnAction((event) -> {
							String value = data.get(row.getIndex()).getAnswer();
							JOptionPane.showMessageDialog(null, value, "Answer", JOptionPane.INFORMATION_MESSAGE);
						});
						setGraphic(button);
					}
					
				};
				return cell;
			}
			
		});
	}
	private void setTextButtonCellProperty() {
		textColumn.setCellFactory(new Callback<TableColumn<Query, String>, TableCell<Query, String>>(){

			@Override
			public TableCell<Query, String> call(TableColumn<Query, String> param) {
				final TableCell<Query, String> cell = new TableCell<Query, String>() {

					@SuppressWarnings("rawtypes")
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						Button button = new Button("Show");
						button.setAlignment(Pos.CENTER);
						button.setStyle("-fx-background-color: lightblue");
						final TableCell<Query, String> c = this;
						TableRow row = c.getTableRow();
						if (row.isEmpty()){
							button.setVisible(false);
						}
						button.setOnAction((event) -> {
							String value = data.get(row.getIndex()).getText();
							JOptionPane.showMessageDialog(null, value, "Query Text", JOptionPane.INFORMATION_MESSAGE);
						});
						setGraphic(button);
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
			ResultSet rs = SQLDatabase.getInstance().getQueryList(false);
			data.clear();
			while (rs.next()){
				Query newQuery = new Query(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getString(6));
				data.add(newQuery);
			}
			setCellProperty();
			setTextButtonCellProperty();
			setAnswerButtonCellProperty();
			queryListTable.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
