package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class Budget{
	
	private IntegerProperty jointAccount = new SimpleIntegerProperty();
	private IntegerProperty newJoint = new SimpleIntegerProperty();
	private IntegerProperty campbellAccount = new SimpleIntegerProperty();
	private IntegerProperty newCampbell = new SimpleIntegerProperty();
	private IntegerProperty teganAccount = new SimpleIntegerProperty();
	private IntegerProperty newTegan = new SimpleIntegerProperty();
	private IntegerProperty total = new SimpleIntegerProperty();
	private StringProperty record = new SimpleStringProperty();
	private ObservableList<Bill> bills = FXCollections.observableArrayList();
	int totalBills;
	
	public Budget(){
		jointAccount.set(0);
		newJoint.set(0);
		campbellAccount.set(0);
		newCampbell.set(0);
		teganAccount.set(0);
		newTegan.set(0);
		total.set(0);
		totalBills = 0;
		record.set("This is where bill records \n will appear");
		
		total.bind(Bindings.add(jointAccountProperty(), Bindings.add(campbellAccountProperty(), teganAccountProperty())));
		total.addListener((observable, oldValue, newValue) -> {
		    allocateBills(newValue.intValue(), bills);
	    });
		bills.addListener(new ListChangeListener<Bill>() {
		    @Override
		    public void onChanged(
		        javafx.collections.ListChangeListener.Change<? extends Bill> c) {
		    	totalBills = 0;
		    	for(Bill bill : bills){
		    		totalBills += bill.getAmount();
		    		System.out.println("Total bills: " + totalBills);
		    	}
		    	
		    	
		    }
		}); 
	}
	
	private void allocateBills(int total, ObservableList<Bill> bills){
		int remaining = total;
		for(Bill bill : bills){
			if(remaining >= bill.getAmount() + bill.originalOwed + bill.originalAmount){
				bill.allocate(bill.getAmount() + bill.originalOwed + bill.originalAmount);
				remaining -= bill.getAmount() + bill.originalOwed + bill.originalAmount;
			}else{
				bill.allocate(remaining);
				remaining = 0;
			}
		}
		newJoint.set(total - remaining);
		if(remaining > 0){
			newTegan.set(remaining / 2);
			newCampbell.set(remaining / 2);
		}else{
			newTegan.set(0);
			newCampbell.set(0);
		}
	}
	
	public void initialise(){
		for(Bill bill : bills){
			bill.setBudget(this);
			bill.originalAmount = bill.getAllocatedAmount();
			bill.originalOwed = bill.getOwedAmount();
			bill.calcPriority();
		}
		sortBills();
	}
	

	
	public void sortBills(){
		Collections.sort(bills, billComparator_byPriority);
	}
	
	public int getMaxBill(){
		int max = 1;
		for(Bill bill : bills){
			if(bill.getAmount() > max){
				max = bill.getRecurrence() / 365 * bill.getAmount();
			}
		}
		System.out.println(max);
		return max;
	}
	
	public void removeLoops(){
		for(Bill bill : bills){
			bill.setBudget(null);
		}
	}
	
	Comparator<? super Bill> billComparator_byPriority = new Comparator<Bill>() {
        @Override
        public int compare(Bill o1, Bill o2) {
            return Double.compare(o2.getPriority(), o1.getPriority());
        }
    };
	
	//Getters and Setters
	public ObservableList<Bill> getBills() { return bills; }
	public void setBills(ObservableList<Bill> bills) { this.bills = bills; }
	
	public int getJointAccount() { return jointAccount.get(); }
	public void setJointAccount(int jointAccount) { this.jointAccount.set(jointAccount); }
	public IntegerProperty jointAccountProperty(){ return jointAccount; }
	
	public int getNewJoint() { return newJoint.get(); }
	public void setNewJoint(int newJoint) { this.newJoint.set(newJoint); }
	public IntegerProperty newJointProperty(){ return newJoint; }
	
	public int getCampbellAccount() { return campbellAccount.get(); }
	public void setCampbellAccount(int campbellAccount) { this.campbellAccount.set(campbellAccount); }
	public IntegerProperty campbellAccountProperty(){ return campbellAccount; }
	
	public int getNewCampbell() { return newCampbell.get(); }
	public void setNewCampbell(int newCampbell) { this.newCampbell.set(newCampbell); }
	public IntegerProperty newCampbellProperty(){ return newCampbell; }
	
	public int getTeganAccount() { return teganAccount.get(); }
	public void setTeganAccount(int teganAccount) { this.teganAccount.set(teganAccount); }
	public IntegerProperty teganAccountProperty(){ return teganAccount; }
	
	public int getNewTegan() { return newTegan.get(); }
	public void setNewTegan(int newTegan) { this.newTegan.set(newTegan); }
	public IntegerProperty newTeganProperty(){ return newTegan; }
	
	public int getTotal() { return total.get(); }
	public void setTotal(int total) { this.total.set(total); }
	public IntegerProperty totalProperty(){ return total; }
	
	public String getRecord() { return record.get(); }
	public void setRecord(String record) { this.record.set(record); }
	public StringProperty recordProperty() { return record; }
	
}
