package simple;

import java.io.IOException;
import java.net.ServerSocket;

/*** RUN SERVER FIRST. SERVER MUST BE RUNNING SO THAT CLIENT CONNECTIONS TO THE SERVER SOCKET CAN TAKE PLACE ***/
/** A server that accepts client connections
 * 
 * @author jnethery
 *
 */
public class Server {
	public static void main(String[] args)
	{
		boolean serverIsRunning = true;
		final int port = 1234;
		
		ServerSocket server = null;
		try {
			server = new ServerSocket(port, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Could not establish server on port " + port);
		} 
		
		while (serverIsRunning)
		{
			try {
				new Thread(new SimpleConverseServer(server.accept())).start();
				System.out.println("Accepted port activity");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Could not close server on port " + port);
		}
	}
}
