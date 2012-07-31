package simple;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	private String message;
	private Contact destination;
	private Contact source;
	
	public Message(String message, Contact destination, Contact source)
	{
		this.message = message;
		this.destination = destination;
		this.source = source;
	}
	
	public String getMessage()
	{
		GregorianCalendar gc = new GregorianCalendar();
		String timestamp = gc.get(Calendar.HOUR_OF_DAY) + ":"
				+ gc.get(Calendar.MINUTE) + "-"
				+ (gc.get(Calendar.MONTH)+1) + "/"
				+ gc.get(Calendar.DAY_OF_MONTH) + "/"
				+ gc.get(Calendar.YEAR);
		return timestamp + "\nFROM: " + source.getFirstName() + " " + source.getLastName() + " " + source.getPID()
				+ "\nTO: " + destination.getFirstName() + " " + destination.getLastName() + " " + destination.getPID()
				+ "\n" + message;
	}
	
	public Contact getDestination()
	{
		return destination;
	}
}
