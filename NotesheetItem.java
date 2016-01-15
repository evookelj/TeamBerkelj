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
}
