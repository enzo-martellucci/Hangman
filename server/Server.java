import java.util.List;
import java.util.ArrayList;

public class Server
{
	// Attributes
	private Receiver receiver;
	private int      capacity;

	private List<IO> lstIO;


	// Constructor
	public Server(int port, int capacity)
	{
		this.receiver = new Receiver(this, port);
		this.capacity = capacity;

		this.lstIO = new ArrayList<IO>();
	}


	// Connection methods
	public synchronized void waitPlayer()
	{
		new Thread(this.receiver).start();
		try
		{
			while (this.lstIO.size() != this.capacity)
			{
				this.wait();
				this.sendToAll(String.format("Waiting players (%d/%d)", this.lstIO.size(), this.capacity));
			}
		}
		catch (Exception e){ e.printStackTrace(); }
		this.receiver.close();

		System.out.println("All here : " + this.lstIO);
	}

	public synchronized void addIO   (IO io){ this.lstIO.add(io)   ; this.notify(); }
	public synchronized void removeIO(IO io){ this.lstIO.remove(io); this.notify();	}


	// IO methods
	public void sendToAll(String msg)
	{
		for (IO io : this.lstIO)
			io.send(msg);
	}


	// Main
	public static void main(String[] args)
	{
		Server s = new Server(6000, 3);
		
		s.waitPlayer();
		while (true)
			s.sendToAll(System.console().readLine());
	}
}