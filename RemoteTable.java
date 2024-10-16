//January 23, 2022
import java.io.*;
import java.net.*;

public class RemoteTable extends Thread implements Table
{
  private Strategy strategy;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private int seat;
  private Card[] hand;
  private int[] players;
  private int discardPileSize;
  
  public RemoteTable(String hostIPAddress, Strategy strategy)
  {
    try
    {
      this.strategy = strategy;
      socket = new Socket(hostIPAddress, 9216);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      out.println(strategy.getName());
      start();
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void run()
  {
    try
    {
      while (true)
      {
        String line = in.readLine();
        String[] tokens = line.split(" ");
        String command = tokens[0];
        if (command.equals("opponentPlayed"))
        {
          int opponentSeat = Integer.parseInt(tokens[1]);
          int numDiscarded = Integer.parseInt(tokens[2]);
          int rank = Integer.parseInt(tokens[3]);
          boolean call = strategy.opponentPlayed(opponentSeat, numDiscarded, rank);
          out.println(call);
        }
        else if (command.equals("hand"))
        {
          hand = new Card[tokens.length - 1];
          for (int i = 0; i < hand.length; i++)
            hand[i] = new Card(tokens[i + 1]);
        }
        else if (command.equals("players"))
        {
          players = new int[tokens.length - 1];
          for (int i = 0; i < players.length; i++)
            players[i] = Integer.parseInt(tokens[i + 1]);
        }
        else if (command.equals("discard"))
          discardPileSize = Integer.parseInt(tokens[1]);
        else if (command.equals("gameStarted"))
        {
          seat = Integer.parseInt(tokens[1]);
          strategy.gameStarted(this);
        }
        else if (command.equals("liarCalled"))
        {
          int playerSeat = Integer.parseInt(tokens[1]);
          int rank = Integer.parseInt(tokens[2]);
          int callerSeat = Integer.parseInt(tokens[3]);
          boolean lying;
          if (tokens[4].equals("true"))
            lying = true;
          else if (tokens[4].equals("false"))
            lying = false;
          else
            throw new RuntimeException("invalid lying string:  " + tokens[4]);
          Card[] cardsRevealed = new Card[tokens.length - 5];
          for (int i = 0; i < cardsRevealed.length; i++)
            cardsRevealed[i] = new Card(tokens[i + 5]);
          strategy.liarCalled(playerSeat, rank, callerSeat, cardsRevealed, lying);
        }
        else if (command.equals("playCards"))
        {
          int rank = Integer.parseInt(tokens[1]);
          Card[] cards = strategy.playCards(rank);
          String s = cards[0].toString();
          for (int i = 1; i < cards.length; i++)
            s += " " + cards[i];
          out.println(s);
        }
        else
          throw new RuntimeException("invalid command:  " + command);
      }
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public int getSeat()
  {
    return seat;
  }
  
  public int getNumCards()
  {
    return hand.length;
  }
  
  public int getNumCards(int seat)
  {
    return players[seat];
  }
  
  public Card getCard(int index)
  {
    return hand[index];
  }
  
  public int getNumPlayers()
  {
    return players.length;
  }
  
  public int getDiscardPileSize()
  {
    return discardPileSize;
  }
}