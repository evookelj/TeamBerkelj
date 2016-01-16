public abstract class Player {

    //private Notesheet _notes
    private Card[] cards;
    private String _name;

    public Player(int numCards, String name) {
	cards = new Card[numCards];
	_name = name;
    }

    public void addCard(Card card) {
        for (int i=0; i<cards.length; i++) {
            if (cards[i] == null) {
                cards[i] = card;
                return ;
            }
        }
    }

    public Card getCard(int i) {
	return cards[i];
    }

    public String toString() {
	return _name;
    }

    abstract boolean accuseThisTurn();
    abstract MurderSituation suspect();
    abstract MurderSituation accuse();
}
