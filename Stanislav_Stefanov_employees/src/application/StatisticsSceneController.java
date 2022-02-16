package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class StatisticsSceneController implements Initializable{
	
	@FXML
	private Button selectNewFileButton;
	
	@FXML
	private Button pieChartButton;

	@FXML
	private Label labelEmp1;
	
	@FXML
	private Label labelEmp2;
	
	@FXML
	private Label labelProj;
	
	@FXML
	private Label labelTimeSpentTogether;
	
	@FXML
	private ListView<String> listViewEmp1Id;
	
	@FXML
	private ListView<String> listViewEmp2Id;
	
	@FXML
	private ListView<String> listViewProjId;
	
	@FXML
	private ListView<String> listViewTimeSpentTogether;
	
	//for pieChart purposes only
	public static int[][] overallTimeSpentTogether;
	
	//#1 Integer => EmpID; #2 Integer => empProj index
	public static HashMap<Integer, Integer> checkedEmployees = new HashMap<>();
	//#1 Integer => empProj index; #2 Integer => EmpID
	public static HashMap<Integer, Integer> checkedEmployeesRevesedOrder = new HashMap<>();
	
	Main main = new Main();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		checkedEmployees.clear();
		checkedEmployeesRevesedOrder.clear();
		
		//check how many unique employees are present
		//{
		HashMap<Integer, Boolean> employeePresent = new HashMap<>();
		int employeeCount = 0;
		
		for(int i = 0; i < MainSceneController.empID.size(); ++i) {
			
			if(employeePresent.containsKey(MainSceneController.empID.get(i))) {
				continue;
			}
			
			employeePresent.put(MainSceneController.empID.get(i), true);
			employeeCount++;
		}
		//}
		
		//check how many unique projects are present
		//{
		HashMap<Integer, Boolean> projectPresent = new HashMap<>();
		int projectCount = 0;
		
		for(int i = 0; i < MainSceneController.projID.size(); ++i) {
			
			if(projectPresent.containsKey(MainSceneController.projID.get(i))) {
				continue;
			}
			
			projectPresent.put(MainSceneController.projID.get(i), true);
			projectCount++;
		}
		//}
		
		//initialize the matrix with invalid values
		//{
		int[][][] empProj = new int[employeeCount][employeeCount][projectCount];
		
		for(int i = 0; i < employeeCount; ++i) {
			for(int j = 0; j < employeeCount; ++j) {
				for(int k = 0; k < projectCount; ++k) {
					empProj[i][j][k] = -1;
				}
			}
		}
		//}
		
		//populate the HashMap
		for(int i = 0, j = 0; i < MainSceneController.empID.size(); ++i) {
			if(!checkedEmployees.containsKey(MainSceneController.empID.get(i))) {
				checkedEmployees.put(MainSceneController.empID.get(i), j);
				checkedEmployeesRevesedOrder.put(j++, MainSceneController.empID.get(i));
			}
		}
		
		//populate the relation matrix
		for(int i = 0; i < MainSceneController.empID.size(); ++i) {
			for(int j = 0, projIndex = 0; j < MainSceneController.empID.size(); ++j) {
				
				int employeeIndex = checkedEmployees.get(MainSceneController.empID.get(i));
				
				if((MainSceneController.projID.get(i).equals(MainSceneController.projID.get(j))) && !(MainSceneController.empID.get(i).equals(MainSceneController.empID.get(j)))) {
					while(empProj[employeeIndex][checkedEmployees.get(MainSceneController.empID.get(j))][projIndex] != -1) {
						projIndex++;
					}
					empProj[employeeIndex][checkedEmployees.get(MainSceneController.empID.get(j))][projIndex] = MainSceneController.projID.get(i);
				}

			}
		}
		
		overallTimeSpentTogether = new int[employeeCount][employeeCount];
		
		//here the date is converted to millis, then calculated to achieve the time worked together back in days
		for(int i = 0; i < empProj.length; ++i) {
			for(int j = i; j < empProj[i].length; ++j) {
				for(int k = 0; k < empProj[i][j].length; ++k) {
					if(empProj[i][j][k] != -1) {
						
						boolean flag1 = false;
						boolean flag2 = false;
						
						String emp1DateFrom = "";
						String emp1DateTo = "";
						String emp2DateFrom = "";
						String emp2DateTo = "";
						
						for(int l = 0; l < MainSceneController.empID.size(); ++l) {
							
							//find the corresponding dates for each employee that has worked in team with somebody
							if(MainSceneController.empID.get(l).equals(checkedEmployeesRevesedOrder.get(i)) && MainSceneController.projID.get(l).equals(empProj[i][j][k])){
								emp1DateFrom = MainSceneController.dateFrom.get(l);
								if(MainSceneController.dateTo.get(l).equals("null")) {
									DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
									LocalDateTime now = LocalDateTime.now();  
									emp1DateTo = dtf.format(now).toString();
								} else {
									emp1DateTo = MainSceneController.dateTo.get(l);
								}
								flag1 = true;
							}
							//find the dates of the corresponding buddy to the first employee in this team
							if(MainSceneController.empID.get(l).equals(checkedEmployeesRevesedOrder.get(j)) && MainSceneController.projID.get(l).equals(empProj[i][j][k])){
								emp2DateFrom = MainSceneController.dateFrom.get(l);
								if(MainSceneController.dateTo.get(l).equals("null")) {
									DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
									LocalDateTime now = LocalDateTime.now();  
									emp2DateTo = dtf.format(now).toString();
								} else {
									emp2DateTo = MainSceneController.dateTo.get(l);
								}
								flag2 = true;
							}
							
							//when both are found and their corresponding dates retrieved
							if(flag1 && flag2) {
								try {
									SimpleDateFormat sdfEmp1From = new SimpleDateFormat("yyyy-MM-dd");
								    Date dateEmp1From = sdfEmp1From.parse(emp1DateFrom);
								    long millisEmp1From = dateEmp1From.getTime();
								    
									SimpleDateFormat sdfEmp1To = new SimpleDateFormat("yyyy-MM-dd");
								    Date dateEmp1To = sdfEmp1To.parse(emp1DateTo);
								    long millisEmp1To = dateEmp1To.getTime();
								    
								    SimpleDateFormat sdfEmp2From = new SimpleDateFormat("yyyy-MM-dd");
								    Date dateEmp2From = sdfEmp2From.parse(emp2DateFrom);
								    long millisEmp2From = dateEmp2From.getTime();
								    
								    SimpleDateFormat sdfEmp2To = new SimpleDateFormat("yyyy-MM-dd");
								    Date dateEmp2To = sdfEmp2To.parse(emp2DateTo);
								    long millisEmp2To = dateEmp2To.getTime();
								    
								    long millisFrom;
								    long millisTo;
								    
								    if(millisEmp1To < millisEmp2From || millisEmp2To < millisEmp1From) {
								    	break;
								    }
								    
								    if(millisEmp1From > millisEmp2From) {
								    	millisFrom = millisEmp1From;
								    } else {
								    	millisFrom = millisEmp2From;
								    }
								    
								    if(millisEmp1To > millisEmp2To) {
								    	millisTo = millisEmp2To;
								    } else {
								    	millisTo = millisEmp1To;
								    }
								    
								    int days = (int)((float)(millisTo - millisFrom) / 86400000);
								    
								    listViewEmp1Id.getItems().add(checkedEmployeesRevesedOrder.get(i).toString());
								    listViewEmp2Id.getItems().add(checkedEmployeesRevesedOrder.get(j).toString());
								    listViewProjId.getItems().add(Integer.toString(empProj[i][j][k]));
								    listViewTimeSpentTogether.getItems().add(Integer.toString(days));
								    
								    overallTimeSpentTogether[i][j] += days;
								}
								catch(Exception ex) {
									ex.printStackTrace();
								}
								
								break;
							}
						}
					} else {
						break;
					}
				}
			}
		}
		
	}
	
	public void selectNewFile(ActionEvent e) {
		main.chooseNewFile();
	}
	
	public void seeChart(ActionEvent e) {
		main.seeChart();
	}

}
