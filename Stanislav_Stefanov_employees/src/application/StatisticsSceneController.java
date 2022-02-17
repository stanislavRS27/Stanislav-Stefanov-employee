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
		
		int dataSize = MainSceneController.empID.size();
		
		checkedEmployees.clear();
		checkedEmployeesRevesedOrder.clear();
		
		//check how many unique employees and projects are present
		//{
		HashMap<Integer, Boolean> employeePresent = new HashMap<>();
		int employeeCount = 0;
		
		HashMap<Integer, Boolean> projectPresent = new HashMap<>();
		int projectCount = 0;
		
		for(int i = 0; i < dataSize; ++i) {		
			if(!employeePresent.containsKey(MainSceneController.empID.get(i))) {
				employeePresent.put(MainSceneController.empID.get(i), true);
				employeeCount++;
			}
			
			if(!projectPresent.containsKey(MainSceneController.projID.get(i))) {
				projectPresent.put(MainSceneController.projID.get(i), true);
				projectCount++;
			}
		}
		//}
		
		//initialize the matrix with invalid values
		//the matrix is of the form
		//Columns: EmployeeID 1; Rows: EmployeeID 2; Depth of the array in the corresponding column & row: Common projectID
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
		//this HashMap helps to find indices of the Employees based on their ID(checkedEmployees) and vice versa(checkedEmployeesRevesedOrder)
		for(int i = 0, j = 0; i < dataSize; ++i) {
			if(!checkedEmployees.containsKey(MainSceneController.empID.get(i))) {
				checkedEmployees.put(MainSceneController.empID.get(i), j);
				checkedEmployeesRevesedOrder.put(j++, MainSceneController.empID.get(i));
			}
		}
		
		//populate the relation matrix
		for(int i = 0; i < dataSize; ++i) {
			for(int j = 0, projIndex = 0; j < dataSize; ++j) {
				
				int employeeIndex = checkedEmployees.get(MainSceneController.empID.get(i));
				
				if((MainSceneController.projID.get(i).equals(MainSceneController.projID.get(j))) && !(MainSceneController.empID.get(i).equals(MainSceneController.empID.get(j)))) {
					
					//the program enters in the while loop if an employee, that already has a project worked on with another employee,
					//has another, different project and an empty index is needed to store the new project 
					while(empProj[employeeIndex][checkedEmployees.get(MainSceneController.empID.get(j))][projIndex] != -1) {
						projIndex++;
					}
					
					//set the value to the projectID
					empProj[employeeIndex][checkedEmployees.get(MainSceneController.empID.get(j))][projIndex] = MainSceneController.projID.get(i);
				}

			}
		}
		
		//needed only for the PieChart
		overallTimeSpentTogether = new int[employeeCount][employeeCount];
		
		//here the Dates are converted to millis, then calculated to achieve the time worked together back in days
		for(int i = 0; i < empProj.length; ++i) {
			for(int j = i; j < empProj[i].length; ++j) {
				for(int k = 0; k < empProj[i][j].length; ++k) {
					//if they have a project they have worked together upon
					if(empProj[i][j][k] != -1) {
						
						boolean flag1 = false;
						boolean flag2 = false;
						
						String emp1DateFrom = "";
						String emp1DateTo = "";
						String emp2DateFrom = "";
						String emp2DateTo = "";
						
						//retrieve the needed dates from the original data ArrayLists
						for(int l = 0; l < dataSize; ++l) {
							
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
									//the only acceptable date format
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									
									//convert the date to millis
									//{
								    Date date = sdf.parse(emp1DateFrom);
								    long millisEmp1From = date.getTime();
								    
								    date = sdf.parse(emp1DateTo);
								    long millisEmp1To = date.getTime();
								    
								    date = sdf.parse(emp2DateFrom);
								    long millisEmp2From = date.getTime();
								    
								    date = sdf.parse(emp2DateTo);
								    long millisEmp2To = date.getTime();
								    //}
								    
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
								    
								    //converting the calculated time worked together back to days (from millis)
								    int days = (int)((float)(millisTo - millisFrom) / 86400000);
								    
								    //add everything to the ListViews in the Statistics Scene
								    listViewEmp1Id.getItems().add(checkedEmployeesRevesedOrder.get(i).toString());
								    listViewEmp2Id.getItems().add(checkedEmployeesRevesedOrder.get(j).toString());
								    listViewProjId.getItems().add(Integer.toString(empProj[i][j][k]));
								    listViewTimeSpentTogether.getItems().add(Integer.toString(days));
								    
								    //calculates the OVERALL time worked together between 2 employees
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
