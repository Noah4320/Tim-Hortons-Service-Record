package application;

import java.text.DateFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Shift {
	
	private LocalDateTime startDateTime;
	private LocalDateTime finishDateTime;
	private String booker;
	private double money;
	private String store;
	private String period;
	
	
	public Shift (LocalDateTime startDateTime, LocalDateTime finishDateTime, String period, String store, String booker) {
		this.startDateTime = startDateTime;
		this.finishDateTime = finishDateTime;
		this.period = period;
		this.store = store;
		this.booker = booker;
		
	}
	
	
	public String getMonth() {
		return startDateTime.getMonth().toString();
	}
	
	public String getDayOfWeek() {
		return startDateTime.getDayOfWeek().toString();
	}
	
	public void processMoney() {
		
		double minWage = 0;
		
		if (startDateTime.toLocalDate().isBefore(LocalDate.parse("2017-10-01"))) {
			minWage = 11.40;
		}
		
		else if (startDateTime.toLocalDate().isAfter(LocalDate.parse("2017-09-30")) && startDateTime.toLocalDate().isBefore(LocalDate.parse("2018-01-01"))) {
			minWage = 11.60;
		}
		
		else if (startDateTime.toLocalDate().isAfter(LocalDate.parse("2017-12-31")) && startDateTime.toLocalDate().isBefore(LocalDate.parse("2020-10-01"))) {
			minWage = 14.00;
		}
		
		else if (startDateTime.toLocalDate().isAfter(LocalDate.parse("2020-09-30")) && startDateTime.toLocalDate().isBefore(LocalDate.parse("2021-10-01"))) {
			minWage = 14.25;
		}
		
		else if (startDateTime.toLocalDate().isAfter(LocalDate.parse("2021-09-30")) && startDateTime.toLocalDate().isBefore(LocalDate.parse("2022-01-01"))) {
			minWage = 14.35;
		}
		
		else if (startDateTime.toLocalDate().isAfter(LocalDate.parse("2021-12-31"))) {
			minWage = 15.00;
		}
		
		else {
			minWage = 14.00;
		}
		
		
		
		if (getDuration().toMinutesPart() == 30) {
			money = minWage * (getDuration().toHours() + 0.5);
		}
		else {
			money = minWage * getDuration().toHours();
		}
		
	}
	
	public static double getTotalMoney(List<Shift> shifts) {
		
		double sum = 0;
		
		for(Shift shift : shifts) {
			sum += shift.money;
		}
		
		return sum;
	}
	
	public void getTotalHours(Date fromDate, Date toDate) {
		
	}
	
	public Duration getDuration() {
		
		// calculate the duration between the two times
        Duration duration = Duration.between(startDateTime.toLocalTime(), finishDateTime.toLocalTime());
          
        return duration;
	}

	public void removeDuplicates(List<Shift> shifts) {
		
		for(int i=0; i < shifts.size(); i++) {
			
			if (shifts.get(i).startDateTime.toLocalDate().equals(this.startDateTime.toLocalDate()) && shifts.get(i).finishDateTime.toLocalDate().equals(this.finishDateTime.toLocalDate())) {
				shifts.remove(i);
				shifts.add(this);
			}
			
		}
		
	}
	
	public LocalDateTime getStartDateTIme() {
		return startDateTime;
	}

	public LocalDateTime getFinishDateTIme() {
		return startDateTime;
	}
	
	public String getBooker() {
		return booker;
	}
	
	public double getMoney() {
		return money;
	}
	
	public String getStore() {
		return store;
	}
}
