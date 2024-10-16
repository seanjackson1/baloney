//January 23, 2022
public class LocalTable implements Table
{
  private Game game;
  private int seat;
  
  public LocalTable(Game game, int seat)
  {
    this.game = game;
    this.seat = seat;
  }
  
  public int getSeat()
  {
    return seat;
  }
  
  public int getNumCards()
  {
    return game.getPlayers()[seat].getHand().size();
  }
  
  public int getNumCards(int seat)
  {
    return game.getPlayers()[seat].getHand().size();
  }
  
  public Card getCard(int index)
  {
    return game.getPlayers()[seat].getHand().get(index);
  }
  
  public int getNumPlayers()
  {
    return game.getPlayers().length;
  }
  
  public int getDiscardPileSize()
  {
    return game.getDiscardPile().size();
  }
}