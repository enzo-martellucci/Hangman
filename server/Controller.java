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
		this.initGame();
		this.play();
	}


	// Methods
	private void waitPlayer()
	{
		this.server.waitPlayer();
		this.view.print("All here, the game will start");
	}

	private void initGame()
	{
		this.server.initGame();
		this.view.print("Game initialized");
	}

	private void play()
	{
		this.server.play();
		this.view.print("All disconnected, server will shutdown");
	}


	// Main
	public static void main(String[] args)
	{
		new Controller(6000, 3);
	}
}