package application;
	
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

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
		
		String pop3Host = "pop.gmail.com";
		String mailStoreType = "pop3s";
		final String username = "Noah_12320@hotmail.com";
		final String password = "abc123";
		
		receiveMail(pop3Host, mailStoreType, username, password);
		
		System.out.println("Launch successful");
		launch(args);
	}
	
	public static void receiveMail(String pop3Host, String storeType, String username, String password)
	{
		Properties props = new Properties();
		
		props.put("mail.pop3.host", props);
		props.put("mail.pop3.port", "995");
		props.put("mail.pop3.starttls.enable", "true");
		props.put("mail.store.protocol", "pop3");
		
		Session session = Session.getInstance(props);
		
		try {
			
			Store mailStore = session.getStore(storeType);
			mailStore.connect(username, password);
			
			Folder folder = mailStore.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			
			Message[] emailMessages = folder.getMessages();
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
				
				System.out.println("Text - " + message.getContent().toString());
				
			}
			
			
			folder.close(false);
			mailStore.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in receiving email.");
		}
	}
	
}
