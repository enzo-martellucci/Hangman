import java.io.BufferedReader;

public class Listener implements Runnable
{
	// Attributes
	private IO             io   ;
	private BufferedReader input;


	// Constructor
	public Listener(IO io, BufferedReader input)
	{
		this.io    = io   ;
		this.input = input;

		new Thread(this).start();
	}


	// Methods
	public void run()
	{
		boolean connected = true;

		try
		{
			while (connected)
			{
				this.input.mark(1);
				if (this.input.read() == -1)
					connected = false;
				this.input.reset();
				this.io.available();
			}

			this.io.disconnected();
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}