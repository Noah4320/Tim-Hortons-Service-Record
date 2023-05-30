package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;

public class MainSceneController {
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField passwordTextField;

	// Event Listener on TextField.onAction
	@FXML
	public void usernameTextField(ActionEvent event) {
		// TODO Autogenerated
	}
	
	@FXML
	public void passwordTextField(ActionEvent event) {

	}
	
	// Event Listener on Button.onAction
	@FXML
	public void btnOkClicked(ActionEvent event) throws IOException {
		Stage mainWindow = (Stage) usernameTextField.getScene().getWindow();
		String username = usernameTextField.getText();
		String password = passwordTextField.getText();
		Main.receiveMail(username, password);
		
		Parent root = FXMLLoader.load(getClass().getResource("DisplayDataScene.fxml"));
	    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
		
	}
}
