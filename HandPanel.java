//January 28, 2022
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class HandPanel extends JComponent implements MouseListener
{
  private static final int SMALL_CARD_WIDTH = 25;
  private static final int SMALL_CARD_HEIGHT = 35;
  private static final Font SMALL_CARD_FONT = new Font(null, Font.BOLD, 14);
  private static final int SMALL_SUIT_SIZE = 10;

  private static final int LARGE_CARD_WIDTH = 50;
  private static final int LARGE_CARD_HEIGHT = 70;
  private static final Font LARGE_CARD_FONT = new Font(null, Font.BOLD, 28);
  private static final int LARGE_SUIT_SIZE = 20;

  private static final int GAP = 5;
  private static final int BACK_INSET = 2;
  
  private static Image[] suitImages;
  
  static
  {
    String[] suitFileNames = {"club.png", "diamond.png", "heart.png", "spade.png"};
    suitImages = new Image[4];
    for (int i = 0; i < 4; i++)
    {
      String fileName = suitFileNames[i];
      URL url = HandPanel.class.getResource(fileName);
      if (url == null)
        throw new RuntimeException("file not found:  " + fileName);
      suitImages[i] = new ImageIcon(url).getImage(); 
    }
  }
  
  private int numRows;
  private int numCols;
  private ArrayList<Card> cards;
  private boolean showCards;
  private int cardWidth;
  private int cardHeight;
  private Font cardFont;
  private boolean[] selected;
  private int rank;
  private int suitSize;
  
  public HandPanel(int numRows, int numCols, boolean showCards, int selectedRank, boolean bigCards)
  {
    this.rank = selectedRank;
    
    if (bigCards)
    {
      cardWidth = LARGE_CARD_WIDTH;
      cardHeight = LARGE_CARD_HEIGHT;
      cardFont = LARGE_CARD_FONT;
      suitSize = LARGE_SUIT_SIZE;
    }
    else
    {
      cardWidth = SMALL_CARD_WIDTH;
      cardHeight = SMALL_CARD_HEIGHT;
      cardFont = SMALL_CARD_FONT;
      suitSize = SMALL_SUIT_SIZE;
    }
    
    this.numRows = numRows;
    this.numCols = numCols;
    this.showCards = showCards;
    
    setPreferredSize(new Dimension(numCols * cardWidth + (numCols + 1) * GAP,
                                   numRows * cardHeight + (numRows + 1) * GAP));
    
    if (selectedRank != 0)
      addMouseListener(this);
  }
  
  public void setShowCards(boolean showCards)
  {
    this.showCards = showCards;
    repaint();
  }
  
  public void paintComponent(Graphics g)
  {
    if (cards == null)
      return;
    
    ((Graphics2D)g).setStroke(new BasicStroke(5));
    
    int col = 0;
    int x = GAP;
    int y = GAP;
    g.setFont(cardFont);
    FontMetrics fm = g.getFontMetrics();
    int fontHeight = fm.getHeight();
    int howMany;
    for (int i = 0; i < cards.size(); i++)
    {
      if (showCards)
      {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, cardWidth, cardHeight);
        Card card = cards.get(i);
        int suit = card.getSuit();
        if (suit == Card.DIAMONDS || suit == Card.HEARTS)
          g.setColor(Color.RED);
        else
          g.setColor(Color.BLACK);
        String rankString = Card.toRankString(card.getRank());
        if (rankString.equals("T"))
          rankString = "10";
        int halfHeight = cardHeight / 2;
        g.drawString(rankString,
                     x + (cardWidth - fm.stringWidth(rankString)) / 2,
                     y + (halfHeight - fontHeight) / 2 + fontHeight);
        
        int suitWidth = suitSize;
        int suitHeight = suitSize;
        g.drawImage(suitImages[suit], 
                    x + (cardWidth - suitWidth) / 2,
                    y + (cardHeight + cardHeight / 2 - suitHeight) / 2,
                    suitWidth,
                    suitHeight,
                    null);
        
        if (i < selected.length && selected[i])
        {
          //System.out.println("drawing rectangle for card " + i);
          g.setColor(Color.BLUE);
          g.drawRect(x, y, cardWidth, cardHeight);
        }
      }
      else
      {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, cardWidth, cardHeight);
        g.setColor(Color.BLUE);
        g.fillRect(x + BACK_INSET,
                   y + BACK_INSET,
                   cardWidth - 2 * BACK_INSET,
                   cardHeight - 2 * BACK_INSET);
      }
      
      x += cardWidth + GAP;
      col++;
      if (col == numCols)
      {
        col = 0;
        x = GAP;
        y += cardHeight + GAP;
      }
    }
  }
  
  public void setCards(ArrayList<Card> cards)
  {
    this.cards = cards;
    selected = new boolean[cards.size()];
    for (int i = 0; i < selected.length; i++)
      selected[i] = (rank != 0 && cards.size() == 1) || cards.get(i).getRank() == rank;
  }
  
  public void mouseClicked(MouseEvent e)
  {
  }
  
  public void mousePressed(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
//    System.out.println("pressed " + x + ", " + y);
    int col = x / (cardWidth + GAP);
    int row = y / (cardHeight + GAP);
    int index = row * numCols + col;
//    System.out.println("row = " + row + ", col = " + col + ", index = " + index);
    if (index < selected.length)
    {
      selected[index] = !selected[index];
      repaint();
    }
  }
  
  public void mouseReleased(MouseEvent e)
  {
  }
  
  public void mouseEntered(MouseEvent e)
  {
  }
  
  public void mouseExited(MouseEvent e)
  {
  }
  
  public boolean[] getSelected()
  {
    return selected;
  }
  
  public ArrayList<Card> getCards()
  {
    return cards;
  }
}