package model;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;

import javax.xml.bind.annotation.XmlTransient;

import budget.Main;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Bill{
	
	//Properties
	private StringProperty name = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private IntegerProperty amount = new SimpleIntegerProperty();
	private IntegerProperty payable = new SimpleIntegerProperty();
	private IntegerProperty allocatedAmount = new SimpleIntegerProperty();
	private IntegerProperty owedAmount = new SimpleIntegerProperty();
	private DoubleProperty priority = new SimpleDoubleProperty();
	private IntegerProperty recurrence = new SimpleIntegerProperty();
	private StringProperty payments = new SimpleStringProperty();
	private StringProperty lastPayment = new SimpleStringProperty();
	int originalAmount, originalOwed;
	private Budget budget;
	
	
	public Bill(){
		name.set("");
		amount.set(0);
		owedAmount.set(0);
		allocatedAmount.set(0);
		description.set("");
		payments.set("");
		originalAmount = 0;
		lastPayment.set(getDate());
		recurrence.set(365);
		priority.set(0);
	}
	
	
	public void calcPriority(){
		double daysUntil, scaledOwed, scaledAmount;
		
		LocalDate nextPayment = createDate(lastPayment.get()).plusDays(recurrence.get());
		daysUntil = (int) LocalDate.now().until(nextPayment, ChronoUnit.DAYS);
		
		if(daysUntil < 0){
			daysUntil = 0;
		}else{
			daysUntil = 100 - daysUntil / 365 * 100;
		}
		
		if(owedAmount.get() + amount.get() == 0){
			scaledOwed = 0;
		}else{
			scaledOwed = owedAmount.get() / (owedAmount.get() + amount.get()) * 10;
		}
		
		if(getBudget().getMaxBill() == 0){
			scaledAmount = 0;
		}else{
			scaledAmount = amount.get() / getBudget().getMaxBill() * 10;
		}
		
		priority.set( daysUntil + scaledAmount + scaledOwed );
		
		System.out.println("Priority: " + priority.get());
	}
	
	public LocalDate createDate(String dateString){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateString, formatter);
		return date;
	}
	
	private String getDate(){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}
	
	public void allocate(int allocation){
		allocatedAmount.set(allocation);
		if(allocation > amount.get() + originalAmount){
			owedAmount.set(originalOwed - (allocation - amount.get() - originalAmount));
		}else if(allocation < amount.get() + originalAmount){
			owedAmount.set(originalOwed + (amount.get() + originalAmount - allocation));
		}else if(allocation == amount.get() + originalAmount){
			owedAmount.set(originalOwed);
		}
		
	}
	
	public void pay(){
		if(payable.get() <= allocatedAmount.get()){
			allocatedAmount.set(allocatedAmount.get() - payable.get());
			getBudget().setRecord(getBudget().getRecord() + String.format("$%-6s paid to    %s %n", payable.get(), name.get()));
			System.out.print(getBudget().getRecord());
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("Can't Pay");
			alert.setContentText("There is not enough money allocated to pay this.\nChange the value and try again.");
	
			alert.showAndWait();
		}
	}
	
	//Getters and Setters
	public int getAmount() { return amount.get(); }
	public void setAmount(int amount) { this.amount.set(amount); }
	public IntegerProperty amountProperty(){ return amount; }
	
	public int getAllocatedAmount() { return allocatedAmount.get(); }
	public void setAllocatedAmount(int allocatedAmount) { this.allocatedAmount.set(allocatedAmount); }
	public IntegerProperty allocatedAmountProperty(){ return allocatedAmount; }

	public int getOwedAmount() { return owedAmount.get(); }
	public void setOwedAmount(int owedAmount) { this.owedAmount.set(owedAmount); }
	public IntegerProperty owedAmountProperty(){ return owedAmount; }
	
	public String getName() { return name.get(); }
	public void setName(String name) { this.name.set(name); }
	public StringProperty nameProperty(){ return name; }
	
	public String getDescription() { return description.get(); }
	public void setDescription(String description) { this.description.set(description); }
	public StringProperty descriptionProperty(){ return description; }
	
	public double getPriority() { return priority.get(); }
	public void setPriority(double priority) { this.priority.set(priority); }
	public ReadOnlyDoubleProperty priorityProperty(){ return priority; }
	
	public String getPayments() { return payments.get(); }
	public void setPayments(String payments) { this.payments.set(payments); }
	public ReadOnlyStringProperty paymentsProperty() { return payments; }
	
	public int getPayable() { return payable.get(); }
	public void setPayable(int payable) { this.payable.set(payable); }
	public IntegerProperty payableProperty(){ return payable; }
	
	public int getRecurrence() { return recurrence.get(); }
	public void setRecurrence(int recurrence) { this.recurrence.set(recurrence); }
	public IntegerProperty recurrenceProperty(){ return recurrence; }
	
	public String getLastPayment() { return lastPayment.get(); }
	public void setLastPayment(String lastPayment) { this.lastPayment.set(lastPayment); }
	public ReadOnlyStringProperty lastPaymentProperty() { return lastPayment; }

	public Budget getBudget() { return budget; }
	public void setBudget(Budget budget) { this.budget = budget; }
}
