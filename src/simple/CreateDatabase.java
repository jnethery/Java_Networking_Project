package simple;

import java.util.ArrayList;
import java.io.*;

/*** USE CREATE DATABASE TO CREATE THE SERVER-SIDE USERACCOUNT DATABASE.
	THIS DATABASE MUST BE INITIALIZED FOR THE SERVER TO FUNCTION ***/
/** Used to create a new database for a server
 * 
 * @author jnethery
 *
 */
public class CreateDatabase {
	public static void main(String[] args) throws IOException
	{
		ArrayList<UserAccount> blankUserList = new ArrayList<UserAccount>();
		FileOutputStream fos = new FileOutputStream("UserDB.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(blankUserList);
		fos.close();
		oos.close();
	}
}
