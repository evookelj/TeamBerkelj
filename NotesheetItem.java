public class NotesheetItem {
    private Card _card;
    private boolean _isInnocent;

    public NotesheetItem(Card card, boolean isInnocent) {
	_card = card;
	_isInnocent = isInnocent;
    }

    public Card getCard() {
	return _card;
    }
   
    public boolean getIsInnocent() {
	return _isInnocent;
    }

    public void setIsInnocent(boolean bool) {
	_isInnocent = bool;
    }

    public String toString() {
	String retStr = _card.getName() + ":\t";
	if (_isInnocent) {
	    retStr += "X";
	} else {
	    retStr += " ";
	}
	return retStr;
    }
}
