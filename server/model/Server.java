// Architecture
package hangmans.model;
import hangmans.network.Receiver;
import hangmans.network.IO;

// Java import
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// Class
public class Server
{
	// Attributes
	private Receiver receiver;

	private IO     [] lstPlayer   ;
	private boolean[] lstConnected;
	private int nbPlayer;


	// Constructor
	public Server(int port, int capacity)
	{
		this.receiver = new Receiver(this, port);

		this.lstPlayer    = new IO     [capacity];
		this.lstConnected = new boolean[capacity];
		this.nbPlayer = 0;
	}
	

	// Getters
	public String getLstIO(){ return Arrays.toString(this.lstPlayer); }


	// Connection methods
	public synchronized void waitPlayer()
	{
		new Thread(this.receiver).start();
		try
		{
			while (this.nbPlayer != this.lstPlayer.length)
			{
				this.wait();
				this.sendToAll(String.format("Waiting players (%d/%d)", this.nbPlayer, this.lstPlayer.length));
			}
		}
		catch (Exception e){ e.printStackTrace(); }
		this.receiver.close();
	}

	public synchronized void addPlayer(IO player)
	{
		int i = Server.indexOf(this.lstPlayer, null);
		this.lstPlayer   [i] = player;
		this.lstConnected[i] = true;
		this.nbPlayer++;
		this.notify();
	}

	public synchronized void removePlayer(IO player)
	{
		int i = Server.indexOf(this.lstPlayer, player);
		this.lstPlayer   [i] = null ;
		this.lstConnected[i] = false;
		this.nbPlayer--;
		this.notify();
	}


	// IO methods
	public void sendToAll(Object o)
	{
		for (int i = 0; i < this.lstPlayer.length; i++)
			if (this.lstConnected[i])
				this.lstPlayer[i].send(o);
	}


	// Utilitary methods
	private static int indexOf(Object[] array, Object target)
	{
		int i = 0;
		while (array[i] != target) i++;
		return i;
	}
}