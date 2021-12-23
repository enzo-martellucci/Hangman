// Architecture
package hangmanc.view;

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
	public String enterName()
	{
		System.out.print("Name : ");
		return this.console.readLine();
	}

	public void print(String msg)
	{
		System.out.println(msg);
	}
}