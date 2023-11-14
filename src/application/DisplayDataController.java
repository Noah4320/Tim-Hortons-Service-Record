package application;

import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DisplayDataController {

	@FXML
	private Label fromDateLabel;
	@FXML
	private Label toDateLabel;
	@FXML
	private Label moneyEarnedLabel;
	
	DataSingleton data = DataSingleton.getInstance();

	public void initialize() {
	    
		
		if (!data.getShifts().isEmpty()) {
			fromDateLabel.setText(data.getShifts().get(0).getStartDateTIme().toLocalDate().toString());
			toDateLabel.setText(data.getShifts().get(data.getShifts().size() - 1).getStartDateTIme().toLocalDate().toString());
			moneyEarnedLabel.setText(String.format("$%.2f", Shift.getTotalMoney(data.getShifts())));
		}
		
	}
	
}
