// Architecture
package hangmans;
import hangmans.model.Server;
import hangmans.view.ViewCUI;

// Class
public class Controller
{
	// Attributes
	private Server  server;
	private ViewCUI view;


	// Constructor
	private Controller(int port, int capacity)
	{
		this.server = new Server (port, capacity);
		this.view   = new ViewCUI();

		this.waitPlayer();
	}


	// Methods
	private void waitPlayer()
	{
		this.server.waitPlayer();
		this.view.print("All here : " + this.server.getLstIO());
	}


	// Main
	public static void main(String[] args)
	{
		new Controller(6000, 3);
	}
}