package simple;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RequestType type;

	//contactlist, src, useracc
	private UserAccount user;
	
	private ArrayList<Object> params = new ArrayList<Object>();
	//add contact:
	//Contact contact
	
	
	public Request(RequestType type, UserAccount user)
	{
		this.type = type;
		this.user = user;
	}
	//send message:
	//String message
	//Contact destination
	//GregorianCalendar timestamp
	
	//login:
	//String password (md5)
	
	/**
	 * @return the type
	 */
	public RequestType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RequestType type) {
		this.type = type;
	}
	
	/**
	 * @return the user
	 */
	public UserAccount getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Object getParam(int pos){
		return params.get(pos);
	}
	
	public void addParam(Object value){
		params.add(value);
	}
}
