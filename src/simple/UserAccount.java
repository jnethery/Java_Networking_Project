package simple;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

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
	
	public AccountType getUserType()
	{
		return type;
	}
	
	public Contact getContactInfo()
	{
		return contactInfo;
	}
	
	public Contact getFromContacts(int pos)
	{
		return ContactList.get(pos);
	}
	
	public ArrayList<Contact> getContacts()
	{
		return ContactList;
	}
	
	public ArrayList<Message> getMessageLog()
	{
		return MessageLog;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void addToMessageLog (Message message)
	{
		MessageLog.add(message);
	}
	
	public void overwriteMessageLog (ArrayList<Message> messages)
	{
		this.MessageLog = messages;
	}
	
	public void addToContacts(Contact contact)
	{
		ContactList.add(contact);
	}
}
