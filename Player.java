public abstract class Player {

    private Notesheet _notes;
    private Card[] _cards;
    private String _name;

    public Player(int numCards, String name) {
	_cards = new Card[numCards];
	_name = name;
	_notes = new Notesheet();
    }

    public Notesheet getNotes() {
	return _notes;
    }

    public void addCard(Card card) {
        for (int i=0; i<_cards.length; i++) {
            if (_cards[i] == null) {
                _cards[i] = card;
                return ;
            }
        }
    }

    public Card getCard(int i) {
	return _cards[i];
    }

    public String getName() {
        return _name;
    }

    public String toString() {
	return _name;
    }

    abstract boolean accuseThisTurn();
    abstract MurderSituation suspect(Game game);
    abstract MurderSituation accuse(Game game);
}
