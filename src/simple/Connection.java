package simple;

import java.net.Socket;
import java.io.ObjectOutputStream;

public class Connection {
	
	public Socket socket;
	public ObjectOutputStream oos;
	
	public Connection(Socket socket, ObjectOutputStream oos)
	{
		this.socket = socket;
		this.oos = oos;
	}
}
