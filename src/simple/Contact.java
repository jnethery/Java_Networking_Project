package simple;

import java.io.Serializable;

/** Stores contact information of a UserAccount
 * 
 * @author jnethery
 *
 */
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
	
	/** Gets a user's PID
	 * 
	 * @return
	 */
	public String getPID()
	{
		return PID;
	}
	
	/** Gets a user's first name
	 * 
	 * @return
	 */
	public String getFirstName()
	{
		return firstName;
	}
	
	/** Gets a user's last name
	 * 
	 * @return
	 */
	public String getLastName()
	{
		return lastName;
	}
	
	/** Gets a user's year
	 * 
	 * @return
	 */
	public Year getYear()
	{
		return year;
	}
	
	/** Gets a user's program
	 * 
	 * @return
	 */
	public Program getProgram()
	{
		return program;
	}
}
