//January 24, 2022
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Display
{
  private Game game;
  private JFrame frame;
  private HandPanel discardPanel;
  private JPanel tablePanel;
  private boolean showAllCards;
  private PlayerPanel[] playerPanels;
  private int delay;
  private HandPanel justPlayedPanel;
  
  public Display(boolean showAllCards, int delayInMilliseconds)
  {
    this.showAllCards = showAllCards;
    delay = delayInMilliseconds;
    
    frame = new JFrame("Baloney");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));
    
    JPanel bigDiscardPanel = new JPanel();
    bigDiscardPanel.setLayout(new BoxLayout(bigDiscardPanel, BoxLayout.PAGE_AXIS));
    bigDiscardPanel.setBackground(Color.BLACK);
    frame.getContentPane().add(bigDiscardPanel);
    
    JLabel label = new JLabel("Just Played");
    label.setForeground(Color.WHITE);
    bigDiscardPanel.add(label);
    
    justPlayedPanel = new HandPanel(2, 2, true, 0, true);
    bigDiscardPanel.add(justPlayedPanel);
    
    label = new JLabel("Discard Pile");
    label.setForeground(Color.WHITE);
    bigDiscardPanel.add(label);
    
    discardPanel = new HandPanel(11, 5, showAllCards, 0, false);
    bigDiscardPanel.add(discardPanel);
    
    playerPanels = new PlayerPanel[1];
    tablePanel = new JPanel(new GridLayout(playerPanels.length, 1));
    frame.getContentPane().add(tablePanel);
    
    for (int seat = 0; seat < playerPanels.length; seat++)
    {
      playerPanels[seat] = new PlayerPanel(seat, null, false);
      tablePanel.add(playerPanels[seat]);
    }
    
    frame.pack();
    frame.setVisible(true);
  }
  
  public void setGame(Game game)
  {
    this.game = game;
    tablePanel.removeAll();
    playerPanels = new PlayerPanel[game.getPlayers().length];
    discardPanel.setCards(game.getDiscardPile());
    justPlayedPanel.setCards(game.getJustPlayed());
    //System.out.println("setGame: set justPlayedPanel to " + System.identityHashCode(justPlayedPanel.getCards()));
    tablePanel.setLayout(new GridLayout(playerPanels.length, 1));
    for (int seat = 0; seat < playerPanels.length; seat++)
    {
      Player player = game.getPlayers()[seat];
      playerPanels[seat] = new PlayerPanel(seat, player,
                                           showAllCards || player.getStrategy() instanceof Human);
      tablePanel.add(playerPanels[seat]);
    }
    frame.pack();
  }
  
  private static final String[] LIAR_MESSAGES = {
    "absurd",
    "apocryphal",
    "applesauce",
    "as if",
    "balderdash",
    "baloney",
    "bilge",
    "blather",
    "bolognese",
    "bogus",
    "bosh",
    "bull",
    "bunk",
    "bunkum",
    "bushwa",
    "cap",
    "cheater",
    "claptrap",
    "codswallop",
    "drivel",
    "eyewash",
    "fake",
    "false",
    "fiction",
    "fictitious",
    "fiddle faddle",
    "flapdoodle",
    "foolishness",
    "fraudulent",
    "garbage",
    "gibberish",
    "guff",
    "hogwarts",
    "hogwash",
    "hooey",
    "horse feathers",
    "hyperbole",
    "i doubt that",
    "inaccurate",
    "inconceivable",
    "liar",
    "lies",
    "ludicrous",
    "malarkey",
    "moonshine",
    "myth",
    "no way",
    "nonsense",
    "pants on fire",
    "phony",
    "piffle",
    "poppycock",
    "ridiculous",
    "rubbish",
    "sham",
    "tripe",
    "tommyrot",
    "tosh",
    "twaddle",
    "unlikely",
    "untrue",
    "utter rot",
    "you wish",
  };
  
  private void update()
  {
    frame.getContentPane().repaint();
  }
  
  private void pause()
  {
    if (delay < 10000)
      try{Thread.sleep(delay);}catch(Exception e){}
    else
      new java.util.Scanner(System.in).nextLine();
  }
  
  public void discarded(int seat, int rank)
  {
    //System.out.println("discarded");
    clearAll();

    int numDiscarded = game.getJustPlayed().size();
    String message = numDiscarded + " ";
    if (numDiscarded > 1)
      message += Card.toPluralRankName(rank).toUpperCase();
    else
      message += Card.toRankName(rank).toUpperCase();

    playerPanels[seat].setMessage(message, Color.CYAN);
    
    justPlayedPanel.setShowCards(false);
    
    updateAndPause();
  }
  
  public void caughtLying(int playerSeat, int rank, int callerSeat)
  {
    //System.out.println("caughtLying");
    justPlayedPanel.setShowCards(true);
    updateAndPause();
  }
  
  private void clearAll()
  {
    for (int i = 0; i < playerPanels.length; i++)
      playerPanels[i].setMessage("", null);
  }
  
  public Card[] playCards(int seat, int rank)
  {
    //System.out.println("playCards");
    clearAll();
    pause();
    return new HandDialog(frame, game.getPlayers()[seat].getHand(), rank).getCards();
  }
  
  public boolean opponentPlayed(int opponentSeat, int numDiscarded, int rank)
  {
    //System.out.println("opponentPlayed");
    String claim;
    if (numDiscarded > 1)
      claim = Card.toPluralRankName(rank);
    else
      claim = Card.toRankName(rank);

    int response = JOptionPane.showOptionDialog(frame,
                                                "Seat " + opponentSeat + " discarded \"" + numDiscarded + " " + claim + "\"",
                                                "Opponent Played",
                                                JOptionPane.OK_CANCEL_OPTION,
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                new String[]{"Liar!", "Ignore"},
                                                "Ignore");
    return response == JOptionPane.OK_OPTION;
  }
  
  public void startOfTurn(int seat, int rank)
  {
    //System.out.println("startOfTurn");
    clearAll();
    //playerPanels[seat].setMessage(Card.toPluralRankName(rank).toUpperCase() + "?", Color.CYAN);
    updateAndPause();
  }
  
  public void liarCalled(int seat, int rank, int callerSeat, Card[] played)
  {
    //System.out.println("liarCalled");
    justPlayedPanel.setShowCards(false);
    String message = LIAR_MESSAGES[(int)(Math.random() * LIAR_MESSAGES.length)];
    message = message.toUpperCase() + "!";
    playerPanels[callerSeat].setMessage(message, Color.MAGENTA);
    updateAndPause();
  }
  
  public void updateAndPause()
  {
//    System.out.println("updateAndPause");
//    System.out.println("discardPanel:  " + discardPanel.getCards() + " " +
//                       System.identityHashCode(discardPanel.getCards()));
//    System.out.println("justPlayedPanel:  " + justPlayedPanel.getCards() + " " +
//                       System.identityHashCode(justPlayedPanel.getCards()));
    update();
    pause();
  }
  
  public void showWinner(int seat)
  {
    JOptionPane.showMessageDialog(frame, game.getPlayers()[seat].getName() + " won!");
  }
}