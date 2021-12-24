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
		new Thread(this).start();
	}


	// Run methods
	public void run()
	{
		try
		{
			while (true)
				this.available(this.input.readObject());
		}
		catch (Exception e){ this.close(); }
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

	private synchronized void available(Object o)
	{
		this.buffer.offer(o);
		this.notify();
	}

	private void close()
	{
		this.ctrl.disconnected();
	}
}