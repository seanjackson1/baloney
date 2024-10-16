//January 23, 2022
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class HandDialog extends JDialog implements ActionListener
{
  private HandPanel handPanel;
  private ArrayList<Card> hand;
  private Card[] selectedCards;
  
  public HandDialog(JFrame owner, ArrayList<Card> hand, int rank)
  {
    super(owner, "", true);
    
    this.hand = hand;
    
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    
    String rankName = Card.toPluralRankName(rank);
    rankName = rankName.substring(0, 1).toUpperCase() + rankName.substring(1);
    setTitle("Play " + rankName);
    
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

    int maxCols = 13;
    int numRows = (hand.size() + maxCols - 1) / maxCols;
    int numCols = Math.min(hand.size(), maxCols);
    handPanel = new HandPanel(numRows, numCols, true, rank, true);
    handPanel.setCards(hand);
    getContentPane().add(handPanel);
    
    JButton button = new JButton("OK");
    button.addActionListener(this);
    getContentPane().add(button);
    
    pack();
  }
  
  public Card[] getCards()
  {
    setVisible(true);
    return selectedCards;
  }
  
  public void actionPerformed(ActionEvent e)
  {
    boolean[] selected = handPanel.getSelected();
    ArrayList<Card> list = new ArrayList<Card>();
    for (int i = 0; i < hand.size(); i++)
    {
      if (selected[i])
        list.add(hand.get(i));
    }
    if (list.size() == 0)
      return;
    selectedCards = new Card[list.size()];
    for (int i = 0; i < list.size(); i++)
      selectedCards[i] = list.get(i);
    
    setVisible(false);
  }
}