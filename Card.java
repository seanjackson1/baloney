//January 28, 2022
public class Card implements Comparable<Card>
{
  public static final int CLUBS = 0;
  public static final int DIAMONDS = 1;
  public static final int HEARTS = 2;
  public static final int SPADES = 3;
  
  private static String RANK_STRING = "0A23456789TJQKA";
  private static String SUIT_STRING = "cdhs";
  private static final String[] RANK_NAMES = {null, "ace", "two", "three", "four", "five", "six", "seven",
    "eight", "nine", "ten", "jack", "queen", "king"};
    
  private int rank;
  private int suit;
  
  public Card(int rank, int suit)
  {
    this.rank = rank;
    this.suit = suit;
    ensureValid();
  }
  
  public Card(String s)
  {
    if (s.length() != 2)
      throw new RuntimeException("invalid Card string:  \"" + s + "\"");
    rank = RANK_STRING.indexOf(s.substring(0, 1));
    suit = SUIT_STRING.indexOf(s.substring(1));
    ensureValid();
  }
  
  private void ensureValid()
  {
    if (rank < 1 || rank > 13)
      throw new RuntimeException("invalid rank:  " + rank);
    if (suit < 0 || suit > 3)
      throw new RuntimeException("invalid suit:  " + suit);
  }
  
  public int getRank()
  {
    return rank;
  }
  
  public int getSuit()
  {
    return suit;
  }
  
  public boolean equals(Object obj)
  {
    Card other = (Card)obj;
    return rank == other.getRank() && suit == other.getSuit();
  }
  
  public int hashCode()
  {
    return rank * 4 + suit;
  }
  
  public int compareTo(Card other)
  {
    if (rank == other.getRank())
      return suit - other.getSuit();
    else
      return rank - other.getRank();
  }
  
  public static String toRankString(int rank)
  {
    return RANK_STRING.substring(rank, rank + 1);
  }
  
  public static String toSuitString(int suit)
  {
    return SUIT_STRING.substring(suit, suit + 1);
  }
  
  public String toString()
  {
    return toRankString(rank) + toSuitString(suit);
  }
  
  public static String toRankName(int rank)
  {
    return RANK_NAMES[rank];
  }
  
  public static String toPluralRankName(int rank)
  {
    String s = RANK_NAMES[rank];
    if (rank == 6)
      return s + "es";
    else
      return s + "s";
  }
}