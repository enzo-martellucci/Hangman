// Architecture
package hangmans;
import hangmans.model.Server;
import hangmans.view.ViewCUI;

// Class
public class Controller
{
	// Attributes
	private Server  server;
	private ViewCUI view  ;


	// Constructor
	private Controller(int port, int capacity)
	{
		this.server = new Server (port, capacity);
		this.view   = new ViewCUI();

		this.waitPlayer();
		this.initGame();
		this.play();

		this.view.print("All disconnected, server will stop");
	}


	// Methods
	private void waitPlayer()
	{
		this.view.print("Waiting for players");
		this.server.waitPlayer();
	}

	private void initGame()
	{
		this.view.print("All here, sending initial infos");
		this.server.initGame();
	}

	private void play()
	{
		this.view.print("Everything ready, the game begins");
		this.server.play();
	}


	// Main
	public static void main(String[] args)
	{
		new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}