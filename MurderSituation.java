public class MurderSituation {
    public Card _who;
    public Card _where;
    public Card _weapon;

    public MurderSituation(Card who, Card where, Card weapon) {
	if (who._cardType == 0 &&
	    where._cardType == 1 &&
	    weapon._cardType == 2) {
	    _who = who;
	    _where = where;
	    _weapon = weapon;
	}
    }

    public Card getWho() { return _who; }
    public Card getWhere() { return _where; }
    public Card getWeapon() { return _weapon; }

    public String toString() {
	return _who.toString() + "," + _where.toString() + "," + _weapon.toString();
    }

    public boolean equals(Object o) {
	if (o == this) { return true; }

	if (o instanceof MurderSituation) {
	    MurderSituation guess = (MurderSituation)o;
	    if (_who.equals(guess._who) &&
		_where.equals(guess._where) &&
		_weapon.equals(guess._weapon)) {
		return true;
	    }
	}
	return false;
    }
}
