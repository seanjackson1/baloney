//January 23, 2022
import java.awt.*;
import javax.swing.*;

public class PlayerPanel extends JPanel
{
  private static final Font MESSAGE_FONT = new Font(null, Font.BOLD, 60);

  private int seat;
  private Player player;
  private JLabel label;
  private HandPanel handPanel;
  private String message;
  private Color color;
  
  public PlayerPanel(int seat, Player player, boolean showCards)
  {
    this.seat = seat;
    this.player = player;
    message = "";
    color = null;
    
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setBackground(Color.BLACK);
    
    label = new JLabel("Seat " + seat);
    label.setForeground(Color.WHITE);
    add(label);

    handPanel = new HandPanel(2, 26, showCards, 0, false);
    add(handPanel);

    if (player != null)
    {
      label.setText("Seat " + seat + ":  " + player.getName());
      handPanel.setCards(player.getHand());
    }
  }
  
  public void setMessage(String message, Color color)
  {
    this.message = message;
    this.color = color;
  }
  
  public void paint(Graphics g)
  {
    super.paint(g);
    
    g.setFont(MESSAGE_FONT);
    FontMetrics fm = g.getFontMetrics();
    g.setColor(Color.BLACK);
    int x = (getWidth() - fm.stringWidth(message)) / 2;
    int y = fm.getHeight();
    g.drawString(message, x - 1, y);
    g.drawString(message, x + 1, y);
    g.drawString(message, x, y - 1);
    g.drawString(message, x, y + 1);
    g.setColor(color);
    g.drawString(message, x, y);
  }
}