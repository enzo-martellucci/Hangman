// Architecture
package hangmanc;
import hangmanc.model.Client;
import hangmanc.view.ViewCUI;
import hangmanc.network.IO;

// Java import
import java.net.Socket;

// Class
public class Controller
{
	// Attributes
	private Client  client;
	private ViewCUI view  ;
	private IO      io    ;


	// Constructor
	private Controller(Socket s)
	{
		this.client = new Client () ;
		this.view   = new ViewCUI() ;
		this.io     = new IO     (this, s);

		this.sendName();
		this.printMessage();
	}


	// Methods
	private void sendName()
	{
		this.client.setName(this.view.enterName());
		this.io.send(this.client.getName());
	}

	private void printMessage()
	{
		String msg;

		do
		{
			msg = (String) this.io.receive();
			this.view.print(msg);
		}
		while (true);
	}

	public void disconnected()
	{
		System.out.println("Disconnected");
		System.exit(1);
	}


	// Main
	public static void main(String[] args)
	{
		try
		{
			new Controller(new Socket("localhost", 6000));
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}