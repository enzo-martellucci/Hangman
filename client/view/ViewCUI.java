// Architecture
package hangmanc.view;
import hangmanc.Controller;
import hangmanc.model.Game;
import hangmanc.model.Player;

// Java import
import java.io.Console;

// Class
public class ViewCUI
{
	// Constants
	private static final String CLEAR        = "\033[H\033[2J";
	private static final String RESET        = "\u001B[0m"    ;
	private static final String CONNECTED    = "\u001B[42m"   ;
	private static final String DISCONNECTED = "\u001B[41m"   ;

	// Attributes
	private Controller ctrl;
	private Game       game;

	private Console console;


	// Constructor
	public ViewCUI(Controller ctrl, Game game)
	{
		this.ctrl = ctrl;
		this.game = game;

		this.console = System.console();
	}


	// Methods
	public String enterName()
	{
		System.out.print("Name : ");
		return this.console.readLine();
	}

	public void print(String msg)
	{
		System.out.println(msg);
	}

	public char enterLetter()
	{
		String letter;
		do
		{
			System.out.print("Enter letter : ");
			letter = this.console.readLine();
		}
		while (letter.length() != 1 || !Character.isLetterOrDigit(letter.charAt(0)));

		return Character.toUpperCase(letter.charAt(0));
	}

	public void printGame()
	{
		String state = ViewCUI.CLEAR;

		char   [] word  = this.game.getWord ();
		boolean[] found = this.game.getFound();

		Player[] lstPlayer = this.game.getLstPlayer();
		int      player    = this.game.getPlayer();

		for (int i = 0; i < word.length; i++)
			state += (found[i] ? word[i] : "_") + " ";
		state += "\n\n";

		for (int i = 0; i < lstPlayer.length; i++)
		{
			state += String.format("%s %-20s %s %s : ", (i == player ? "->" : "  "), lstPlayer[i].getName(), (lstPlayer[i].isConnected() ? ViewCUI.CONNECTED : ViewCUI.DISCONNECTED), ViewCUI.RESET );
			for (Character c : lstPlayer[i].getLetterTried())
				state += c + " ";
			state += "\n";
		}
		state += "\n";

		state += String.format("Errors : %d/%d", this.game.getError(), this.game.getMaxError());

		System.out.println(state);
	}

	public void disconnected()
	{
		this.print(ViewCUI.CLEAR + "Disconnected");
	}
}