// Architecture
package hangmanc.model;

// Class
public class Game
{
	// Constants
	private static final int MAX_ERROR = 10;


	// Attributes
	private Player[] lstPlayer;
	private int      player   ;

	private char   [] word ;
	private boolean[] found;
	private int       error;

	private boolean gameOver;
	private boolean win     ;


	// Constructor method
	public void init(String rawWord, String[] lstName)
	{
		// Init players
		this.lstPlayer = new Player[lstName.length];
		for (int i = 0; i < this.lstPlayer.length; i++)
			this.lstPlayer[i] = new Player(lstName[i]);
		
		// Init word
		this.word  = rawWord.toCharArray();
		this.found = new boolean[this.word.length];
	}


	// Getters
	public Player[] getLstPlayer(){ return this.lstPlayer; }
	public int      getPlayer   (){ return this.player   ; }

	public char   [] getWord    (){ return this.word     ; }
	public boolean[] getFound   (){ return this.found    ; }
	public int       getError   (){ return this.error    ; }
	public int       getMaxError(){ return Game.MAX_ERROR; }

	public boolean isOver(){ return this.gameOver; }
	public boolean isWin (){ return this.win     ; }


	// Methods
	public void majConnected(boolean[] lstConnected)
	{
		for (int i = 0; i < this.lstPlayer.length; i++)
			this.lstPlayer[i].setConnected(lstConnected[i]);
	}

	public void setPlayer(int player)
	{
		this.player = player;
	}

	public void play(char letter)
	{
		boolean contain = false;
		for (int i = 0; i < this.word.length; i++)
			if (this.word[i] == letter)
				this.found[i] = contain = true;
		
		if (!contain)
		{
			this.error++;
			this.lstPlayer[this.player].addLetter(letter);
		}

		if (this.error == Game.MAX_ERROR)
		{
			this.gameOver = true;
			this.win      = false;
			return;
		}

		for (int i = 0; i < this.word.length; i++)
			if (!this.found[i])
				return;
		
		this.gameOver = this.win = true;
	}
}