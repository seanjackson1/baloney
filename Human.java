//January 23, 2022
public class Human implements Strategy
{
  private Display display;
  private Table table;
  
  public Human(Display display)
  {
    this.display = display;
  }
  
  public void gameStarted(Table table)
  {
    this.table = table;
  }
  
  public Card[] playCards(int rank)
  {
    return display.playCards(table.getSeat(), rank);
  }
  
  public boolean opponentPlayed(int opponentSeat, int numDiscarded, int rank)
  {
    return display.opponentPlayed(opponentSeat, numDiscarded, rank);
  }
  
  public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean wasLying)
  {
  }
  
  public String getName()
  {
    return "You";
  }
}