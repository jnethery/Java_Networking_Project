package simple;

/** Enumeration of different search types
 * 
 * @author jnethery
 *
 */
public enum SearchType {
	Type, //Type SearchType has 2 request parameters: 0: SearchType.Type, 1: AccountType.type;
	Name, //Name SearchType has 3 Request parameters: 0: SearchType.Name, 1: String firstName, 2: String lastName
	Year, //Year SearchType has 2 Request parameters: 0: SearchType.Year, 1: Year.year
	Program, //Program SearchType has 2 Request parameters: 0: SearchType.Program, 1: Program.program
	PID, //PID SearchType has 2 Request parameters: 0: SearchType.PID, 1: String PID
	ALL; //ALL SearchType has 1 Request parameters: 0: SearchType.ALL
}
