// Architecture
package hangmans.view;

// Java import
import java.io.Console;

// Class
public class ViewCUI
{
	// Attributes
	private Console console;

	
	// Constructor
	public ViewCUI()
	{
		this.console = System.console();
	}


	// Methods
	public void print(String msg)
	{
		System.out.println(msg);
	}
}