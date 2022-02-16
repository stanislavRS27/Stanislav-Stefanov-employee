package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

public class PieChartSceneController implements Initializable{
	
	@FXML
	private Button backToStatisticsButton;
	
	@FXML
	private PieChart pieChartOverallTimeSpentTogether;
	
	Main main = new Main();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		/*
		 * Everything in this class is only responsible
		 * for the creation of the pieChart
		 * */
		
		String[] labels;
		int[] time;
		
		int counter = 0;
		
		for(int i = 0; i < StatisticsSceneController.overallTimeSpentTogether.length; ++i) {
			for(int j = 0; j < StatisticsSceneController.overallTimeSpentTogether[i].length; ++j) {
				if(StatisticsSceneController.overallTimeSpentTogether[i][j] == 0) {
					continue;
				}
				counter++;
			}
		}
		
		PieChart.Data[] pieChartDataArray = new PieChart.Data[counter];
		
		labels = new String[counter];
		time = new int[counter];
		
		counter = 0;
		
		for(int i = 0; i < StatisticsSceneController.overallTimeSpentTogether.length; ++i) {
			for(int j = 0; j < StatisticsSceneController.overallTimeSpentTogether[i].length; ++j) {
				if(StatisticsSceneController.overallTimeSpentTogether[i][j] == 0) {
					continue;
				}
				labels[counter] = "Employee #"
							+ StatisticsSceneController.checkedEmployeesRevesedOrder.get(i).toString()
							+ " & Employee #"
							+ StatisticsSceneController.checkedEmployeesRevesedOrder.get(j).toString();
				time[counter++] = StatisticsSceneController.overallTimeSpentTogether[i][j];
			}
		}
		
		for(int i = 0; i < pieChartDataArray.length; ++i) {
			pieChartDataArray[i] = new PieChart.Data(labels[i], time[i]);
		}
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(pieChartDataArray);
		pieChartOverallTimeSpentTogether.setData(pieChartData);
		pieChartOverallTimeSpentTogether.setTitle("Teamwork on common projects (Days)");
		
		pieChartData.forEach(data ->
        data.nameProperty().bind(
                Bindings.concat(
                        data.getName() + " (" + data.pieValueProperty().intValue() + " Days)"
                )
        )
);
		
	}
	
	public void backToStatisitcs(ActionEvent e) {
		main.backToStatisitcs();
	}

}
