
//January 23, 2022
import java.util.*;

public class Baloney {
  public static void main(String[] args) {

    boolean showAllCards = true;
    int delayInMilliseconds = 200;
    Display display = new Display(showAllCards, delayInMilliseconds);
    Strategy[] strategies = { new SeanJackson(), new Human(display), new
    SeanJackson(), new SeanJackson()
    };
    int winner = Game.play(display, strategies);
    System.out.println("winner = " + winner);

    for (int i = 0; i < 10; i++) {
    System.out.println("Game: " + i + " Winner = " + Game.play(null,
    strategies));
    }

    // Strategy[] strategies = {
    //     new SeanJackson(), new SeanJackson(), new SeanJackson(), new SeanJackson() };

    // int[] seats = new int[4];
    // for (int i = 1; i < 100; i++) {
    //   int winner = Game.play(null, strategies);
    //   seats[winner]++;
    //   System.out.println("Game: " + i + " Winner = " + winner);
    // }
    // for (int i = 0; i < 4; i++)
    //   System.out.println("Seat: " + i + " Games Won = " + seats[i]);

    // new RemoteTable("10.13.99.50", new SeanJackson());
    // new RemoteTable("10.13.99.50", new SeanJacksonTheBestIfNoOther());
  }
}
