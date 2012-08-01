package simple;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;


/*** COMMENT OUT THE PROMPTS IN THE INDIVIDUAL METHODS WHEN LINKING METHODS TO GUI ***/
public class Client {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{	
		boolean clientRunning = true;
		boolean clientLoggedIn = false;
		
		InputStreamReader textIn = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(textIn);
	
		Socket clientSocket = new Socket("localhost", 1234);
		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
		
		UserAccount nullAccount = new UserAccount(new Contact(null, null, null, null, null), null, null);
		UserAccount User = null;
		
		while (clientRunning)
		{
			while (!clientLoggedIn)
			{
				System.out.println("LOGIN:");
				if (Login(in, oos, ois, nullAccount))
				{
					User = (UserAccount) ois.readObject();
					clientLoggedIn = true;
				}
				else 
				{
					System.out.println("Login credentials incorrect");
					boolean registerLoop = true;
					while (registerLoop)
					{
						System.out.println("Do you wish to create a new account to register? (y/n)");
						switch(in.readLine())
						{
						case "y":
							Register(in, oos);
							registerLoop = false;
							break;
						case "n":
							registerLoop = false;
							clientRunning = false;
							clientLoggedIn = true;
							Disconnect(oos, nullAccount);
							break;
						default:
							System.out.println("Invalid choice");
							break;
						}
					}
				}
			}
			if (clientRunning)
			{
				while (clientLoggedIn)
				{
					boolean validMenuChoice = false;
					while (!validMenuChoice)
					{
						System.out.println("Account Options:\n" +
							"LOGOUT\n" +
							"CONTACTS\n" +
							"GET\n" +
							"SEND\n" +
							"SEARCH");
						switch(User.getUserType())
						{
						case Staff:
							System.out.println("LOG");
							break;
						default:
							break;
						}
						switch (in.readLine())
						{
						case "log":
							switch(User.getUserType())
							{
							case Staff:
								GetMessageLog(oos, ois, User);
								break;
							default:
								break;
							}
							break;
						case "logout":
							Logout(oos, User);
							Disconnect(oos, User);
							validMenuChoice = true;
							clientLoggedIn = false;
							clientRunning = false;
							break;
						case "contacts":
							GetContacts(User, in);
							validMenuChoice = true;
							break;
						case "get":
							GetMessages(User, oos, ois);
							validMenuChoice = true;
							break;
						case "send":
							SendMessage(in, User, oos);
							validMenuChoice = true;
							break;
						case "search":
							Search(User, oos, ois, in);
							validMenuChoice = true;
							break;
						default:
							System.out.println("Not a valid choice.");
							break;
						}
					}
				}
			}
		}
		
		textIn.close();
		in.close();
		oos.close();
		ois.close();
		clientSocket.close();
	}
	
	public static boolean Login(BufferedReader in, ObjectOutputStream oos, ObjectInputStream ois, UserAccount nullAccount) throws IOException, ClassNotFoundException
	{
		String userName = null;
		String passWord = null;
		
		System.out.println("Enter your PID");
		userName = in.readLine();
		System.out.println("Enter your password");
		passWord = in.readLine();
		
		nullAccount = new UserAccount(new Contact(userName, null, null, null, null), null, passWord);
		
		Request LoginRequest = new Request(RequestType.LOGIN, nullAccount);
		LoginRequest.addParam(passWord);
		oos.writeObject(LoginRequest);
		oos.flush();
		return (Boolean) ois.readObject();
	}
	
	public static void Register(BufferedReader in, ObjectOutputStream oos) throws IOException
	{
		UserAccount accountToRegister = null;
		
		String PID = null;
		String firstName = null;
		String lastName = null;
		Year year = null;
		Program program = null;
		AccountType accountType = null;
		String passWord = null;
		
		boolean yearDefined = false;
		boolean programDefined = false;
		boolean typeDefined = false;
		
		System.out.println("Enter a PID");
		PID = in.readLine();
		System.out.println("Enter a first name");
		firstName = in.readLine();
		System.out.println("Enter a last name");
		lastName = in.readLine();
		while(!yearDefined)
		{
			System.out.println("Enter a year (Freshman, Sophomore, Junior, Senior)");
			switch(in.readLine())
			{
			case "Freshman":
				year = Year.Freshman;
				yearDefined = true;
				break;
			case "Sophomore":
				year = Year.Sophomore;
				yearDefined = true;
				break;
			case "Junior":
				year = Year.Junior;
				yearDefined = true;
				break;
			case "Senior":
				year = Year.Senior;
				yearDefined = true;
				break;
			default:
				System.out.println("Not a valid choice.");
				break;
			}
		}
		
		while(!programDefined)
		{
			System.out.println("Enter a program (CS, IT, CpE, EE)");
			switch(in.readLine())
			{
			case "CS":
				program = Program.CS;
				programDefined = true;
				break;
			case "IT":
				program = Program.IT;
				programDefined = true;
				break;
			case "CpE":
				program = Program.CpE;
				programDefined = true;
				break;
			case "EE":
				program = Program.EE;
				programDefined = true;
				break;
			default:
				System.out.println("Not a valid choice.");
				break;
			}
		}
		
		while(!typeDefined)
		{
			System.out.println("Enter an account type (Student, Faculty, Staff)");
			switch(in.readLine())
			{
			case "Student":
				accountType = AccountType.Student;
				typeDefined = true;
				break;
			case "Faculty":
				accountType = AccountType.Faculty;
				typeDefined = true;
				break;
			case "Staff":
				accountType = AccountType.Staff;
				typeDefined = true;
				break;
			default:
				System.out.println("Not a valid choice.");
				break;
			}
		}
		
		System.out.println("Enter a password");
		passWord = in.readLine();
		
		accountToRegister = new UserAccount(new Contact(PID, firstName, lastName, year, program), accountType, passWord);
		Request RegisterRequest = new Request(RequestType.REGISTER, accountToRegister);
		oos.writeObject(RegisterRequest);
		oos.flush();
	}
	
	@SuppressWarnings("unchecked")
	public static void Search(UserAccount User, ObjectOutputStream oos, ObjectInputStream ois, BufferedReader in) throws IOException, ClassNotFoundException
	{
		ArrayList<UserAccount> searchResults = null;
		SearchType search = null;
		boolean validChoice = false;
		Request searchRequest = new Request(RequestType.SEARCH, User);
		while (!validChoice)
		{
			System.out.println("Please choose search parameters:\n" +
					"PID\n" +
					"Name\n" +
					"Year\n" +
					"Program\n" +
					"Type\n" +
					"All");
			switch(in.readLine())
			{
			case "pid":
				search = SearchType.PID;
				searchRequest.addParam(search);
				System.out.println("Enter a PID to search for");
				searchRequest.addParam(in.readLine());
				validChoice = true;
				break;
			case "name":
				search = SearchType.Name;
				searchRequest.addParam(search);
				System.out.println("Enter a first name to search for");
				searchRequest.addParam(in.readLine());
				System.out.println("Enter a last name to search for");
				searchRequest.addParam(in.readLine());
				validChoice = true;
				break;
			case "year":
				boolean validYear = false;
				search = SearchType.Year;
				searchRequest.addParam(search);
				while(!validYear)
				{
					System.out.println("Enter a year to search for (Freshman, Sophomore, Junior, Senior)");
					switch(in.readLine())
					{
					case "freshman":
						searchRequest.addParam(Year.Freshman);
						validYear = true;
						break;
					case "sophomore":
						searchRequest.addParam(Year.Sophomore);
						validYear = true;
						break;
					case "junior":
						searchRequest.addParam(Year.Junior);
						validYear = true;
						break;
					case "senior":
						searchRequest.addParam(Year.Senior);
						validYear = true;
						break;
					default:
						System.out.println("Not a valid choice");
						break;
					}
				}
				validChoice = true;
				break;
			case "program":
				boolean validProgram = false;
				search = SearchType.Program;
				searchRequest.addParam(search);
				while(!validProgram)
				{
					System.out.println("Enter a program to search for (CS, IT, CpE, EE)");
					switch(in.readLine())
					{
					case "cs":
						searchRequest.addParam(Program.CS);
						validProgram = true;
						break;
					case "it":
						searchRequest.addParam(Program.IT);
						validProgram = true;
						break;
					case "cpe":
						searchRequest.addParam(Program.CpE);
						validProgram = true;
						break;
					case "ee":
						searchRequest.addParam(Program.EE);
						validProgram = true;
						break;
					default:
						System.out.println("Not a valid choice");
						break;
					}
				}
				validChoice = true;
				break;
			case "type":
				boolean validType = false;
				search = SearchType.Type;
				searchRequest.addParam(search);
				while(!validType)
				{
					System.out.println("Enter an account type to search for (Student, Faculty, Staff)");
					switch(in.readLine())
					{
					case "student":
						searchRequest.addParam(AccountType.Student);
						validType = true;
						break;
					case "faculty":
						searchRequest.addParam(AccountType.Faculty);
						validType = true;
						break;
					case "staff":
						searchRequest.addParam(AccountType.Staff);
						validType = true;
						break;
					default:
						System.out.println("Not a valid choice");
						break;
					}
				}
				validChoice = true;
				break;
			case "all":
				search = SearchType.ALL;
				searchRequest.addParam(search);
				validChoice = true;
				break;
			default:
				System.out.println("Not a valid choice");
				break;
			}
		}
	
		oos.writeObject(searchRequest);
		oos.flush();
		searchResults = (ArrayList<UserAccount>) ois.readObject();
		
		int userNumber = 1;
		for (UserAccount u: searchResults)
		{
			System.out.println(userNumber + ".) " + u.getContactInfo().getFirstName()
					+ " " + u.getContactInfo().getLastName());
			userNumber++;
		}
		
		if (searchResults.size() > 0)
		{
			validChoice = false;
			while(!validChoice)
			{
				System.out.println("Do you wish to add one of these as a contact?(y/n)");
				switch(in.readLine())
				{
				case "y":
					validChoice = true;
					System.out.println("Which one?");
					int choice = Integer.parseInt(in.readLine());
					User.addToContacts(searchResults.get(choice - 1).getContactInfo());
					Request addContactRequest = new Request(RequestType.ADD_CONTACT, User);
					addContactRequest.addParam(searchResults.get(choice - 1).getContactInfo());
					oos.writeObject(addContactRequest);
					oos.flush();
					System.out.println("Successfully added contact");
					break;
				case "n":
					validChoice = true;
					break;
				default:
					System.out.println("Not a valid choice");
					break;
				}
			}
		}
		else
		{
			System.out.println("Found no users matching these search parameters");
		}
	}
	
	public static void GetContacts(UserAccount User, BufferedReader in) throws IOException
	{
		boolean validContactList = false;
		if (User.getContacts().size() == 0)
		{
			System.out.println("No Contacts");
		}
		else
		{
			while (!validContactList)
			{
				int userNumber = 1;
				System.out.println("1.) ALL\n2.) NAME\n3.) PROGRAM\n4.) YEAR");
				switch(in.readLine())
				{
				case "all":
					validContactList = true;
					System.out.println("Contacts:");
					for (Contact c: User.getContacts())
					{
						System.out.println(userNumber + ".) " + c.getFirstName() + " " + c.getLastName());
						userNumber++;
					}
					break;
				case "name":
					validContactList = true;
					System.out.println("First name?");
					String firstName = in.readLine();
					System.out.println("Last name?");
					String lastName = in.readLine();
					System.out.println("Contacts:");
					for (Contact c: User.getContacts())
					{
						if (c.getFirstName().equals(firstName) && c.getLastName().equals(lastName))
						{
							System.out.println(userNumber + ".) " + c.getFirstName() + " " + c.getLastName());
							userNumber++;
						}
					}
					break;
				case "program":
					validContactList = true;
					Program program = null;
					boolean validProgramType = false;
					while(!validProgramType)
					{
						System.out.println("Program? (CS, IT, CpE, EE)");
						switch(in.readLine())
						{
						case "cs":
							validProgramType = true;
							program = Program.CS;
							break;
						case "it":
							validProgramType = true;
							program = Program.IT;
							break;
						case "cpe":
							validProgramType = true;
							program = Program.CpE;
							break;
						case "ee":
							validProgramType = true;
							program = Program.EE;
							break;
						default:
							System.out.println("Not a valid choice");
							break;
						}
					}
					
					System.out.println("Contacts:");
					for (Contact c: User.getContacts())
					{
						if (c.getProgram().equals(program))
						{
							System.out.println(userNumber + ".) " + c.getFirstName() + " " + c.getLastName());
							userNumber++;
						}
					}
					break;
				case "year":
					validContactList = true;
					Year year = null;
					boolean validYearType = false;
					while(!validYearType)
					{
						System.out.println("Year? (Freshman, Sophomore, Junior, Senior)");
						switch(in.readLine())
						{
						case "freshman":
							validYearType = true;
							year = Year.Freshman;
							break;
						case "sophomore":
							validYearType = true;
							year = Year.Sophomore;
							break;
						case "junior":
							validYearType = true;
							year = Year.Junior;
							break;
						case "senior":
							validYearType = true;
							year = Year.Senior;
							break;
						default:
							System.out.println("Not a valid choice");
							break;
						}
					}
					
					System.out.println("Contacts:");
					for (Contact c: User.getContacts())
					{
						if (c.getYear().equals(year))
						{
							System.out.println(userNumber + ".) " + c.getFirstName() + " " + c.getLastName());
							userNumber++;
						}
					}
					break;
				default:
					System.out.println("Not a valid choice");
					break;
				}
			}
		}
	}
	
	public static void SendMessage(BufferedReader in, UserAccount User, ObjectOutputStream oos) throws IOException
	{
		GetContacts(User, in);
		if (User.getContacts().size() > 0)
		{
			System.out.println("Which contact do you wish to send a message to?");
			int userNumber = Integer.parseInt(in.readLine());
			Contact userToGetMessage = User.getContacts().get(userNumber - 1);
			System.out.println("What do you want your message to say?");
			String messageContent = in.readLine();
			Message message = new Message(messageContent, userToGetMessage, User.getContactInfo());
			Request sendMessageRequest = new Request(RequestType.SEND_MESSAGE, User);
			sendMessageRequest.addParam(message);
			oos.writeObject(sendMessageRequest);
			oos.flush();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void GetMessages(UserAccount User, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException
	{
		Request getMessagesRequest = new Request(RequestType.GET_MESSAGES, User);
		oos.writeObject(getMessagesRequest);
		oos.flush();
		User.overwriteMessageLog((ArrayList<Message>) ois.readObject());
		System.out.println("Messages:");
		for (Message m: User.getMessageLog())
		{
			System.out.println(m.getMessage());
		}
		if (User.getMessageLog().size() == 0)
		{
			System.out.println("No Messages");
		}
	}
	
	public static void Logout(ObjectOutputStream oos, UserAccount User) throws IOException
	{
		Request LogoutRequest = new Request(RequestType.LOGOUT, User);
		oos.writeObject(LogoutRequest);
		oos.flush();
	}
	
	public static void Disconnect(ObjectOutputStream oos, UserAccount User) throws IOException
	{
		Request DisconnectRequest = new Request(RequestType.DISCONNECT, User);
		oos.writeObject(DisconnectRequest);
		oos.flush();
	}
	
	@SuppressWarnings("unchecked")
	public static void GetMessageLog(ObjectOutputStream oos, ObjectInputStream ois, UserAccount User) throws IOException, ClassNotFoundException
	{
		ArrayList<Message> messageLog = null;
		
		Request getMessageLogRequest = new Request(RequestType.VIEW_MESSAGE_LOG, User);
		oos.writeObject(getMessageLogRequest);
		messageLog = (ArrayList<Message>) ois.readObject();
		
		for (Message m: messageLog)
		{
			System.out.println(m.getMessage());
		}
		
		if (messageLog.size() == 0)
		{
			System.out.println("No messages in log");
		}
	}
}
