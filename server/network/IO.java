// Architecture
package hangmans.network;
import hangmans.model.Server;

// Java import
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

// Class
public class IO implements Runnable
{
	// Sequential number
	private static int SEQ_NUM = 0;


	// Attributes
	private Server server;

	private ObjectOutputStream output;
	private Object             buffer;

	private String name;


	// Constructor
	public IO(Server server, Socket s)
	{
		this.server = server;

		try
		{
			this.output      = new ObjectOutputStream(s.getOutputStream());
			new Listener(this, new ObjectInputStream (s.getInputStream ()));
		}
		catch (Exception e){ e.printStackTrace(); }

		new Thread(this).start();
	}


	// Getters
	public String getName(){ return this.name; }


	// Run methods
	public void run()
	{
		this.name = (String) this.receive();
		if (this.name == null)
			return;
		if (name.equals(""))
			this.name = "Anonymous" + (++IO.SEQ_NUM);

		this.server.addIO(this);
	}
	
	
	// IO methods
	public void send(Object o)
	{
		try
		{
			this.output.writeObject(o);
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public synchronized Object receive()
	{
		Object o = null;
		try
		{
			this.wait  ();
			o = this.buffer;
			this.notify();
		}
		catch (Exception e){ e.printStackTrace(); }
		return o;
	}

	public synchronized void available(Object o)
	{
		try
		{
			this.buffer = o;
			this.notify();
			this.wait(20);
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public void disconnected()
	{
		this.available(null);
		try
		{
			this.output.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		if (this.name != null)
			this.server.removeIO(this);
	}


	// toString method
	public String toString(){ return this.name; }
}