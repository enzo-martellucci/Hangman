# How to play
## Description
This is a multiplayer Hangman game.

## How to launch the application
First you need to start the server with the command :
java -jar HangmanS.jar [port] [nbPlayer]

Then you must connect as many clients as expected, with the command :
java -jar HangmanC.jar [ip] [port]

You can now play.

## How do the words are choosen ?
Words are randomly choose in the file "wordlist.txt", if you want custom words, use this file and put one word by line (don't change the file name).

# Compile and execute manually
## Compile
Go in the src directory and run the commands :
javac @client.list -d "."
javac @server.list -d "."

## Execute
Run respectivly for server and client :
java hangmans.Controller [port] [nbPlayer]
java hangmanc.Controller [ip] [port]


# Programming details
## Improvements
- Enter options in a more securely than using executable arguments
- Maybe move the 2 utility methods of Server into a specific class
- Write documentation
- Create an executable instead of a .jar file

## Future features ?
- Add the possibility to try a word instead of a letter
- Add the possibility to choose a specific word list (instead of modifying the existing one)
- Create a GUI

## Known issues
- Sometime, at the end of the game, IO on server-side throw an exception because some players are removed of lstPlayer after testing the condition "this.nbPlayer != 0", so the server sends on closed IO (Thread synchronization problem). I removed the exception display only for this version.
- I don't understand why, but when the original lstConnected boolean array is sent, it only contains true values, so send a copy should just be a temporary fix.