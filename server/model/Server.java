// Architecture
package hangmans.model;
import hangmans.network.Receiver;
import hangmans.network.IO;

// Java import
import java.util.List;
import java.util.ArrayList;

// Class
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


	// Getters
	public String getLstIO(){ return this.lstIO.toString(); }


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
	}

	public synchronized void addIO   (IO io){ this.lstIO.add(io)   ; this.notify(); }
	public synchronized void removeIO(IO io){ this.lstIO.remove(io); this.notify();	}


	// IO methods
	public void sendToAll(String msg)
	{
		for (IO io : this.lstIO)
			io.send(msg);
	}
}