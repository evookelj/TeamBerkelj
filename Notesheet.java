public class Notesheet {
    private ArrayList<NotesheetItem> notes;

    public String toString() {
	String retStr = "";
	for (int i=0; i<notes.size(); i++) {
	    retStr += notes.get(i).getCard().getName() + ": "
		+ notes.get(i).getIsInnocent() + "\n";
	}
	return retStr;
    }

    public Notesheet() {
	notes = new ArrayList<NotesheetItem>;
	for (int i = 0; i < Game.personCards.length; i++) {
	    notes.add(new NotesheetItem(Game.personCards[i], false));
	}
	for (int i = 0; i < Game.placeCards.length; i++) {
	    notes.add(new NotesheetItem(Game.placeCards[i], false));
	}
	for (int i = 0; i < Game.weaponCards.length; i++) {
	    notes.add(new NotesheetItem(Game.weaponCards[i], false));
	}
    }

    public void makeW
}
