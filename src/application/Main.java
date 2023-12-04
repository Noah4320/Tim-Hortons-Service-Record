package application;
	
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	public static int totalMessages = 0;
	public static double localProgressValue = 0;
	public static volatile boolean stopFlag = false;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));	
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("Tim Hortons Service Record" );
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Launch successful");
		//System.out.println("Launch successful");
		launch(args);
	}
	
	public static List<Shift> receiveMail(String username, String password, LocalDate localFromDate, LocalDate localToDate)
	{
		localProgressValue = 0;
		Properties props = new Properties();
		
		 props.setProperty("mail.transport.protocol", "smtp");
		 props.setProperty("mail.host", "smtp.live.com");
		 props.put("mail.smtp.starttls.enable", "true");
		 props.put("mail.smtp.auth", "true");
		
		 props.put("mail.smtp.starttls.enable", "true");
		 Session session = Session.getInstance(props); 
		 session.setDebug(true);
		 
		 List<Shift> shifts = new ArrayList<Shift>();
		 session.setDebug(false);
		
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
			DateTimeFormatter localStringFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
			
			SimpleDateFormat localDateFormatter = new SimpleDateFormat( "MM/dd/yy" );
		    java.util.Date fromDate = localDateFormatter.parse(localFromDate.format(localStringFormatter));
		    java.util.Date toDate = localDateFormatter.parse(localToDate.format(localStringFormatter));
			
		    
			
			//Apply filters
		    
		    //2017-2019
		    Message[] emailMessages1 = folder.search(applyFilters("quickservicesoftware", fromDate, toDate));
			//2019
			Message[] emailMessages2 = folder.search(applyFilters("qssweb", fromDate, toDate));
			//2019-2022
			Message[] emailMessages3 = folder.search(applyFilters("clearviewconnect", fromDate, toDate));
			
			totalMessages = emailMessages1.length + emailMessages2.length + emailMessages3.length;
				
			processEmails(emailMessages1, shifts, localFromDate, localToDate);
			processEmails(emailMessages2, shifts, localFromDate, localToDate);
			processEmails(emailMessages3, shifts, localFromDate, localToDate);
					
			folder.close(false);
			mailStore.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in receiving email.");
		}
		return shifts;
	}
	
	public static void processEmails(Message[] emailMessages, List<Shift> shifts, LocalDate fromLocalDate, LocalDate toLocalDate) throws MessagingException, IOException {
		
		System.out.println("Total Message - " + emailMessages.length);
		
		//Iternate the messages
		for (int i = 0; i < emailMessages.length; i++) {
			
			if (Thread.currentThread().isInterrupted()) {
				return;
			}
			
			Message message = emailMessages[i];

			String[] subjectSplit = message.getSubject().split(" ");
			Address[] toAddress = message.getRecipients(Message.RecipientType.TO);
			System.out.println();
			System.out.println("Email " + (i+1) + "-");
			System.out.println("From - " + message.getFrom()[0]);
			System.out.println("To - ");
			
			for (int j = 0; j < toAddress.length; j++) {
				System.out.println(toAddress[j].toString());
			}
		
			//Get calendar metadata
			DateFormatSymbols calendar = new DateFormatSymbols();
			String[] months = calendar.getShortMonths();
			String[] daysOfWeek = calendar.getWeekdays();

			int indexOfBooker = Arrays.asList(message.getContent().toString().split("\\r?\\n")).indexOf("Thanks,") + 1;
			String booker = message.getContent().toString().split("\\r?\\n")[indexOfBooker].trim();

			System.out.println("Text - " + message.getContent().toString());
			
			for (String shiftAsString : getShiftsAsString(message)) {
				
				if (!shiftAsString.contains("Not Scheduled"))
				{
				
				try {
					String monthDayAsString = shiftAsString.split(":")[0].trim();
					String monthAsString = monthDayAsString.split(" ")[0].trim();
					String dayAsString = monthDayAsString.split(" ")[1].trim();
					String store = "";
					
					for (String word : shiftAsString.split(" ")) {
						
						if (word.contains("#")) {
						    store = "Main";
						}
						
						if (store.equals("")) {
							store = "Esso";
						}
						
					}
				
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
			        
			       //Process the date and times
			       if (startTimeString != null && endTimeString != null)
			       {
			    	   DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");
			    	   DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
			    	   
				        LocalTime startTime = LocalTime.parse(startTimeString, timeFormatter);
				        LocalTime endTime = LocalTime.parse(endTimeString, timeFormatter);
				        
				        LocalDate date;
				        //Detect if year flips
				        if (monthAsString.equals("Jan")) {
				        	 date = LocalDate.parse(subjectSplit[9] + "-" + monthAsString + "-" + dayAsString, dateFormatter);
				        } else {
				        	 date = LocalDate.parse(subjectSplit[5] + "-" + monthAsString + "-" + dayAsString, dateFormatter);
				        }
				        
				        
				        
				        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
				        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);			       
				        
				        if (startDateTime.toLocalDate().isBefore(fromLocalDate) || startDateTime.toLocalDate().isAfter(toLocalDate)) {
				        	continue;
				        }
				        
				        //Add shift to list
				        Shift shift = new Shift(startDateTime, endDateTime, subjectSplit[subjectSplit.length - 1],  store, booker);	                
				        
				        if (subjectSplit[subjectSplit.length - 1].equals("changed")) {
				        	
				        	boolean isDuplicate = shift.findAndReplaceDuplicates(shifts);
				        	
				        	if (!isDuplicate) {
				        		shifts.add(shift);
				        	}
				        	
				        }
				        else {
				        	shifts.add(shift);
				        }
				        
				        shift.processMoney();
				        
				        
				        System.out.printf("Duration: %d hours, %d minutes, %d seconds %n",
				                shift.getDuration().toHours(),
				                shift.getDuration().toMinutesPart(),
				                shift.getDuration().toSecondsPart()
				        );
				        System.out.println("Month: " + shift.getMonth());
			       }
			       
				} catch (Exception e) {
					//e.printStackTrace();
					System.err.println("Issue with processing shift: " + shiftAsString);
				}
				
				}
				
			} // end loop for processing shift
			
			localProgressValue += 1;
			MainSceneController.progressValue = localProgressValue / totalMessages;
			
		} //end loop for processing email
	}
	
	public static SearchTerm applyFilters(String address, Date fromDate, Date toDate) {
		
		LocalDate fromLocalDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		fromLocalDate = fromLocalDate.minusWeeks(2);
		fromDate = Date.from(fromLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		LocalDate toLocalDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		toLocalDate = toLocalDate.plusWeeks(2);
		toDate = Date.from(toLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		//Create filters
		SearchTerm filterAddress = new FromStringTerm(address);
		SearchTerm filterFromDate = new ReceivedDateTerm(ComparisonTerm.GT, fromDate);
		SearchTerm filterToDate = new ReceivedDateTerm(ComparisonTerm.LT, toDate);
		SearchTerm[] allFilters = {filterAddress, filterFromDate, filterToDate};
		SearchTerm filters = new AndTerm(allFilters);
		
		return filters;
	}
	
	public static List<String> getShiftsAsString(Message message) throws IOException, MessagingException {
		//Removes all new lines
		String[] shiftsSplit = message.getContent().toString().split("\\r?\\n"); 		
		//Convert to list and take shift data only. ToDo: Considering taking manager name of who distributed email and which store 
		List<String> shiftsAsString = Arrays.asList(shiftsSplit);
		
		int startIndex = 0;
		int endIndex = 0;
		
		for (int i=0; i < shiftsAsString.size(); i++) {
			
			if (shiftsAsString.get(i).contains("Monday")) {
				startIndex = i;
			}
			
			if (shiftsAsString.get(i).contains("Sunday")) {
				endIndex = i;
			}
		}
		
		shiftsAsString = shiftsAsString.subList(startIndex, endIndex + 1);
		
		return shiftsAsString;
	}
	
}
