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

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.ButtonBar;
import com.calendarfx.view.CalendarView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
	@FXML
	private ListView<String> storeOccurrenceListView;
	@FXML
	private ListView<String> bookerOccurrenceListView;
	
	DataSingleton data = DataSingleton.getInstance();
	
	public static Map<String, Integer> shiftsDictionary;
	public static Map<DayOfWeek, Integer> daysDictionary;
	
	public static boolean sortedShiftsByValue = false;
	public static boolean sortedDaysByValue = false;
	
	public static DayOfWeek dayOfWeekFilter;

	public void initialize() {
	    
		
		if (!data.getShifts().isEmpty()) {
			fromDateLabel.setText(data.getShifts().get(0).getStartDateTime().toLocalDate().toString());
			toDateLabel.setText(data.getShifts().get(data.getShifts().size() - 1).getStartDateTime().toLocalDate().toString());
			totalShiftsLabel.setText(Integer.toString(data.getShifts().size()));
			moneyEarnedLabel.setText(String.format("$%.2f", Shift.getTotalMoney(data.getShifts())));
			
			populateShiftOccurrenceListView();
			populateDaysOccurrenceListView();
			populateStoreOccurrenceListView();
			populateBookerOccurrenceListView();

			
			dayOccurrenceListView.setOnMouseClicked(event -> {
				
				String day = dayOccurrenceListView.getSelectionModel().getSelectedItem();
				day = day.split(" ")[0];
				
				dayOfWeekFilter = DayOfWeek.valueOf(day);
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
		
		dayOfWeekFilter = null;
		
		populateShiftOccurrenceListView();
		
	}
	
	@FXML
    public void btnCalendarClicked(ActionEvent event) throws IOException {
	
		//FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayDataScene.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		

		CalendarView calendarView = new CalendarView();

		Calendar threeHrShifts = new Calendar("3-hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() < 4).toList().size()) + ")");
		Calendar fourHrShifts = new Calendar("4hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 4 && i.getDuration().toHours() < 5).toList().size()) + ")");
		Calendar fiveHrShifts = new Calendar("5hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 5 && i.getDuration().toHours() < 6).toList().size()) + ")");
		Calendar sixHrShifts = new Calendar("6hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 6 && i.getDuration().toHours() < 7).toList().size()) + ")");
		Calendar sevenHrShift = new Calendar("7hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 7 && i.getDuration().toHours() < 8).toList().size()) + ")");
		Calendar eightHrShifts = new Calendar("8hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 8 && i.getDuration().toHours() < 9).toList().size()) + ")");
		Calendar nineHrShifts = new Calendar("8+hr Shifts (" + String.valueOf(data.getShifts().stream().filter(i -> i.getDuration().toHours() >= 9).toList().size()) + ")");
		
		threeHrShifts.setStyle(Style.STYLE1);
		fourHrShifts.setStyle(Style.STYLE2);
		fiveHrShifts.setStyle(Style.STYLE3);
		sixHrShifts.setStyle(Style.STYLE6);
		sevenHrShift.setStyle(Style.STYLE5);
		eightHrShifts.setStyle(Style.STYLE4);
		nineHrShifts.setStyle(Style.STYLE7);
		
		for (int i=0; i < data.getShifts().size(); i++) {
			
			Shift shift = data.getShifts().get(i);
			
			Entry<String> shiftEntry = new Entry<>("Shift #" + String.valueOf(i + 1));
			shiftEntry.setId(String.valueOf(i));
			shiftEntry.setTitle("Shift #" + String.valueOf(i + 1));
			//shiftEntry.setInterval(shift.getStartDateTime().toLocalDate());
			shiftEntry.setInterval(shift.getStartDateTime(), shift.getFinishDateTime());
			
			if (shift.getDuration().toHours() < 4) {
				threeHrShifts.addEntry(shiftEntry);
			}
			
			else if (shift.getDuration().toHours() >= 4 && shift.getDuration().toHours() < 5) {
				fourHrShifts.addEntry(shiftEntry);
			}
			
			else if (shift.getDuration().toHours() >= 5 && shift.getDuration().toHours() < 6) {
				fiveHrShifts.addEntry(shiftEntry);
			}
			
			else if (shift.getDuration().toHours() >= 6 && shift.getDuration().toHours() < 7) {
				sixHrShifts.addEntry(shiftEntry);
			}
			
			else if (shift.getDuration().toHours() >= 7 && shift.getDuration().toHours() < 8) {
				sevenHrShift.addEntry(shiftEntry);
			}
			
			else if (shift.getDuration().toHours() >= 8 && shift.getDuration().toHours() < 9) {
				eightHrShifts.addEntry(shiftEntry);
			}
			
			else {
				nineHrShifts.addEntry(shiftEntry);
			}
			
		}
		
		
		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		myCalendarSource.getCalendars().addAll(threeHrShifts, fourHrShifts, fiveHrShifts, sixHrShifts, sevenHrShift, eightHrShifts, nineHrShifts);
		
		calendarView.getCalendarSources().addAll(myCalendarSource);
		calendarView.setRequestedTime(data.getShifts().get(0).getStartDateTime().toLocalTime());
		
		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						calendarView.setToday(data.getShifts().get(0).getStartDateTime().toLocalDate());
						calendarView.setTime(data.getShifts().get(0).getStartDateTime().toLocalTime());
					});
					
					try {
						sleep (10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();
		
		
		Button backButton = new Button("Back to Display Data");
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayDataScene.fxml"));
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}

			    Scene scene = new Scene(root);
			    stage.setScene(scene);
			    stage.show();
				
			}
		});
		
		VBox root = new VBox(backButton, calendarView);
		VBox.setVgrow(calendarView, Priority.ALWAYS);

		Scene scene = new Scene(root);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.setWidth(1300);
        stage.setHeight(1000);
        stage.centerOnScreen();
        stage.show();
	}
	
	@FXML
    public void btnLogoutClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	}

	
	public Map<String, Integer> countShiftOccurrences (List<Shift> shifts) {
		
		Map<String, Integer> shiftDictonary = new LinkedHashMap<>();
		
		Comparator<Shift> compareTime = (c1, c2) -> c1.getStartDateTime().toLocalTime().compareTo(c2.getStartDateTime().toLocalTime());		
		shifts.sort(compareTime);
		
		for (Shift shift : shifts) {
			
			if (dayOfWeekFilter != null) {
				
				if (dayOfWeekFilter.equals(shift.getStartDateTime().toLocalDate().getDayOfWeek())) {
					
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
    
    public Map<String, Integer> countStoreOccurrences (List<Shift> shifts) {
    	
    	Map<String, Integer> storeDictionary = new LinkedHashMap<>();
    	
    	for (Shift shift : shifts) {
    		storeDictionary.put(shift.getStore(), storeDictionary.getOrDefault(shift.getStore(), 0) + 1);
    	}
    	
    	return storeDictionary;
    }
    
    public Map<String, Integer> countBookerOccurrences (List<Shift> shifts) {
    	
    	Map<String, Integer> bookerDictionary = new LinkedHashMap<>();
    	
    	for (Shift shift : shifts) {
    		bookerDictionary.put(shift.getBooker(), bookerDictionary.getOrDefault(shift.getBooker(), 0) + 1);
    	}
    	
    	return bookerDictionary;
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
	
    public void populateStoreOccurrenceListView() {
    	
    	if(storeOccurrenceListView.getItems().size() != 0) {
    		storeOccurrenceListView.getItems().clear();
    	}
    	
    	LinkedHashMap<String, Integer> storeDictonary = new LinkedHashMap<>(countStoreOccurrences(data.getShifts()));
    	
    	for (Map.Entry<String, Integer> entry : storeDictonary.entrySet()) {
			storeOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
		}
    }
    
    public void populateBookerOccurrenceListView() {
    	
    	if(bookerOccurrenceListView.getItems().size() != 0) {
    		bookerOccurrenceListView.getItems().clear();
    	}
    	
    	LinkedHashMap<String, Integer> bookerDictonary = new LinkedHashMap<>(countBookerOccurrences(data.getShifts()));
    	
    	for (Map.Entry<String, Integer> entry : bookerDictonary.entrySet()) {
			bookerOccurrenceListView.getItems().add(entry.getKey() + " Count: " + entry.getValue());
		}
    }
    
}
