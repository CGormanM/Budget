package controller;

import java.io.IOException;
import java.util.Collections;

import fxTools.Controller;
import fxTools.ViewLoader;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.Bill;
import model.Budget;

public class BudgetController extends Controller<Budget>{
	
	@FXML private TableColumn<Bill, String> nameCLM;
	@FXML private TableColumn<Bill, String> amountCLM;
	@FXML private TableColumn<Bill, String> allocatedCLM;
	@FXML private TableColumn<Bill, String> owedCLM;
	@FXML private TableView<Bill> billsTV;
	@FXML private TextField jointAccountTF;
	@FXML private TextField teganAccountTF;
	@FXML private TextField campbellAccountTF;
	@FXML private Text newJointTXT;
	@FXML private Text newCampbellTXT;
	@FXML private Text newTeganTXT;
	@FXML private Text recordTXT;
	
	public final Budget getBudget() {return model;}

	@FXML private void initialize() {  
		nameCLM.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		amountCLM.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asString());
		allocatedCLM.setCellValueFactory(cellData -> cellData.getValue().allocatedAmountProperty().asString());
		owedCLM.setCellValueFactory(cellData -> cellData.getValue().owedAmountProperty().asString());
		
		newJointTXT.textProperty().bind(getBudget().newJointProperty().asString());
		newCampbellTXT.textProperty().bind(getBudget().newCampbellProperty().asString());
		newTeganTXT.textProperty().bind(getBudget().newTeganProperty().asString());
		recordTXT.textProperty().bind(getBudget().recordProperty());
		
		billsTV.setRowFactory( tv -> {
		    TableRow<Bill> row = new TableRow<Bill>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		            Bill bill = row.getItem();
		            try {
						ViewLoader.showStage(bill, "/view/Bill.fxml", "Bill", new Stage());
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		    });
		    return row ;
		});
		
		Bindings.bindBidirectional(jointAccountTF.textProperty(), getBudget().jointAccountProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(teganAccountTF.textProperty(), getBudget().teganAccountProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(campbellAccountTF.textProperty(), getBudget().campbellAccountProperty(), new NumberStringConverter());
		
	}
	
	@FXML private void handleAddBill(ActionEvent event) throws Exception{
		Bill bill = new Bill();
		getBudget().getBills().add(bill);
		getBudget().sortBills();
		ViewLoader.showStage(bill, "/view/Bill.fxml", "Bill", new Stage());
	}
	
	@FXML private void handlePayBills(ActionEvent event) throws Exception{
		getBudget().setRecord("");
		ViewLoader.showStage(getBudget(), "/view/PayBills.fxml", "Pay Bills", new Stage());
	}
}
