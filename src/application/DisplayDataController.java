package application;

import java.io.IOException;
import java.time.DayOfWeek;
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
	
	public static Map<String, Integer> shiftsDictionary;
	public static Map<DayOfWeek, Integer> daysDictionary;
	
	public static boolean sortedShiftsByValue = false;
	public static boolean sortedDaysByValue = false;
	
	public static DayOfWeek dayOfWeek;

	public void initialize() {
	    
		
		if (!data.getShifts().isEmpty()) {
			fromDateLabel.setText(data.getShifts().get(0).getStartDateTime().toLocalDate().toString());
			toDateLabel.setText(data.getShifts().get(data.getShifts().size() - 1).getStartDateTime().toLocalDate().toString());
			totalShiftsLabel.setText(Integer.toString(data.getShifts().size()));
			moneyEarnedLabel.setText(String.format("$%.2f", Shift.getTotalMoney(data.getShifts())));
			
			populateShiftOccurrenceListView();
			populateDaysOccurrenceListView();

			
			dayOccurrenceListView.setOnMouseClicked(event -> {
				
				String day = dayOccurrenceListView.getSelectionModel().getSelectedItem();
				day = day.split(" ")[0];
				
				dayOfWeek = DayOfWeek.valueOf(day);
				populateShiftOccurrenceListView();
				
			});
			
		}
		
	}
	
	@FXML
	public void btnSortToggleShiftsClicked(ActionEvent event) throws IOException {
    	
    	if (!sortedShiftsByValue) {
    		List<Map.Entry<String, Integer>> sortedList = shiftsDictionary.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
        	
        	shiftOccurrenceListView.getItems().clear();
        	
        	for (Map.Entry<String, Integer> entry : sortedList) {
    			shiftOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
    		}
        	sortedShiftsByValue = true;
    	} 
    	else {
    		populateShiftOccurrenceListView();
    		sortedShiftsByValue = false;
    	}
    	
    }
	
	@FXML
	public void btnSortToggleDaysClicked(ActionEvent event) throws IOException {
    	
    	if (!sortedDaysByValue) {
    		List<Map.Entry<DayOfWeek, Integer>> sortedList = daysDictionary.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());
        	
    		dayOccurrenceListView.getItems().clear();
        	
        	for (Map.Entry<DayOfWeek, Integer> entry : sortedList) {
        		dayOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
    		}
        	sortedDaysByValue = true;
    	} 
    	else {
    		populateDaysOccurrenceListView();
    		sortedDaysByValue = false;
    	}
    	
    }
	
	@FXML
	public void btnResetShiftListViewClicked(ActionEvent event) throws IOException {
		
		dayOfWeek = null;
		
		populateShiftOccurrenceListView();
		
	}
	
	
	public Map<String, Integer> countShiftOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> shiftDictonary = new LinkedHashMap<>();
		
		Comparator<Shift> compareTime = (c1, c2) -> c1.getStartDateTime().toLocalTime().compareTo(c2.getStartDateTime().toLocalTime());
		
		shifts.sort(compareTime);
		
		for (Shift shift : shifts) {
			
			if (dayOfWeek != null) {
				
				if (dayOfWeek.equals(shift.getStartDateTime().toLocalDate().getDayOfWeek())) {
					
					String time = shift.getTimeAsString();
					
					shiftDictonary.put(time, shiftDictonary.getOrDefault(time, 0) + 1);
					
				}
			}		
			else {
				
				String time = shift.getTimeAsString();
				
				shiftDictonary.put(time, shiftDictonary.getOrDefault(time, 0) + 1);
			}
		}
		
		return shiftDictonary;
	}
	
    public Map<DayOfWeek, Integer> countDayOccurrences (List<Shift> shifts) {
		
		Map<DayOfWeek, Integer> dayDictonary = new LinkedHashMap<>();
		
        Comparator<Shift> compareTime = (c1, c2) -> c1.getStartDateTime().toLocalDate().getDayOfWeek().compareTo(c2.getStartDateTime().toLocalDate().getDayOfWeek());
		
		shifts.sort(compareTime);
		
		for (Shift shift : shifts) {
			
			DayOfWeek dayOfWeek = shift.getStartDateTime().toLocalDate().getDayOfWeek();
			
			dayDictonary.put(dayOfWeek, dayDictonary.getOrDefault(dayOfWeek, 0) + 1);
		}
		
		return dayDictonary;
    }
    
    
    public void populateShiftOccurrenceListView() {
    	
    	if(shiftOccurrenceListView.getItems().size() != 0) {
    		shiftOccurrenceListView.getItems().clear();
    	}
    	
    	shiftsDictionary = new LinkedHashMap<>(countShiftOccurrences(data.getShifts()));
		
		for (Map.Entry<String, Integer> entry : shiftsDictionary.entrySet()) {
			shiftOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
		}
    }
    
    public void populateDaysOccurrenceListView() {
    	
    	if(dayOccurrenceListView.getItems().size() != 0) {
    		dayOccurrenceListView.getItems().clear();
    	}
		
    	daysDictionary = new LinkedHashMap<>(countDayOccurrences(data.getShifts()));
        
		for (Map.Entry<DayOfWeek, Integer> entry : daysDictionary.entrySet()) {
			dayOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
		}
    }
	
}
