import java.net.Socket;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IO
{
	// Attributes
	private Client client;

	private PrintWriter    output;
	private BufferedReader input ;


	// Constructor
	public IO(Client client, Socket s)
	{
		this.client = client;

		try
		{
			this.output = new PrintWriter   (s.getOutputStream(), true);
			this.input  = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch (Exception e){ e.printStackTrace(); }

		new Listener(this, this.input);
	}

	
	// IO methods
	public void send(String msg)
	{
		try
		{
			this.output.println(msg);
		}
		catch (Exception e){ e.printStackTrace(); }
	}

	public synchronized String receive()
	{
		String msg = null;
		try
		{
			this.wait  ();
			msg = this.input.readLine();
			this.notify();
		}
		catch (Exception e){ e.printStackTrace(); }
		return msg;
	}

	public synchronized void available()
	{
		try
		{
			// wait(20) to stop wait when io disconnected while not receiving
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
			this.input .close();
		}
		catch (Exception e){ e.printStackTrace(); }

		this.client.disconnected();
	}
}