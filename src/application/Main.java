package application;
	
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

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
			
			//sign in
			Store mailStore = session.getStore("imaps");
			//Transport trans = session.getTransport("smtp");
			//trans.connect("smtp.live.com", 25, username, password);
			mailStore.connect("imap-mail.outlook.com", username, password);
			
			//Open inbox
			Folder folder = mailStore.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			
			//Parse to/from dates
			 SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yy" );
		     String fDate="03/01/22";
		     java.util.Date fromDate = df1.parse(fDate);
		     String tDate="05/01/22";
		     java.util.Date toDate = df1.parse(tDate);
			
		    //Create filters
			SearchTerm filterAddress = new FromStringTerm("clearviewconnect");
			SearchTerm filterFromDate = new ReceivedDateTerm(ComparisonTerm.GT, fromDate);
			SearchTerm filterToDate = new ReceivedDateTerm(ComparisonTerm.LT, toDate);
			SearchTerm[] allFilters = {filterAddress, filterFromDate, filterToDate};
			SearchTerm filters = new AndTerm(allFilters);
			
			//Apply filters
			Message[] emailMessages = folder.search(filters);
			System.out.println("Total Message - " + emailMessages.length);
			
			
			//Iternate the messages
			for (int i = 0; i < emailMessages.length; i++) {
				Message message = emailMessages[i];
				//5, 9
				String[] subjectSplit = message.getSubject().split(" ");
				Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
				System.out.println();
				System.out.println("Email " + (i+1) + "-");
				System.out.println("From - " + message.getFrom()[0]);
				System.out.println("To - ");
				
				for (int j = 0; j < toAddress.length; j++) {
					System.out.println(toAddress[j].toString());
				}
				
				//Removes all new lines
				String[] shiftsSplit = message.getContent().toString().split("\\r?\\n"); 		
				//Convert to list and take shift data only. ToDo: Considering taking manager name of who distributed email and which store 
				List<String> shiftsAsString = Arrays.asList(shiftsSplit);
				shiftsAsString = shiftsAsString.subList(6, 13);
				
				//Get calendar metadata
				DateFormatSymbols calendar = new DateFormatSymbols();
				String[] months = calendar.getShortMonths();
				String[] daysOfWeek = calendar.getWeekdays();
				
				
				for (String shiftAsString : shiftsAsString) {
					
					String monthDayAsString = shiftAsString.split(":")[0].trim();
					String monthAsString = monthDayAsString.split(" ")[0].trim();
					String dayAsString = monthDayAsString.split(" ")[1].trim();
				
				        Pattern pattern = Pattern.compile("\\d{1,2}:\\d{2}(AM|PM)");
				        Matcher matcher = pattern.matcher(shiftAsString);
				        
				        String startTimeString = null;
			            String endTimeString = null;
				        
			            //Get start and end time
				        while (matcher.find()) {
				            
				            
				            if (startTimeString == null)
				            {
				            	startTimeString = matcher.group();
				            }
				            else
				            {
				            	endTimeString = matcher.group();
				            }
				        }
				        
				       //Process the times
				       if (startTimeString != null && endTimeString != null)
				       {
				    	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
				    	   DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
					        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
					        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

					        LocalDate date = LocalDate.parse(subjectSplit[5] + "-" + monthAsString + "-" + dayAsString, dateFormatter);
					        
					        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
					        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
					        
					        Shift shift = new Shift(startDateTime, endDateTime, null);
					        shift.getDuration();
				       }
					
					for (String month : months) {
						
						boolean isMonth = shiftAsString.contains(month);
						
						if (isMonth) {
							System.out.println(month);
						}
						
					}
					
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
