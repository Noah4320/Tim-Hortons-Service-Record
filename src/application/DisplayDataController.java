package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
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
	
	public static Map<String, Integer> shiftsDictonary;
	
	public static boolean sortedByValue = false;

	public void initialize() {
	    
		
		if (!data.getShifts().isEmpty()) {
			fromDateLabel.setText(data.getShifts().get(0).getStartDateTime().toLocalDate().toString());
			toDateLabel.setText(data.getShifts().get(data.getShifts().size() - 1).getStartDateTime().toLocalDate().toString());
			totalShiftsLabel.setText(Integer.toString(data.getShifts().size()));
			moneyEarnedLabel.setText(String.format("$%.2f", Shift.getTotalMoney(data.getShifts())));
			
			populateShiftOccurrenceListView();
			
	        Map<String, Integer> sortedDayDictonary = new LinkedHashMap<>(countDayOccurrences(data.getShifts()));
	        
			for (Map.Entry<String, Integer> entry : sortedDayDictonary.entrySet()) {
				dayOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
			}
			
		}
		
	}
	
	@FXML
	public void btnSortToggleShiftsClicked(ActionEvent event) throws IOException {
    	
    	if (!sortedByValue) {
    		List<Map.Entry<String, Integer>> sortedList = shiftsDictonary.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
        	
        	shiftOccurrenceListView.getItems().clear();
        	
        	for (Map.Entry<String, Integer> entry : sortedList) {
    			shiftOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
    		}
        	sortedByValue = true;
    	} 
    	else {
    		populateShiftOccurrenceListView();
    		sortedByValue = false;
    	}
    	
    }
	
	
	public Map<String, Integer> countShiftOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> shiftDictonary = new LinkedHashMap<>();
		
		Comparator<Shift> compareTime = (c1, c2) -> c1.getStartDateTime().toLocalTime().compareTo(c2.getStartDateTime().toLocalTime());
		
		shifts.sort(compareTime);
		
		for (Shift shift : shifts) {
			
			String time = shift.getTimeAsString();
			
			shiftDictonary.put(time, shiftDictonary.getOrDefault(time, 0) + 1);
		}
		
		return shiftDictonary;
	}
	
    public Map<String, Integer> countDayOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> dayDictonary = new LinkedHashMap<>();
		
		for (Shift shift : shifts) {
			
			String dayOfWeek = shift.getStartDateTime().toLocalDate().getDayOfWeek().toString();
			
			dayDictonary.put(dayOfWeek, dayDictonary.getOrDefault(dayOfWeek, 0) + 1);
		}
		
		return dayDictonary;
    }
    
    
    public void populateShiftOccurrenceListView() {
    	
    	if(shiftOccurrenceListView.getItems().size() != 0) {
    		shiftOccurrenceListView.getItems().clear();
    	}
    	
    	shiftsDictonary = new LinkedHashMap<>(countShiftOccurrences(data.getShifts()));
		
		for (Map.Entry<String, Integer> entry : shiftsDictonary.entrySet()) {
			shiftOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
		}
    }
	
}
