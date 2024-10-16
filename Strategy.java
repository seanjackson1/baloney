//January 23, 2022
public interface Strategy
{
  String getName();

  void gameStarted(Table table);
  
  Card[] playCards(int rank);
  
  boolean opponentPlayed(int opponentSeat, int numDiscarded, int claimedRank);
  
  void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying);
}