//January 23, 2022
import java.io.*;
import java.net.*;

public class RemoteStrategy implements Strategy
{
  private static ServerSocket server;
  
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private String name;
  private Table table;
  
  public RemoteStrategy()
  {
    try
    {
      if (server == null)
        server = new ServerSocket(9216);
      socket = server.accept();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      name = in.readLine() + " (Remote)";
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public String getName()
  {
    return name;
  }

  public void gameStarted(Table table)
  {
    this.table = table;
    sendTable();
    out.println("gameStarted " + table.getSeat());
  }
  
  public Card[] playCards(int rank)
  {
    try
    {
      sendTable();
      out.println("playCards " + rank);
      String line = in.readLine();
      String[] tokens = line.split(" ");
      Card[] cards = new Card[tokens.length];
      for (int i = 0; i < cards.length; i++)
        cards[i] = new Card(tokens[i]);
      return cards;
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public boolean opponentPlayed(int opponentSeat, int numDiscarded, int rank)
  {
    try
    {
      sendTable();
      out.println("opponentPlayed " + opponentSeat + " " + numDiscarded + " " + rank);
      String line = in.readLine();
      if (line.equals("true"))
        return true;
      if (line.equals("false"))
        return false;
      throw new RuntimeException("invalid response to opponentPlayed:  \"" + line + "\"");
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying)
  {
    sendTable();
    String revealedString = "";
    for (Card card : cardsRevealed)
      revealedString += " " + card;
    out.println("liarCalled " + playerSeat + " " + rank + " " + callerSeat + " " + lying + revealedString);
  }
  
  private void sendTable()
  {
    String s = "hand";
    for (int i = 0; i < table.getNumCards(); i++)
      s += " " + table.getCard(i);
    out.println(s);
    s = "players";
    for (int seat = 0; seat < table.getNumPlayers(); seat++)
      s += " " + table.getNumCards(seat);
    out.println(s);
    out.println("discard " + table.getDiscardPileSize());
  }
}