package simple;

import java.util.ArrayList;
import java.io.*;

/*** USE CREATE DATABASE TO CREATE THE SERVER-SIDE USERACCOUNT DATABASE.
	THIS DATABASE MUST BE INITIALIZED FOR THE SERVER TO FUNCTION ***/
public class CreateMessageLog {
	public static void main(String[] args) throws IOException
	{
		ArrayList<Message> blankMessageLog = new ArrayList<Message>();
		FileOutputStream fos = new FileOutputStream("MessageLog.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(blankMessageLog);
		fos.close();
		oos.close();
	}
}
