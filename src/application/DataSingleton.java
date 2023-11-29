package application;

import java.util.Comparator;
import java.util.List;

public class DataSingleton {

	private static final DataSingleton instance = new DataSingleton();
	
	private List<Shift> shifts;
	
	private DataSingleton() {}
	
	public static  DataSingleton getInstance() {
		return instance;
	}
	
	
	public List<Shift> getShifts() {
		
		Comparator<Shift> compareDate = (c1, c2) -> c1.getStartDateTime().toLocalDate().compareTo(c2.getStartDateTime().toLocalDate());
		shifts.sort(compareDate);
		
		return shifts;
	}
	
	public void setShifts(List<Shift> shifts)
	{
		this.shifts = shifts;
	}
	
}
