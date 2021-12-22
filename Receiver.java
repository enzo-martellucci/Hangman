import java.net.Socket;
import java.net.ServerSocket;

public class Receiver implements Runnable
{
	// Attributes
	private Server server;

	private ServerSocket receiver;
	private int          port    ;
	private boolean      waiting ;


	// Constructor
	public Receiver(Server server, int port)
	{
		this.server  = server;
		this.port    = port  ;
		this.waiting = true  ;

		try
		{
			this.receiver = new ServerSocket(this.port);
		}
		catch (Exception e){ e.printStackTrace(); }
	}


	// Methods
	public void run()
	{
		Socket s;
		try
		{
			while (true)
			{
				s = this.receiver.accept();
				if (this.waiting)
					new IO(this.server, s);
				else
					break;
			}

			this.receiver.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public void close()
	{
		this.waiting = false;
		try
		{
			new Socket("localhost", this.port);
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}