import java.util.*;

// CLUBS = 0;
// DIAMONDS = 1;
// HEARTS = 2;
// SPADES = 3;

//January 23, 2022
public class SeanJacksonTheBestIfNoOther implements Strategy {

    private Table table;
    private int seat;
    private ArrayList<Integer> toPlay;
    private ArrayList<HashSet<Integer>> confirmedHasNot;
    private ArrayList<ArrayList<Integer>> otherDecks;
    private ArrayList<Integer> discardDeck;

    public String getName() {
        return "HA! Goteem.";
    }

    public void gameStarted(Table table) {
        confirmedHasNot = new ArrayList<HashSet<Integer>>();
        otherDecks = new ArrayList<ArrayList<Integer>>();
        discardDeck = new ArrayList<Integer>();
        this.table = table;
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

        for (int i = 0; i < table.getNumPlayers(); i++)
            otherDecks.add(new ArrayList<Integer>());

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

        ArrayList<Integer> badCards = new ArrayList<Integer>();

        for (int i = 1; i <= 13; i++) {
            if (!goodCards.contains(i))
                badCards.add(i);
        }

        int numCardsAvailable = getNumCardsAvailable(rank);

        if (totalOfRank != 0 && badCards.size() == 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (table.getCard(i).getRank() == rank)
                    cards.add(table.getCard(i));
            }
        }

        else if (totalOfRank == 0 && badCards.size() != 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (badCards.contains(table.getCard(i).getRank()))
                    cards.add(table.getCard(i));
            }
        }

        else if (totalOfRank != 0 && badCards.size() != 0) {
            for (int i = 0; i < table.getNumCards(); i++) {
                if (table.getCard(i).getRank() == rank)
                    cards.add(table.getCard(i));
            }
        }

        else if (totalOfRank == 0 && badCards.size() == 0) {
            for (int i = 0; i < table.getNumCards() && i <= 4; i++) {
                cards.add(table.getCard(i));
            }
        }

        if (numCardsAvailable > 0) {
            while (cards.size() > numCardsAvailable) {
                cards.remove(0);
            }
            if ((cards.size() == 4 || cards.size() == 3)) {
                boolean x = true;
                for (Card i : cards) {
                    if (i.getRank() != rank) {
                        x = false;
                        break;
                    }
                }
                if (!x) {
                    cards.remove(0);
                    cards.remove(0);
                }
            }
        } else {
            while (cards.size() > 1) {
                cards.remove(0);
            }
        }

        Card[] cardsArray = new Card[cards.size()];

        for (int i = 0; i < cards.size(); i++)
            cardsArray[i] = cards.get(i);

        for (Card i : cardsArray) {
            discardDeck.add(i.getRank());
        }

        return cardsArray;

    }

    public int getNumCardsAvailable(int rank) {
        int total = 4;
        for (ArrayList<Integer> i : otherDecks) {
            for (int j : i) {
                if (j == rank)
                    total--;
            }
        }
        if (total >= 0)
            return total;
        else
            return 0;
    }

    public int getNumCardsAvailable(int rank, int playerSeat) {
        int total = 4;
        for (int i : discardDeck) {
            if (i == rank)
                total--;
        }
        for (int i = 0; i < table.getNumCards(); i++) {
            if (rank == table.getCard(i).getRank())
                total--;
        }

        if (total >= 0)
            return total;
        else
            return 0;
    }

    public boolean opponentPlayed(int opponentSeat, int numDiscarded, int discardedRank) {

        if (numDiscarded > 4)
            return true;

        if (confirmedHasNot.get(opponentSeat).contains(discardedRank))
            return true;
        if (table.getNumCards(opponentSeat) == 0)
            return true;
        int possible = getNumCardsAvailable(discardedRank, opponentSeat);

        if (numDiscarded > possible)
            return true;
        confirmedHasNot.get(opponentSeat).add(discardedRank);

        return false;

    }

    public void liarCalled(int playerSeat, int rank, int callerSeat, Card[] cardsRevealed, boolean lying) {
        if (lying) {
            confirmedHasNot.get(playerSeat).add(rank);
            for (Card i : cardsRevealed) {
                if (confirmedHasNot.get(playerSeat).contains(i.getRank()))
                    confirmedHasNot.get(playerSeat).remove(i.getRank());
            }

            for (Card i : cardsRevealed) {
                otherDecks.get(playerSeat).add(i.getRank());
            }

        } else {
            for (Card i : cardsRevealed) {
                if (confirmedHasNot.get(callerSeat).contains(i.getRank()))
                    confirmedHasNot.get(callerSeat).remove(i.getRank());
            }

            for (Card i : cardsRevealed) {
                otherDecks.get(callerSeat).add(i.getRank());
            }

            discardDeck = new ArrayList<Integer>();
        }
    }
}
