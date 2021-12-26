// Architecture
package hangmanc.network;
import hangmanc.Controller;

// Java import
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Queue;
import java.util.LinkedList;

// Class
public class IO implements Runnable
{
	// Attributes
	private Controller ctrl;

	private Queue<Object>      buffer;
	private ObjectOutputStream output;
	private ObjectInputStream  input ;

	private boolean running;


	// Constructor
	public IO(Controller ctrl, Socket s)
	{
		this.ctrl = ctrl;
		try
		{
			this.buffer = new LinkedList<Object>();
			this.output = new ObjectOutputStream(s.getOutputStream());
			this.input  = new ObjectInputStream (s.getInputStream ());
		}
		catch (Exception e){ e.printStackTrace(); }
		this.running = true;
		new Thread(this).start();
	}


	// Listening methods
	public void run()
	{
		try
		{
			while (this.running)
				this.available(this.input.readObject());
		}
		catch (Exception e){ this.ctrl.disconnected(); }
	}

	private synchronized void available(Object o)
	{
		this.buffer.offer(o);
		try
		{
			this.notify();
			this.wait  ();
		}
		catch (Exception e){ e.printStackTrace(); }
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
		Object o;
		if (this.buffer.peek() == null)
			try{ this.wait(); } catch (Exception e){ e.printStackTrace(); }
		o = this.buffer.poll();
		this.notify();
		return o;
	}

	public synchronized void close()
	{
		this.running = false;
		this.notify();
	}
}