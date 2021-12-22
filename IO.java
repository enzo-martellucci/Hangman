import java.net.Socket;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IO implements Runnable
{
	// Sequential number
	private static int SEQ_NUM = 0;


	// Attributes
	private Server server;

	private PrintWriter    output;
	private BufferedReader input ;

	private String name;


	// Constructor
	public IO(Server server, Socket s)
	{
		this.server = server;

		try
		{
			this.output = new PrintWriter   (s.getOutputStream(), true);
			this.input  = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch (Exception e){ e.printStackTrace(); }

		new Listener(this, this.input);
		new Thread(this).start();
	}


	// Getters
	public String getName(){ return this.name; }


	// Run methods
	public void run()
	{
		this.send("Name : ");
		this.name = this.receive();
		if (this.name == null)
			return;
		if (name.equals(""))
			this.name = "Anonymous" + (++IO.SEQ_NUM);

		this.server.addIO(this);
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

		if (this.name != null)
			this.server.removeIO(this);
	}


	// toString method
	public String toString(){ return this.name; }
}