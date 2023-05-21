package application;

import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Shift {
	
	private LocalDateTime startDateTime;
	private LocalDateTime finishDateTime;
	private String booker;
	private double money;
	private boolean mainStore;
	private Duration duration;
	
	
	
	private DateFormatSymbols calendarFormat = new DateFormatSymbols();
	private String[] months = calendarFormat.getMonths();
	private String[] daysOfWeek = calendarFormat.getWeekdays();
	
	Calendar calendar = Calendar.getInstance();
	
	
	public Shift (LocalDateTime startDateTime, LocalDateTime finishDateTime, String booker) {
		this.startDateTime = startDateTime;
		this.finishDateTime = finishDateTime;
		this.booker = booker;
		//calendar.setTime(date);
		
	}
	
	
	private String getMonth() {
		return months[calendar.get(Calendar.MONTH)];
	}
	
	private String getDayOfWeek() {
		return daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];
	}
	
	private void getTotalHours(Date fromDate, Date toDate) {
		
	}
	

}
