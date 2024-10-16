//January 23, 2022
import java.util.*;

public class Player
{
  private Strategy strategy;
  private ArrayList<Card> hand;
  
  public Player(Strategy strategy)
  {
    this.strategy = strategy;
    hand = new ArrayList<Card>();
  }
  
  public void gameStarted(Table table)
  {
    strategy.gameStarted(table);
  }
  
  public Card[] playCards(int rank)
  {
    Card[] discard = strategy.playCards(rank);
    if (discard == null)
      throw new RuntimeException(strategy + " playCards method returned null");
    if (discard.length == 0)
      throw new RuntimeException(strategy + " playCards method returned empty array");
    for (Card card : discard)
    {
      if (!hand.remove(card))
        throw new RuntimeException(strategy + " playCards method played card " + card + " not in hand");
    }
    return discard;
  }
  
  public boolean opponentPlayed(int playerIndex, int numDiscarded, int rank)
  {
    return strategy.opponentPlayed(playerIndex, numDiscarded, rank);
  }
  
  public ArrayList<Card> getHand()
  {
    return hand;
  }
  
  public String toString()
  {
    return hand.toString();
  }
  
  public void liarCalled(int playerIndex, int rank, int callerIndex, Card[] cardsRevealed, boolean wasLying)
  {
    Card[] copy = new Card[cardsRevealed.length];
    for (int i = 0; i < cardsRevealed.length; i++)
      copy[i] = cardsRevealed[i];
    strategy.liarCalled(playerIndex, rank, callerIndex, copy, wasLying);
  }
  
  public String getName()
  {
    return strategy.getName();
  }
  
  public Strategy getStrategy()
  {
    return strategy;
  }
}