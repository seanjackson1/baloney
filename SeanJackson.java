import java.util.*;

//Current
//January 23, 2022
public class SeanJackson implements Strategy {

    private Table table;
    private ArrayList<ArrayList<Card>> hands;
    private ArrayList<Card> discardPile;
    ArrayList<Integer> toPlay;

    public String getName() {
        return "The Army of Bologna";
    }

    public void gameStarted(Table table) {
        this.table = table;
        toPlay = new ArrayList<Integer>();
        hands = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < table.getNumPlayers(); i++) {
            hands.add(new ArrayList<Card>());
        }
        for (int i = 0; i < table.getNumCards(); i++) {
            hands.get(table.getSeat()).add(table.getCard(i));
        }
        discardPile = new ArrayList<Card>();
        for (int i = table.getSeat() + 1; i < table.getNumPlayers() * 100; i += table.getNumPlayers()) {
            if (i <= 13)
                toPlay.add(i);
            else
                toPlay.add(i % 13);
        }
    }

    public Card[] playCards(int rank) {

        // System.out.println("Start of Method");

        int totalOfRank = 0;
        for (int i = 0; i < table.getNumCards(); i++) {
            if (table.getCard(i).getRank() == rank)
                totalOfRank++;
        }

        // int[] ranks = new int[14];

        // for (int i = 1; i <= 13; i++) {
        // inner: for (int j = 0; j < toPlay.size(); j++) {
        // if (toPlay.get(j) == i) {
        // ranks[i] = j;
        // break inner;
        // }
        // }
        // }

        ArrayList<Card> ans = new ArrayList<Card>();

        // for (int j = 0; j < myHand.length; j++) {
        // int min = Integer.MAX_VALUE;
        // for (int k = 0; k < myHand.length; k++) {
        // if (ranks[myHand[k].getRank()] < Integer.MAX_VALUE)
        // min = k;
        // }
        // Card temp = myHand[j];
        // myHand[j] = myHand[min];
        // myHand[min] = temp;
        // }

        if (totalOfRank > 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (table.getCard(i).getRank() == rank) {
                    ans.add(table.getCard(i));
                }
            }
        } else {
            for (int j = 0; j < table.getNumCards(); j++) {
                ans.add(table.getCard(j));
            }

            int total = 0;
            int i = 0;
            while (i < table.getNumPlayers() && i != table.getSeat()) {
                for (Card card : hands.get(i)) {
                    if (card.getRank() == rank)
                        total++;
                }
                i++;
            }

            if (4 - total >= 2) {
                int currentRank = rank;
                while (ans.size() > 2) {
                    currentRank += table.getNumPlayers();
                    if (currentRank > 13)
                        currentRank = currentRank % 13;
                    int t = 0;
                    for (Card card : ans) {
                        if (card.getRank() == currentRank)
                            t++;
                    }

                    if (ans.size() - t > 1) {
                        for (int j = 0; j < ans.size(); j++) {
                            if (ans.get(j).getRank() == currentRank) {
                                ans.remove(j);
                                j--;
                            }
                        }
                    } else {
                        for (int j = 0; j < ans.size() && ans.size() > 1; j++) {
                            if (ans.get(j).getRank() == currentRank) {
                                ans.remove(j);
                                j--;
                            }
                        }
                    }

                }
            } else {
                // System.out.println("4 - total < 2");
                int currentRank = rank;
                while (ans.size() > 1) {
                    currentRank += table.getNumPlayers();
                    if (currentRank > 13)
                        currentRank = currentRank % 13;
                    // System.out.println("Rank: " + currentRank);
                    int t = 0;
                    for (Card card : ans) {
                        if (card.getRank() == currentRank)
                            t++;
                    }
                    // System.out.println("t: " + t);

                    if (ans.size() - t > 0) {
                        for (int j = 0; j < ans.size(); j++) {
                            // System.out.println("j in (ans.size() - t > 0): " + j);
                            if (ans.get(j).getRank() == currentRank) {
                                ans.remove(j);
                                j--;
                            }
                        }
                    } else {
                        for (int j = 0; j < ans.size() && ans.size() > 1; j++) {
                            // System.out.println("j in (ans.size() - t <= 0): " + j);
                            if (ans.get(j).getRank() == currentRank) {
                                ans.remove(j);
                                j--;
                            }
                        }
                    }

                }
            }

            // Card max = table.getCard(0);

            // for (int i = 0; i < table.getNumCards(); i++) {
            // if (ranks[table.getCard(i).getRank()] > ranks[table.getCard(i).getRank()]
            // || ranks[table.getCard(i).getRank()] == 0)
            // max = table.getCard(i);
            // }
            // ans.add(max);

            // int totalOfMax = 0;
            // for (int i = 0; i < table.getNumCards(); i++) {
            // if (table.getCard(i).getRank() == max.getRank())
            // totalOfMax++;
            // }

            // if (totalOfMax > 1) {

            // int total = 0;
            // int i = 0;
            // while (i < table.getNumPlayers() && i != table.getSeat()) {
            // for (Card card : hands.get(i)) {
            // if (card.getRank() == max.getRank())
            // total++;
            // }
            // i++;
            // }

            // if (4 - total >= 2) {
            // for (int j = 0; j < table.getNumCards(); j++) {
            // if (table.getCard(j).getRank() == max.getRank() &&
            // !table.getCard(j).equals(max)) {
            // ans.add(table.getCard(j));
            // break;
            // }
            // }
            // }

            // }

        }

        if (toPlay.size() > 0)
            toPlay.remove(0);

        for (Card card : ans)
            discardPile.add(card);

        Card[] actualAns = new Card[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            actualAns[i] = ans.get(i);
        }
        for (Card card : actualAns) {
            if (hands.get(table.getSeat()).contains(card))
                hands.get(table.getSeat()).remove(card);
        }
        return actualAns;
    }

    public boolean opponentPlayed(int opponentSeat, int numDiscarded, int discardedRank) {
        if (table.getNumCards(opponentSeat) == 0)
            return true;

        int total = 0;
        int i = 0;
        while (i < table.getNumPlayers() && i != opponentSeat && i != table.getSeat()) {
            for (Card card : hands.get(i)) {
                if (card.getRank() == discardedRank)
                    total++;
            }
            i++;
        }
        for (Card card : discardPile) {
            if (card.getRank() == discardedRank)
                total++;
        }
        for (int j = 0; j < table.getNumCards(); j++) {
            if (table.getCard(j).getRank() == discardedRank)
                total++;
        }

        if (numDiscarded > 4 - total)
            return true;
        else
            return false;
    }

    public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying) {
        discardPile = new ArrayList<Card>();
        if (lying) {
            for (Card card : cardsRevealed) {
                hands.get(playerSeat).add(card);
            }
            int i = 0;
            while (i < table.getNumPlayers() && i != playerSeat) {
                for (Card card : cardsRevealed) {
                    inner: for (int j = 0; j < hands.get(i).size(); j++) {
                        if (card.equals(hands.get(i).get(j))) {
                            hands.get(i).remove(j);
                            break inner;
                        }
                    }
                }
                i++;
            }
        } else {
            for (Card card : cardsRevealed) {
                hands.get(callerSeat).add(card);
            }
            int i = 0;
            while (i < table.getNumPlayers() && i != callerSeat) {
                for (Card card : cardsRevealed) {
                    inner: for (int j = 0; j < hands.get(i).size(); j++) {
                        if (card.equals(hands.get(i).get(j))) {
                            hands.get(i).remove(j);
                            break inner;
                        }
                    }
                }
                i++;
            }

        }

        /*
         * if (table.getSeat() == callerSeat)
         * printHands();
         */
    }

    /*
     * public void printHands() {
     * int seat = 0;
     * for (ArrayList<Card> hand : hands) {
     * System.out.println("Seat: " + seat);
     * for (Card card : hand) {
     * System.out.println("Suit: " + card.getSuit() + " --- Rank: " +
     * card.getRank());
     * }
     * seat++;
     * }
     * }
     */

}