package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainSceneController implements Initializable{
	
	@FXML
	private Button fileChooserButton;
	
	public static ArrayList<Integer> empID = new ArrayList<>();
	public static ArrayList<Integer> projID = new ArrayList<>();
	public static ArrayList<String> dateFrom = new ArrayList<>();
	public static ArrayList<String> dateTo = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		empID.clear();
		projID.clear();
		dateFrom.clear();
		dateTo.clear();
	}
	
	public void chooseCSVFile(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(Main.primaryStage);
		if (selectedFile != null) {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));
				String line;
				
				while ((line = bufferedReader.readLine()) != null) {
					String[] row = line.split(",");
					empID.add(Integer.parseInt(row[0]));
					projID.add(Integer.parseInt(row[1]));
					dateFrom.add(row[2]);
					dateTo.add(row[3]);
				}

				bufferedReader.close();
			}
			catch(NumberFormatException nfe) {
				//pop-up notification for the exception
				System.out.println("Format of the .csv file is not suppored. Needs to be \"EmpID, ProjID, DateFrom, DateTo\"");
				return;
			}
			catch(Exception ex) {
				//pop-up notification for the exception
				System.out.println("Perhaps you didn't choose a .csv file?");
				return;
			}	 
		}
		
		try {
			Main.primaryStage.close();
			
			Main.mainFXMLLoader = new FXMLLoader(getClass().getResource("StatisticsFXML.fxml"));
			Parent rootPrimary = Main.mainFXMLLoader.load();
			Scene scenePrimary = new Scene(rootPrimary);
			Main.primaryStage.setScene(scenePrimary);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setTitle("Statistics");
			
			Main.primaryStage.show();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
