import java.util.*;

// CLUBS = 0;
// DIAMONDS = 1;
// HEARTS = 2;
// SPADES = 3;

//January 23, 2022
public class SeanJackson22 implements Strategy {

    private Table table;
    private int seat;
    private ArrayList<Integer> toPlay;
    private int[][] deck;
    private int UNKNOWN;
    private ArrayList<HashSet<Integer>> confirmedHasNot;
    private ArrayList<Integer> discardDeck;

    public String getName() {
        return "HA! Goteem.";
    }

    public void gameStarted(Table table) {
        confirmedHasNot = new ArrayList<HashSet<Integer>>();
        discardDeck = new ArrayList<Integer>();
        this.table = table;
        UNKNOWN = table.getNumPlayers() + 5;
        seat = table.getSeat();
        toPlay = new ArrayList<Integer>();
        for (int i = seat + 1; i < 100 * table.getNumPlayers(); i += table.getNumPlayers()) {
            if (i <= 13)
                toPlay.add(i);
            else
                toPlay.add(i % 13);
        }

        for (int i = 0; i < table.getNumPlayers(); i++)
            confirmedHasNot.add(new HashSet<Integer>());

        deck = new int[4][14];

        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {

                deck[i][j] = UNKNOWN;

            }
        }

        doDeck();

    }

    public void doDeck() {

        for (int i = 0; i < 4; i++) {
            for (int j = 1; j < 14; j++) {
                for (int k = 0; k < table.getNumCards(); k++) {
                    if (table.getCard(k).getSuit() == i && table.getCard(k).getRank() == j)
                        deck[i][j] = seat;
                }
            }
        }
    }

    public Card[] playCards(int rank) {

        int totalOfRank = 0;
        for (int i = 0; i < table.getNumCards(); i++) {
            if (table.getCard(i).getRank() == rank)
                totalOfRank++;
        }

        ArrayList<Card> cards = new ArrayList<Card>();

        ArrayList<Integer> goodCards = new ArrayList<Integer>();

        for (int i = 0; i < table.getNumCards(); i++) {
            goodCards.add(toPlay.get(i));
        }

        ArrayList<Card> badCards = new ArrayList<Card>();

        for (int i = 0; i < table.getNumCards(); i++) {
            for (int j = 0; j < goodCards.size(); j++) {
                int total = 0;
                if (table.getCard(i).getRank() != goodCards.get(j)) {
                    total++;
                }
                if (total == goodCards.size())
                    badCards.add(table.getCard(i));
            }
        }

        int numCardsAvailable = getNumCardsAvailable(rank, seat);

        int k = 0;

        if (badCards.size() == 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (table.getCard(i).getRank() == rank) {
                    cards.add(table.getCard(i));
                }
            }
        }

        else if (totalOfRank == 0) {
            while (k < numCardsAvailable - 2 && k < badCards.size()) {
                cards.add(badCards.get(k));
                k++;
            }

        }

        if (cards.size() == 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (table.getCard(i).getRank() == rank) {
                    cards.add(table.getCard(i));
                }
            }
        }

        if (cards.size() == 0) {
            k = 0;
            while (k < numCardsAvailable - 2 && k < table.getNumCards()) {
                cards.add(table.getCard(k));
                k++;
            }
        }

        if (cards.size() == 0) {
            k = 0;
            while (k < 1 && k < table.getNumCards()) {
                cards.add(table.getCard(k));
                k++;
            }
        }

        Card[] cardsArray = new Card[cards.size()];

        for (int i = 0; i < cards.size(); i++)
            cardsArray[i] = cards.get(i);

        for (Card i : cardsArray) {
            deck[i.getSuit()][i.getRank()] = UNKNOWN;
        }

        for (Card i : cardsArray) {
            discardDeck.add(i.getRank());
        }

        return cardsArray;

    }

    public int getNumCardsAvailable(int rank) {
        int total = 4;
        for (int i = 0; i < deck.length; i++) {
            if (deck[i][rank] < table.getNumPlayers() && deck[i][rank] != seat)
                total--;
        }
        return total;
    }

    public int getNumCardsAvailable(int rank, int personSeat) {
        int total = 4;
        for (int i = 0; i < deck.length; i++) {
            if (deck[i][rank] < table.getNumPlayers() && deck[i][rank] != personSeat)
                total--;
        }
        return total;
    }

    public boolean opponentPlayed(int opponentSeat, int numDiscarded, int discardedRank) {

        if (confirmedHasNot.get(opponentSeat).contains(discardedRank))
            return true;

        if (table.getNumCards(opponentSeat) == 0)
            return true;

        for (int i = 0; i < 4; i++) {
            if (deck[i][discardedRank] == opponentSeat)
                deck[i][discardedRank] = UNKNOWN;
        }

        doDeck();

        return false;
    }

    public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying) {
        if (lying) {
            confirmedHasNot.get(playerSeat).add(rank);
            for (Card i : cardsRevealed) {
                deck[i.getSuit()][i.getRank()] = playerSeat;
                if (confirmedHasNot.get(playerSeat).contains(i.getRank()))
                    confirmedHasNot.get(playerSeat).remove(i.getRank());
            }
        } else {
            for (Card i : cardsRevealed) {
                if (confirmedHasNot.get(callerSeat).contains(i.getRank()))
                    confirmedHasNot.get(callerSeat).remove(i.getRank());
                deck[i.getSuit()][i.getRank()] = callerSeat;
            }
        }
        doDeck();
    }
}
