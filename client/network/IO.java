// Architecture
package hangmanc.network;
import hangmanc.Controller;

// Java import
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream ;

// Class
public class IO
{
	// Attributes
	private Controller ctrl;

	private ObjectOutputStream output;
	private Object             buffer;


	// Constructor
	public IO(Controller ctrl, Socket s)
	{
		this.ctrl = ctrl;

		try
		{
			this.output      = new ObjectOutputStream(s.getOutputStream());
			new Listener(this, new ObjectInputStream (s.getInputStream ()));
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
		try
		{
			this.output.close();
		}
		catch (Exception e){ e.printStackTrace(); }
		this.ctrl.disconnected();
	}
}