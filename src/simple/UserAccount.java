package simple;

import java.util.ArrayList;
import java.io.Serializable;

/** Holds the user's contact information and their message log and contact list
 * 
 * @author jnethery
 *
 */
public class UserAccount implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private AccountType type;
	private String password;
	private ArrayList<Message> MessageLog = new ArrayList <Message>();
	private ArrayList<Contact> ContactList = new ArrayList<Contact>();
	private Contact contactInfo;
	
	public UserAccount(Contact contact, AccountType type, String password)
	{
		this.contactInfo = contact;
		this.type = type;
		this.password = password;
	}
	
	/** Gets the account's type
	 * 
	 * @return
	 */
	public AccountType getUserType()
	{
		return type;
	}
	
	/** Gets the user's contact information
	 * 
	 * @return
	 */
	public Contact getContactInfo()
	{
		return contactInfo;
	}
	
	/** Return specific user from contact list
	 * 
	 * @param pos
	 * @return
	 */
	public Contact getFromContacts(int pos)
	{
		return ContactList.get(pos);
	}
	
	/** Returns user's contact list
	 * 
	 * @return
	 */
	public ArrayList<Contact> getContacts()
	{
		return ContactList;
	}
	
	/** Returns user's message log
	 * 
	 * @return
	 */
	public ArrayList<Message> getMessageLog()
	{
		return MessageLog;
	}
	
	/** Returns user's password
	 * 
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}
	
	/** Adds a message to the user's message log
	 * 
	 * @param message
	 */
	public void addToMessageLog (Message message)
	{
		MessageLog.add(message);
	}
	
	/** Overwrites the user's message log
	 * 
	 * @param messages
	 */
	public void overwriteMessageLog (ArrayList<Message> messages)
	{
		this.MessageLog = messages;
	}
	
	/** Adds a contact to the user's contact list
	 * 
	 * @param contact
	 */
	public void addToContacts(Contact contact)
	{
		ContactList.add(contact);
	}
}
