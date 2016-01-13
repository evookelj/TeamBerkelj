public abstract class Card {

    //cause every card has a name
    private String _name;

    public boolean equals(Object o) {
	if (o instanceof Card) {
	    Card other = (Card)o;
	} else {
	    return false;
	}
	return this._name.equals(other.name);
    }
}
