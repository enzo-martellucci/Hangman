import java.net.Socket;

public class Client
{
	// Attributes
	private IO io;


	// Constructor
	public Client(Socket s)
	{
		this.io = new IO(this, s);

		this.enterName();
		this.printMessage();
	}


	// Methods
	public void enterName()
	{
		System.out.print("Name : ");
		this.io.send(System.console().readLine());
	}

	public void printMessage()
	{
		String msg;

		while (true)
			System.out.println(this.io.receive());
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
			Client client = new Client(new Socket("localhost", 6000));
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}