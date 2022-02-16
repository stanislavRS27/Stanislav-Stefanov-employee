package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static Stage primaryStage;
	public static FXMLLoader mainFXMLLoader;
	
	public static String title = "Employees";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Main.primaryStage = primaryStage;
			Main.mainFXMLLoader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
			Parent rootPrimary = Main.mainFXMLLoader.load();
			Scene scenePrimary = new Scene(rootPrimary);
			Main.primaryStage.setScene(scenePrimary);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setTitle(title);
			
			Main.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * Methods for calling different scenes
	 * */
	
	public void chooseNewFile() {
		primaryStage.close();
		
		try {
			Main.primaryStage = new Stage();
			Main.mainFXMLLoader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
			Parent rootPrimary = Main.mainFXMLLoader.load();
			Scene scenePrimary = new Scene(rootPrimary);
			Main.primaryStage.setScene(scenePrimary);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setTitle(title);
			
			Main.primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void seeChart() {
		try {
			Main.mainFXMLLoader = new FXMLLoader(getClass().getResource("PieChartFXML.fxml"));
			Parent rootPrimary = Main.mainFXMLLoader.load();
			Scene scenePrimary = new Scene(rootPrimary);
			Main.primaryStage.setScene(scenePrimary);
			Main.primaryStage.setResizable(false);
			Main.primaryStage.setTitle("PieChart");
			
			Main.primaryStage.show();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void backToStatisitcs() {
		try {
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
