// Architecture
package hangmans.network;

// Java import
import java.io.ObjectInputStream;

// Class
public class Listener implements Runnable
{
	// Attributes
	private IO                io   ;
	private ObjectInputStream input;


	// Constructor
	public Listener(IO io, ObjectInputStream input)
	{
		this.io    = io   ;
		this.input = input;

		new Thread(this).start();
	}


	// Methods
	public void run()
	{
		try
		{
			while (true)
				this.io.available(this.input.readObject());
		}
		catch (Exception e){ this.io.disconnected(); }
	}
}