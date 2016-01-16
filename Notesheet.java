import java.util.*;

public class Notesheet {
    private ArrayList<NotesheetItem> notes;

    public Notesheet() {
	notes = new ArrayList<NotesheetItem>();
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

    public int indexOf(Card card) {
	for (int i=0; i<notes.size(); i++) {
	    if (notes.get(i).getCard().equals(card)) {
		return i;
	    }
	}
	return -1;
    }

    public void crossOff(Card theInnocent) {
	notes.get(indexOf(theInnocent)).setIsInnocent(true);
    }

    public String toString() {
	String retStr = "";
	retStr += "PEOPLE:\n";
	for (int i=0; i<Game.personCards.length; i++) {
	    retStr += notes.get(i).toString() + "\n";
	}
	retStr += "\nPLACES:\n";
	for (int i=Game.personCards.length; i<(2*Game.placeCards.length)-3); i++) {
	    retStr += notes.get(i).toString() + "\n";
	}
	retStr += "\nWEAPONS:\n";
	for (int i=(Game.placeCards.length+Game.personCards.length); i<notes.size(); i++) {
	    retStr += notes.get(i).toString() + "\n";
	}
	return retStr;
    }
}
