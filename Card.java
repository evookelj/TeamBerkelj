public class Card {
    private final String _name;
    private final int _cardType;
    /*CARD TYPES:
      0: Person Card
      1: Place Card
      2: Weapon Card
     */

    public Card(String name, int type) {
	_name = name;
	_cardType = type;
    }

    public String getName() { return _name; }

    public int getCardType() { return _cardType; }

    public boolean equals(Object o) {
	if (o instanceof Card) {
	    Card other = (Card)o;
	    return this._name.equals(other._name) &&
                this._cardType == other._cardType;
	} else {
	    return false;
	}
    }
}
