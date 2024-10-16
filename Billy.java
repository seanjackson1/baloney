//January 23, 2022
public class Billy implements Strategy {
  private Table table;

  public String getName() {
    return "Billy Baloney";
  }

  public void gameStarted(Table table) {
    this.table = table;
  }

  public Card[] playCards(int rank) {
    Card[] cards = new Card[table.getNumCards()];
    for (int i = 0; i < table.getNumCards(); i++)
      cards[i] = table.getCard(i);
    return cards;
  }

  public boolean opponentPlayed(int opponentSeat, int numDiscarded, int discardedRank) {
    return false;
  }

  public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying) {
  }
}