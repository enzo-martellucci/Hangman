// Architecture
package hangmans.network;
import hangmans.model.Server;

// Java import
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Queue;
import java.util.LinkedList;

// Class
public class IO implements Runnable
{
	// Sequential number
	private static int SEQ_NUM = 0;


	// Attributes
	private Server server;

	private Queue<Object>      buffer;
	private ObjectOutputStream output;
	private ObjectInputStream  input ;

	private String name;


	// Constructor
	public IO(Server server, Socket s)
	{
		this.server = server;
		try
		{
			this.buffer = new LinkedList<Object>();
			this.output = new ObjectOutputStream(s.getOutputStream());
			this.input  = new ObjectInputStream (s.getInputStream ());
		}
		catch (Exception e){ e.printStackTrace(); }
		new Thread(this).start();
	}


	// Getters
	public String getName(){ return this.name; }


	// Setters
	public void setName(String name)
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
		this.buffer.offer(o);
		this.notify();
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
		if (this.buffer.peek() == null)
			try{ this.wait(); } catch (Exception e){ e.printStackTrace(); }
		return this.buffer.poll();
	}

	private void close()
	{
		if (this.name != null)
			this.server.removePlayer(this);
		try
		{
			this.available(null);
			this.input .close();
			this.output.close();
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}