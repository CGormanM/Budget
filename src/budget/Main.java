package budget;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import fxTools.ViewLoader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Bill;
import model.BillListWrapper;
import model.Budget;


public class Main extends Application {
	
	Budget budget = new Budget();
	File saveFile, logFile;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		prepSaves();
		loadBillsFromFile(saveFile);
		budget.initialise();
		ViewLoader.showStage(budget, "/view/Budget.fxml", "Budget", primaryStage);
		
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	      public void handle(WindowEvent we) {
	    	  System.out.println("Closing");
	    	  saveLogs(logFile.toString());
	    	  budget.removeLoops();
	    	  saveBillsToFile(saveFile);
	      }
        });
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void loadBillsFromFile(File file){
		try{
			JAXBContext context = JAXBContext
					.newInstance(BillListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			
			BillListWrapper wrapper = (BillListWrapper) um.unmarshal(file);
			
			budget.getBills().clear();
			budget.getBills().addAll(wrapper.getBills());
			
			//TODO setPersonFilePath(file);
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	public void saveBillsToFile(File file){
		try{	
			JAXBContext context = JAXBContext
					.newInstance(BillListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			BillListWrapper wrapper = new BillListWrapper();
			wrapper.setBills(budget.getBills());
			
			m.marshal(wrapper, file);
			
			//TODO setPersonFilePath(file);
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
	
	private void saveLogs(String filePath){
		try{
		    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		    writer.print(budget.getRecord());
		    writer.close();
		} catch (IOException e) {
		   e.printStackTrace();
		}
	}
	
	private void prepSaves(){
		LocalDate date = LocalDate.now();
		String currentDate = date.toString();
		String currentDir = System.getProperty("user.dir") + File.separator;
		
        File logFolder = new File(currentDir + "logs");
        if(!logFolder.exists()){
        	logFolder.mkdir();
        }
        
        saveFile = new File(currentDir + "save.xml");
        if(!saveFile.exists()){
        	try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        logFile = new File(currentDir + "logs" + File.separator + currentDate + ".txt");
        if(!logFile.exists()){
        	try {
        		logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

}
