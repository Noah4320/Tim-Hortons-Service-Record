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
	
	DataSingleton data = DataSingleton.getInstance();

	public void initialize() {
	    
		fromDateLabel.setText("test");
		
	}
	
}
