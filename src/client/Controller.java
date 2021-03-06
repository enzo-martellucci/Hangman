// Architecture
package hangmanc;
import hangmanc.model.Game;
import hangmanc.view.ViewCUI;
import hangmanc.network.IO;

// Java import
import java.net.Socket;

// Class
public class Controller
{
	// Attributes
	private Game    game;
	private ViewCUI view;
	private IO      io  ;

	private int followed;


	// Constructor
	private Controller(Socket s)
	{
		this.game = new Game   () ;
		this.view = new ViewCUI(this, this.game) ;
		this.io   = new IO     (this, s);

		this.sendName  ();
		this.waitPlayer();
		this.initGame  ();
		this.play      ();
		this.end       ();

		this.io.close();
	}


	// Methods
	private void sendName()
	{
		this.io.send(this.view.enterName());
	}

	private void waitPlayer()
	{
		String msg = (String) this.io.receive();
		do
		{
			this.view.print(msg);
			msg = (String) this.io.receive();
		}
		while (!msg.equals("START"));
	}

	private void initGame()
	{
		this.followed = (int) this.io.receive();

		String   rawWord = (String  ) this.io.receive();
		String[] lstName = (String[]) this.io.receive();

		this.game.init(rawWord, lstName);
	}

	private void play()
	{
		Character letter;
		while (!this.game.isOver())
		{
			this.game.majConnected((boolean[]) this.io.receive());
			this.game.setPlayer   ((int      ) this.io.receive());
			this.view.printGame();

			if (this.game.getPlayer() == this.followed)
				this.io.send(this.view.enterLetter());
			else
				this.view.print("Waiting others to play");

			letter = (Character) this.io.receive();
			if (letter != null)
				this.game.play(letter);
		}
	}

	private void end()
	{
		this.view.printGame();
		this.view.print(this.game.isWin() ? "You win !!!" : "You loose =(");
	}

	public void disconnected()
	{
		this.view.disconnected();
		System.exit(1);
	}


	// Main
	public static void main(String[] args)
	{
		try
		{
			new Controller(new Socket(args[0], Integer.parseInt(args[1])));
		}
		catch (Exception e){ e.printStackTrace(); }
	}
}