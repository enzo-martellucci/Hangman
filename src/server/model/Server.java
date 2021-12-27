// Architecture
package hangmans.model;
import hangmans.network.Receiver;
import hangmans.network.IO;

// Java import
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Scanner;
import java.io.FileInputStream;

// Class
public class Server
{
	// Attributes
	private Receiver receiver;

	private IO     [] lstPlayer   ;
	private boolean[] lstConnected;
	private int nbPlayer;


	// Constructor
	public Server(int port, int capacity)
	{
		this.receiver = new Receiver(this, port);

		this.lstPlayer    = new IO     [capacity];
		this.lstConnected = new boolean[capacity];
		this.nbPlayer = 0;
	}


	// Connection methods
	public synchronized void waitPlayer()
	{
		new Thread(this.receiver).start();
		try
		{
			while (this.nbPlayer != this.lstPlayer.length)
			{
				this.wait();
				this.sendToAll(String.format("Waiting players (%d/%d)", this.nbPlayer, this.lstPlayer.length));
			}
		}
		catch (Exception e){ e.printStackTrace(); }
		this.sendToAll("START");
		this.receiver.close();
	}

	public synchronized void addPlayer(IO player)
	{
		int i = Server.indexOf(this.lstPlayer, null);
		this.lstPlayer   [i] = player;
		this.lstConnected[i] = true;
		this.nbPlayer++;
		this.notify();
	}

	public synchronized void removePlayer(IO player)
	{
		int i = Server.indexOf(this.lstPlayer, player);
		this.lstPlayer   [i] = null ;
		this.lstConnected[i] = false;
		this.nbPlayer--;
		this.notify();
	}

	// Init methods
	public void initGame()
	{
		String   rawWord = Server.randomWord();
		String[] lstName = new String[this.lstPlayer.length];

		for (int i = 0; i < this.lstPlayer.length; i++)
		{
			this.lstPlayer[i].send(i);
			lstName[i] = this.lstPlayer[i].getName();
		}
		
		this.sendToAll(rawWord);
		this.sendToAll(lstName);
	}


	// Play methods
	public void play()
	{
		int player = 0;
		while (this.nbPlayer != 0)
		{
			this.sendToAll(Arrays.copyOf(this.lstConnected, this.lstConnected.length));
			this.sendToAll(player);
			this.sendToAll(this.lstPlayer[player].receive());

			do
				player = (player + 1) % this.lstPlayer.length;
			while (this.lstPlayer[player] == null && this.nbPlayer != 0);
		}
	}


	// IO methods
	private void sendToAll(Object o)
	{
		for (int i = 0; i < this.lstPlayer.length; i++)
			if (this.lstConnected[i])
				this.lstPlayer[i].send(o);
	}


	// Utilitary methods
	private static int indexOf(Object[] array, Object target)
	{
		int i = 0;
		while (array[i] != target) i++;
		return i;
	}

	private static String randomWord()
	{
		List<String> lstWord = new ArrayList<String>();
		try(Scanner sc = new Scanner(new FileInputStream("wordlist.txt")))
		{
			while (sc.hasNext())
				lstWord.add(sc.nextLine());
		}
		catch (Exception e){ e.printStackTrace(); }
		return lstWord.get((int)(Math.random() * lstWord.size()));
	}
}