package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DisplayDataController {

	@FXML
	private Label fromDateLabel;
	@FXML
	private Label toDateLabel;
	@FXML
	private Label totalShiftsLabel;
	@FXML
	private Label moneyEarnedLabel;
	@FXML
	private ListView<String> shiftOccurrenceListView;
	@FXML
	private ListView<String> dayOccurrenceListView;
	
	DataSingleton data = DataSingleton.getInstance();

	public void initialize() {
	    
		
		if (!data.getShifts().isEmpty()) {
			fromDateLabel.setText(data.getShifts().get(0).getStartDateTime().toLocalDate().toString());
			toDateLabel.setText(data.getShifts().get(data.getShifts().size() - 1).getStartDateTime().toLocalDate().toString());
			totalShiftsLabel.setText(Integer.toString(data.getShifts().size()));
			moneyEarnedLabel.setText(String.format("$%.2f", Shift.getTotalMoney(data.getShifts())));
			
			
			Map<String, Integer> sortedShiftsDictonary = new TreeMap<>(countShiftOccurrences(data.getShifts()));
			
			for (Map.Entry<String, Integer> entry : sortedShiftsDictonary.entrySet()) {
				shiftOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
			}
			
	        Map<String, Integer> sortedDayDictonary = new TreeMap<>(countDayOccurrences(data.getShifts()));
			
			for (Map.Entry<String, Integer> entry : sortedDayDictonary.entrySet()) {
				dayOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
			}
			
		}
		
	}
	
	
	public Map<String, Integer> countShiftOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> shiftDictonary = new HashMap<>();
		
		for (Shift shift : shifts) {
			
			String time = shift.getTimeAsString();
			
			shiftDictonary.put(time, shiftDictonary.getOrDefault(time, 0) + 1);
		}
		
		return shiftDictonary;
	}
	
    public Map<String, Integer> countDayOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> dayDictonary = new HashMap<>();
		
		for (Shift shift : shifts) {
			
			String dayOfWeek = shift.getStartDateTime().toLocalDate().getDayOfWeek().toString();
			
			dayDictonary.put(dayOfWeek, dayDictonary.getOrDefault(dayOfWeek, 0) + 1);
		}
		
		return dayDictonary;
    }
	
}
