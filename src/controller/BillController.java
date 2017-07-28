package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import fxTools.Controller;
import fxTools.ViewLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.Bill;
import model.Budget;

public class BillController extends Controller<Bill>{
	
	@FXML private TextField nameTF;
	@FXML private TextField amountTF;
	@FXML private DatePicker paymentDP;
	@FXML private TextField recurrenceTF;
	@FXML private TextArea descriptionTF;
	
	private ObjectProperty<LocalDate> lastPayment = new SimpleObjectProperty<LocalDate>();;
	

	
	public final Bill getBill() {return model;}
	
	@FXML private void initialize(){
		lastPayment = new SimpleObjectProperty<LocalDate>(getBill().createDate(getBill().getLastPayment()));
		nameTF.textProperty().bindBidirectional(getBill().nameProperty());
		Bindings.bindBidirectional(amountTF.textProperty(), getBill().amountProperty(), new NumberStringConverter());
		descriptionTF.textProperty().bindBidirectional(getBill().descriptionProperty());
		paymentDP.valueProperty().bindBidirectional(lastPaymentProperty());
		Bindings.bindBidirectional(recurrenceTF.textProperty(), getBill().recurrenceProperty(), new NumberStringConverter());
		
		paymentDP.valueProperty().addListener((ov, oldValue, newValue) -> {
            getBill().setLastPayment(newValue.toString());
        });

	}

	@FXML private void handleSave(ActionEvent event){
		stage.close();
	}
	
	public LocalDate getLastPayment() { return lastPayment.get(); }
	public void setLastPayment(LocalDate lastPayment) { this.lastPayment.set(lastPayment); }
	public ObjectProperty<LocalDate> lastPaymentProperty() { return lastPayment; }
}


