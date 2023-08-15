package application;

import java.util.List;

public class DataSingleton {

	private static final DataSingleton instance = new DataSingleton();
	
	private List<Shift> shifts;
	
	private DataSingleton() {}
	
	public static  DataSingleton getInstance() {
		return instance;
	}
	
	
	public List<Shift> getShifts() {
		return shifts;
	}
	
	public void setShifts(List<Shift> shifts)
	{
		this.shifts = shifts;
	}
	
}
