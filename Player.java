public abstract class Player {

    //private Notesheet _notes
    private Card[] cards;
    private String _name;

    public Player(int numCards, String name) {
	cards = new Card[numCards];
	_name = name;
    }

    abstract boolean accuseThisTurn();
    abstract MurderSituation suspect();
    abstract MurderSituation accuse();
}
