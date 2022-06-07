package application;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class Shift {
	
	private Date date;
	private String booker;
	private double money;
	private boolean mainStore;
	
	
	
	private DateFormatSymbols calendarFormat = new DateFormatSymbols();
	private String[] months = calendarFormat.getMonths();
	private String[] daysOfWeek = calendarFormat.getWeekdays();
	
	Calendar calendar = Calendar.getInstance();
	
	
	public Shift (Date date, String booker) {
		this.date = date;
		this.booker = booker;
		calendar.setTime(date);
		
	}
	
	
	private String getMonth() {
		return months[calendar.get(Calendar.MONTH)];
	}
	
	private String getDayOfWeek() {
		return daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];
	}
	
	private void calculateStatistics(Date fromDate, Date toDate) {
		
	}
	

}
