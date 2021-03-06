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

	private Object             buffer;
	private ObjectOutputStream output;
	private ObjectInputStream  input ;

	private String name;


	// Constructor
	IO(Server server, Socket s)
	{
		this.server = server;

		try
		{
			this.output = new ObjectOutputStream(s.getOutputStream());
			this.input  = new ObjectInputStream (s.getInputStream ());
		}
		catch (Exception e){ e.printStackTrace(); }

		new Thread(this).start();
	}


	// Getters
	public String getName(){ return this.name; }


	// Setters
	private void setName(String name)
	{
		this.name = name.equals("") ? "Anonymous" + IO.SEQ_NUM++ : name;
		this.server.addPlayer(this);
	}


	// Listening methods
	public void run()
	{
		try
		{
			this.setName((String) this.input.readObject());
			while (true)
				this.available(this.input.readObject());
		}
		catch (Exception e){ this.close(); }
	}

	private synchronized void available(Object o)
	{
		this.buffer = o;
		this.notify();
		try{ this.wait(); } catch (Exception e){ e.printStackTrace(); }
	}
	
	
	// IO methods
	public void send(Object o)
	{
		try
		{
			this.output.writeObject(o);
		}
		catch (Exception e){}
	}

	public synchronized Object receive()
	{
		Object o;
		if (this.buffer == null)
			try{ this.wait(); } catch (Exception e){ e.printStackTrace(); }
		o = this.buffer;
		this.buffer = null;
		this.notify();
		return o;
	}

	private synchronized void close()
	{
		if (this.name != null)
			this.server.removePlayer(this);

		this.notify();
		try { this.input.close(); }
		catch (Exception e){ e.printStackTrace(); }
	}
}