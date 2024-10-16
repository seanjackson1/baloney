//January 23, 2022
public interface Table
{
  int getSeat();
  
  int getNumCards();
  
  int getNumCards(int seat);
  
  Card getCard(int index);
  
  int getNumPlayers();
  
  int getDiscardPileSize();
}