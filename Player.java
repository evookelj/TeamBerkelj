public abstract class Player {

    private String _name;
    private Card[] _cards;
    private Notesheet _notes;
    private boolean _stillPlaying;

    public Player(int numCards, String name) {
	_name = name;
	_cards = new Card[numCards];
	_notes = new Notesheet();
        _stillPlaying = true;
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

    public boolean hasCard(Card card) {
	for (int i=0; i<_cards.length; i++) {
	    if (getCard(i).equals(card)) return true;
	}
	return false;
    }

    public String getName() {
        return _name;
    }

    public boolean getStillPlaying() {
        return _stillPlaying;
    }

    public void setStillPlaying(boolean stillPlaying) {
        _stillPlaying = stillPlaying;
    }

    public String toString() {
	return _name;
    }

    abstract boolean accuseThisTurn();
    abstract MurderSituation suspect(Game game);
    abstract MurderSituation accuse(Game game);
    abstract boolean accuseAfterSuspect();
}
