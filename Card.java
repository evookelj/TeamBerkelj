public class Card {

    //cause every card has a name
    public String _name;
    public int _cardType;
    /*CARD TYPES:
      0: Person Card
      1: Place Card
      2: Weapon Card
     */

    public Card(String name,int type) {
	_name = name;
	_cardType = type;
    }

    public String toString() {
	return _name;
    }

    public boolean equals(Object o) {
	if (o instanceof Card) {
	    Card other = (Card)o;
	    return (this._name.equals(other._name) && this._cardType == other._cardType);
	} else {
	    return false;
	}
    }
}
