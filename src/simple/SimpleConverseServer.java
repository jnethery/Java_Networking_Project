package simple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/** Handles client requests 
 * 
 * @author jnethery
 *
 */
public class SimpleConverseServer implements Runnable{
	
	private Socket connection;
	
	public SimpleConverseServer(Socket socket)
	{
		connection = socket;
		System.out.println("Server accepted on port " + socket.getLocalPort());
	}
	
	public synchronized void run()
	{
		boolean clientIsConnected = true;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Request request = null;
		
		try {
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			System.out.println("Successfully connected to ObjectInputStream");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error on ObjectInputStream");
		}
		
		while (clientIsConnected)
		{
			try {
				request = (Request) in.readObject();
				System.out.println("Successfully got request " + request.getType().name());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<UserAccount> userDB = null;
			try {
				userDB = getUserDB();
			} catch (ClassNotFoundException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			} catch (IOException e5) {
				// TODO Auto-generated catch block
				e5.printStackTrace();
			}
			
			switch(request.getType())
			{
			case ADD_CONTACT:
				for (UserAccount u: userDB)
				{
					if (u.getContactInfo().getPID().equals(request.getUser().getContactInfo().getPID()))
					{
						u.addToContacts((Contact) request.getParam(0));
						try {
							updateUserDB(userDB);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
				
				break;
			case SEARCH: //searches for users in database given parameters
				SearchType searchType = (SearchType) request.getParam(0);
				ArrayList<UserAccount> searchResults = new ArrayList<UserAccount>();
	
				switch(searchType)
				{
				case Type:
					AccountType typeToSearch = (AccountType) request.getParam(1);
					for (UserAccount u: userDB)
					{
						if (u.getUserType().equals(typeToSearch))
						{
							searchResults.add(u);
						}
					}
					break;
				case Name:
					String firstNameToSearch = (String) request.getParam(1);
					String lastNameToSearch = (String) request.getParam(2);
					for (UserAccount u: userDB)
					{
						if (u.getContactInfo().getFirstName().equals(firstNameToSearch))
						{
							if (u.getContactInfo().getLastName().equals(lastNameToSearch))
							{
								searchResults.add(u);
							}
						}
					}
					break;
				case Year:
					Year yearToSearch = (Year) request.getParam(1);
					for (UserAccount u: userDB)
					{
						if (u.getContactInfo().getYear().equals(yearToSearch))
						{
							searchResults.add(u);
						}
					}
					break;
				case Program:
					Program programToSearch = (Program) request.getParam(1);
					for (UserAccount u: userDB)
					{
						if (u.getContactInfo().getProgram().equals(programToSearch))
						{
							searchResults.add(u);
						}
					}
					break;
				case PID:
					String PIDtoSearch = (String) request.getParam(1);
					for (UserAccount u: userDB)
					{
						if (u.getContactInfo().getPID().equals(PIDtoSearch))
						{
							searchResults.add(u);
						}
					}
					break;
				case ALL:
					for (UserAccount u: userDB)
					{
						searchResults.add(u);
					}
					break;
				}
				try {
					out.writeObject(searchResults);
					out.flush();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				break;
			case REGISTER:
				userDB.add(request.getUser());
				try {
					updateUserDB(userDB);
				} catch (ClassNotFoundException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				break;
			case LOGIN:
				UserAccount account = null;
				boolean userExistsInSystem = false;
				for (UserAccount u: userDB)
				{
					if(compareUsers(u.getContactInfo(), request.getUser().getContactInfo()))
					{
						userExistsInSystem = true;
						account = u;
						break;
					}
				}
				if (userExistsInSystem)
				{
					if (request.getUser().getPassword().equals((String) request.getParam(0)))
					{
						System.out.println("Successfully logged in user " + account.getContactInfo().getFirstName());
						try {
							out.writeObject(new Boolean(true));
							out.writeObject(account);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						System.out.println("Incorrect password");
						try {
							out.writeObject(new Boolean(false));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				}
				else
				{
					System.out.println("User " + request.getUser().getContactInfo().getFirstName() + " does not exist!");
					try {
						out.writeObject(new Boolean(false));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case SEND_MESSAGE:
				System.out.println("Successfully got request to send message from user " + request.getUser().getContactInfo().getFirstName());
				Message message = (Message) request.getParam(0); //gets the Message from the request.
				System.out.println("Successfully retrieved message " + message.getMessage());
				
				for (UserAccount u: userDB)
				{
					if (message.getDestination().getPID().equals(u.getContactInfo().getPID()))
					{
						u.addToMessageLog(message);
						try {
							updateUserDB(userDB);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
				
				System.out.println("Successfully sent message " + message.getMessage());
				
				ArrayList<Message> messageLog = null;
				try {
					messageLog = getMessageLog();
				} catch (ClassNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				updateMessageLog(messageLog, message);
				break;
			case GET_MESSAGES:
				for (UserAccount u: userDB)
				{
					if (request.getUser().getContactInfo().getPID().equals(u.getContactInfo().getPID()))
					{
						try {
							out.writeObject(u.getMessageLog());
							out.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			case LOGOUT:
				try {
					updateUserAccount(request.getUser());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case DISCONNECT:
				clientIsConnected = false;
				break;
			case VIEW_MESSAGE_LOG:
				try {
					out.writeObject(getMessageLog());
					out.flush();
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Could not establish connection to Admin");			
				}
				
			}
		}
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** Compares two Contacts to each other
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean compareUsers(Contact a, Contact b)
	{
		if (a.getPID().equals(b.getPID()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/** Gets the user database
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<UserAccount> getUserDB() throws IOException, ClassNotFoundException
	{
		FileInputStream fromUserDB = null;
		ObjectInputStream oisFromUserDB = null;
		
		fromUserDB = new FileInputStream("UserDB.txt");
		oisFromUserDB = new ObjectInputStream(fromUserDB);
		@SuppressWarnings("unchecked")
		ArrayList<UserAccount> userDB = (ArrayList<UserAccount>) oisFromUserDB.readObject();
		fromUserDB.close();
		oisFromUserDB.close();
		return userDB;
	}
	
	/** Gets the message log database 
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ArrayList<Message> getMessageLog() throws IOException, ClassNotFoundException
	{
		FileInputStream fromMessageLog = null;
		ObjectInputStream oisFromMessageLog = null;
		
		fromMessageLog = new FileInputStream("MessageLog.txt");
		oisFromMessageLog = new ObjectInputStream(fromMessageLog);
		@SuppressWarnings("unchecked")
		ArrayList<Message> messageLog = (ArrayList<Message>) oisFromMessageLog.readObject();
		fromMessageLog.close();
		oisFromMessageLog.close();
		return messageLog;		
	}
	
	/** Updates the message log database
	 * 
	 * @param message
	 * @param mess
	 */
	public void updateMessageLog(ArrayList<Message> message, Message mess)
	{
		FileOutputStream toDB = null;
		ObjectOutputStream oosToDB = null;
		message.add(mess);
		try {
			toDB = new FileOutputStream("MessageLog.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			oosToDB = new ObjectOutputStream(toDB);
			oosToDB.writeObject(message);
			oosToDB.flush();
			oosToDB.close();
			toDB.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	/** Updates the user database
	 * 
	 * @param users
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void updateUserDB(ArrayList<UserAccount> users) throws ClassNotFoundException, IOException
	{
		FileOutputStream toDB = null;
		ObjectOutputStream oosToDB = null;
		try {
			toDB = new FileOutputStream("userDB.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			oosToDB = new ObjectOutputStream(toDB);
			oosToDB.writeObject(users);
			oosToDB.flush();
			oosToDB.close();
			toDB.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
	
	/** Updates a specific user account in the user database
	 * 
	 * @param currentUser
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void updateUserAccount(UserAccount currentUser) throws ClassNotFoundException, IOException
	{
		ArrayList<UserAccount> users = getUserDB();
		for (UserAccount u: users)
		{
			if (u.getContactInfo().getPID().equals(currentUser.getContactInfo().getPID()))
			{
				u = currentUser;
				break;
			}
		}	
		updateUserDB(users);
		System.out.println("Sent call to update UserDB");
	}
}
