package application;
	
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.search.FromStringTerm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("Hello World!" );
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Launch successful");
		launch(args);
	}
	
	public static void receiveMail(String username, String password)
	{
		Properties props = new Properties();
		
		 props.setProperty("mail.transport.protocol", "smtp");
		 props.setProperty("mail.host", "smtp.live.com");
		 props.put("mail.smtp.starttls.enable", "true");
		 props.put("mail.smtp.auth", "true");
		
		 props.put("mail.smtp.starttls.enable", "true");
		 Session session = Session.getInstance(props); 
		 session.setDebug(true);
		
		try {
			
			Store mailStore = session.getStore("imaps");
			//Transport trans = session.getTransport("smtp");
			//trans.connect("smtp.live.com", 25, username, password);
			mailStore.connect("imap-mail.outlook.com", username, password);
			
			Folder folder = mailStore.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			
			Message[] emailMessages = folder.search(new FromStringTerm(""));
			System.out.println("Total Message - " + emailMessages.length);
			
			//Iternate the messages
			for (int i = 0; i < emailMessages.length; i++) {
				Message message = emailMessages[i];
				
				Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
				System.out.println();
				System.out.println("Email " + (i+1) + "-");
				System.out.println("From - " + message.getFrom()[0]);
				
				System.out.println("To - ");
				
				for (int j = 0; j < toAddress.length; j++) {
					System.out.println(toAddress[j].toString());
				}
				
				String[] shiftsSplit = message.getContent().toString().split("\\r?\\n"); 				
				List<String> shifts = Arrays.asList(shiftsSplit);
				shifts = shifts.subList(6, 13);
				
				DateFormatSymbols calendar = new DateFormatSymbols();
				String[] months = calendar.getShortMonths();
				String[] daysOfWeek = calendar.getWeekdays();
				
				
				for (String shift : shifts) {
					
					for (String month : months) {
						
						boolean isMonth = shift.contains(month);
						
						if (isMonth) {
							System.out.println(month);
						}
						
					}
					
				}
				
				//System.out.println("Text - " + message.getContent().toString());
				
			}
			
			
			
			folder.close(false);
			mailStore.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in receiving email.");
		}
	}
	
}
