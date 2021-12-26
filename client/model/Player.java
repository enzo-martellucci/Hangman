// Architecture
package hangmanc.model;

// Java import
import java.util.List;
import java.util.ArrayList;

// Class
public class Player
{
	// Attributes
	private String  name;
	private boolean connected;
	
	private List<Character> letterTried;


	// Constructor
	Player(String name)
	{
		this.name      = name;
		this.connected = true;

		this.letterTried = new ArrayList<Character>();
	}


	// Getters
	public String          getName       (){ return this.name       ; }
	public List<Character> getLetterTried(){ return this.letterTried; }
	public boolean         isConnected   (){ return this.connected  ; }


	// Methods
	void setConnected(boolean connected)
	{
		this.connected = connected;
	}
	
	void addLetter(char letter)
	{
		this.letterTried.add(letter);
	}
}