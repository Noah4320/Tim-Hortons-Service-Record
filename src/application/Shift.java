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
	private String store;
	
	
	public Shift (LocalDateTime startDateTime, LocalDateTime finishDateTime, String store, String booker) {
		this.startDateTime = startDateTime;
		this.finishDateTime = finishDateTime;
		this.store = store;
		this.booker = booker;
		
	}
	
	
	public String getMonth() {
		return startDateTime.getMonth().toString();
	}
	
	public String getDayOfWeek() {
		return startDateTime.getDayOfWeek().toString();
	}
	
	public void getTotalHours(Date fromDate, Date toDate) {
		
	}
	
	public Duration getDuration() {
		  // calculate the duration between the two times
        Duration duration = Duration.between(startDateTime.toLocalTime(), finishDateTime.toLocalTime());
        
        System.out.printf("Duration: %d hours, %d minutes, %d seconds %n",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart()
        );  
        return duration;
	}

}
