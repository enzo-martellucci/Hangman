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

	private boolean waiting;


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
		this.waiting = true;
		new Thread(this).start();
	}


	// Listening methods
	public void run()
	{
		Object o;
		try
		{
			while (true)
			{
				o = this.input.readObject(); 
				if (this.waiting)
					this.available(o);
				else
					break;
			}
		}
		catch (Exception e){ this.ctrl.disconnected(); }
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

	public void close(){ this.waiting  = false; }
}