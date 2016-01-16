import java.util.ArrayList;

public class Notesheet {
    private ArrayList<NotesheetItem> people;
    private ArrayList<NotesheetItem> places;
    private ArrayList<NotesheetItem> weapons;

    public Notesheet() {
        // People
        people = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.personCards.length; i++) {
	    people.add(new NotesheetItem(Game.personCards[i], false));
	}

        // Places
        places = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.placeCards.length; i++) {
	    places.add(new NotesheetItem(Game.placeCards[i], false));
	}

        // Weapons
        weapons = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.weaponCards.length; i++) {
	    weapons.add(new NotesheetItem(Game.weaponCards[i], false));
	}
    }

    private ArrayList<NotesheetItem> itemsOfCardType(int cardType) {
        if (cardType == 0) { return people; }
        if (cardType == 1) { return places; }
        return weapons;
    }

    private NotesheetItem findItem(Card card) {
        ArrayList<NotesheetItem> items = itemsOfCardType(card.getCardType());
        for (int i = 0; i < items.size(); i++) {
            NotesheetItem item = items.get(i);
            if (item.getCard().equals(card)) {
                return item;
	    }
	}
        return null;
    }

    public void crossOff(Card theInnocent) {
        findItem(theInnocent).setIsInnocent(true);
    }

    public String toString() {
	String retStr = "";
	retStr += "PEOPLE:\n";
	for (int i=0; i < people.size(); i++) {
	    retStr += people.get(i).toString() + "\n";
	}
	retStr += "\nPLACES:\n";
	for (int i = 0; i < places.size(); i++) {
	    retStr += places.get(i).toString() + "\n";
	}
	retStr += "\nWEAPONS:\n";
	for (int i = 0; i < weapons.size(); i++) {
	    retStr += weapons.get(i).toString() + "\n";
	}
	return retStr;
    }
}
