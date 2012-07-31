package simple;

import java.io.Serializable;

public class Contact implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	
	private String PID;
	private String firstName;
	private String lastName;
	private Year year;
	private Program program;
	
	public Contact(String PID, String firstName, String lastName, Year year, Program program)
	{
		this.PID = PID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.year = year;
		this.program = program;
	}
	
	public String getPID()
	{
		return PID;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public Year getYear()
	{
		return year;
	}
	
	public Program getProgram()
	{
		return program;
	}
}
