package controller;

import java.io.IOException;

import fxTools.Controller;
import fxTools.ViewLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Bill;
import model.Budget;

public class PayBillsController extends Controller<Budget>{
	
	@FXML private TableView<Bill> billsTV;
	@FXML private TableColumn<Bill, String> nameCLM;
	@FXML private TableColumn<Bill, String> allocatedCLM;
	@FXML private TableColumn<Bill, String> payableCLM;
	@FXML private TableColumn<Bill, String> includeCLM;
	
	public final Budget getBudget() {return model;}
	
	@FXML private void handleFinish(ActionEvent event){
		stage.close();
	}
	
	@FXML private void initialize() {  
		billsTV.setEditable(true);
		nameCLM.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		allocatedCLM.setCellValueFactory(cellData -> cellData.getValue().allocatedAmountProperty().asString());
		
		payableCLM.setCellFactory(TextFieldTableCell.<Bill>forTableColumn());
		payableCLM.setCellValueFactory(cellData -> cellData.getValue().payableProperty().asString());
		payableCLM.setOnEditCommit(
            new EventHandler<CellEditEvent<Bill, String>>() {
                @Override
                public void handle(CellEditEvent<Bill, String> t) {
                    ((Bill) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setPayable(Integer.parseInt(t.getNewValue()));
                }
            }
        );
		
		billsTV.setRowFactory( tv -> {
		    TableRow<Bill> row = new TableRow<Bill>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 3 && (! row.isEmpty()) ) {
		            Bill bill = row.getItem();
					bill.pay();
		        }
		    });
		    return row ;
		});
	}
}
