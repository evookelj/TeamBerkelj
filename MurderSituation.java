public class MurderSituation {
    private Card _who;
    private Card _where;
    private Card _weapon;

    public MurderSituation(Card who, Card where, Card weapon) {
	if (who.getCardType() == 0 &&
	    where.getCardType() == 1 &&
	    weapon.getCardType() == 2) {
	    _who = who;
	    _where = where;
	    _weapon = weapon;
	}
    }

    public Card getWho() { return _who; }
    public Card getWhere() { return _where; }
    public Card getWeapon() { return _weapon; }

    public String toString() {
	return _who.getName() + " in the "
            + _where.getName() + " with the "
            + _weapon.getName();
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
