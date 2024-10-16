//January 24, 2022
import java.util.*;

public class Game
{
  public static int play(Display display, Strategy[] strategies)
  {
    return new Game(display, strategies).play();
  }
  
  private Player[] players;
  private ArrayList<Card> discardPile;
  private ArrayList<Card> justPlayed;
  private Display display;
  
  private Game(Display display, Strategy ... strategies)
  {
    this.display = display;
    
    players = new Player[strategies.length];
    for (int seat = 0; seat < strategies.length; seat++)
      players[seat] = new Player(strategies[seat]);
    
    discardPile = new ArrayList<Card>();
    justPlayed = new ArrayList<Card>();
  }
  
  //returns winning seat #, or -1 if no one wins in max turns
  private int play()
  {
    //create and shuffle deck
    ArrayList<Card> deck = new ArrayList<Card>();
    for (int rank = 1; rank <= 13; rank++)
    {
      for (int suit = 0; suit <= 3; suit++)
        deck.add(new Card(rank, suit));
    }
    Collections.shuffle(deck);
    
    //deal all cards to players
    int seat = 0;
    while (deck.size() > 0)
    {
      players[seat].getHand().add(deck.remove(deck.size() - 1));
      seat = (seat + 1) % players.length;
    }
    
    //create discard pile and just played
    discardPile = new ArrayList<Card>();
    justPlayed = new ArrayList<Card>();
    
    //inform each player that the game is starting
    for (seat = 0; seat < players.length; seat++)
      players[seat].gameStarted(new LocalTable(this, seat));
    
    seat = 0;
    
    if (display != null)
      display.setGame(this);
    
    int turn = 0;
    while (turn < players.length * 100) // should rarely be more than 26 * players.length
    {
      //determine whose turn it is
      seat = turn % players.length;
      
      //determine what rank to play
      int rank = turn % 13 + 1;
      
      //announce whose turn and what they must play
      if (display != null)
        display.startOfTurn(seat, rank);
      
      //ask player to choose cards to play
      Card[] justPlayedArray = players[seat].playCards(rank);
      for (Card card : justPlayedArray)
        justPlayed.add(card);
      
      //show number of cards played
      if (display != null)
        display.discarded(seat, rank);
      
      //give everyone a chance to call
      int caller = (seat + 1) % players.length;
      boolean called = false;
      while (caller != seat)
      {
        called = players[caller].opponentPlayed(seat, justPlayed.size(), rank);
        if (called)
          break;
        
        caller = (caller + 1) % players.length;
      }
      
      if (called)
      {
        //called
        
        //announce that someone called
        if (display != null)
          display.liarCalled(seat, rank, caller, justPlayedArray);
        
        //check if lying
        boolean lying = !allCardsMatch(justPlayedArray, rank);
        
        //determine who picks up the cards
        int loser;
        if (lying)
          loser = seat;
        else
          loser = caller;
        
        if (display != null)
          display.caughtLying(seat, rank, caller);
        
        //check if game over
        
        //announce that liar was called and the outcome
        for (int i = 0; i < players.length; i++)
          players[i].liarCalled(seat, rank, caller, justPlayedArray, lying);
        
        //first move just played cards to losing player
        while (justPlayed.size() > 0)
        {
          players[loser].getHand().add(justPlayed.remove(justPlayed.size() - 1));
          
          if (display != null)
            display.updateAndPause();
        }
        
        //shuffle the discard so no one can work backward to determine earlier plays
        Collections.shuffle(discardPile);
        
        //move all cards from discardPile to losing player
        
        while (discardPile.size() > 0)
        {
          players[loser].getHand().add(discardPile.remove(discardPile.size() - 1));
          
          if (display != null)
            display.updateAndPause();  
        }
      }
      else
      {
        //no one called
        
        //move all cards from played to discardPile
        //first move all played cards into the discard
        while (justPlayed.size() > 0)
        {
          discardPile.add(justPlayed.remove(justPlayed.size() - 1));
          
          if (display != null)
            display.updateAndPause();
        }
      }
      
      //game ends if player ran out of cards
      if (players[seat].getHand().size() == 0)
      {
        if (display != null)
          display.showWinner(seat);
        return seat;
      }
      
      //game continues

      turn++;
    }
    
    return -1;
  }
  
  private static boolean allCardsMatch(Card[] cards, int rank)
  {
    for (Card card : cards)
    {
      if (card.getRank() != rank)
        return false;
    }
    return true;
  }
  
  public Player[] getPlayers()
  {
    return players;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < players.length; i++)
    {
      sb.append(i);
      sb.append(":  ");
      sb.append(players[i].toString());
      sb.append("\n");
    }
    sb.append("discard:  ");
    sb.append(discardPile.toString());
    sb.append("\n");
    return sb.toString();
  }
  
  public ArrayList<Card> getDiscardPile()
  {
    return discardPile;
  }
  
  public ArrayList<Card> getJustPlayed()
  {
    return justPlayed;
  }
}