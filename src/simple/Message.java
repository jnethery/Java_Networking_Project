package simple;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.Serializable;

/** Holds a contact destination and contact source and a string representing a chat message
 * 
 * @author jnethery
 *
 */
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String message;
	private Contact destination;
	private Contact source;
	
	public Message(String message, Contact destination, Contact source)
	{
		this.message = message;
		this.destination = destination;
		this.source = source;
		GregorianCalendar gc = new GregorianCalendar();
		year = Integer.toString(gc.get(Calendar.YEAR));
		month = Integer.toString(gc.get(Calendar.MONTH) + 1);
		day = Integer.toString(gc.get(Calendar.DAY_OF_MONTH));
		hour = Integer.toString(gc.get(Calendar.HOUR_OF_DAY));
		minute = Integer.toString(gc.get(Calendar.MINUTE));
	}
	
	/** Returns the message as a string with a timestamp
	 * 
	 * @return
	 */
	public String getMessage()
	{
		String timestamp = hour + ":"
				+ minute + "-"
				+ month + "/"
				+ day + "/"
				+ year;
		return timestamp + "\nFROM: " + source.getFirstName() + " " + source.getLastName() + " " + source.getPID()
				+ "\nTO: " + destination.getFirstName() + " " + destination.getLastName() + " " + destination.getPID()
				+ "\n" + message;
	}
	
	/** Returns the contact destination for the message
	 * 
	 * @return
	 */
	public Contact getDestination()
	{
		return destination;
	}
}
