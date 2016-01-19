import java.util.ArrayList;
import java.util.Scanner;

public class Notesheet {
    private ArrayList<NotesheetItem> _people;
    private ArrayList<NotesheetItem> _places;
    private ArrayList<NotesheetItem> _weapons;
    private String _comments = "";

    public Notesheet() {
        // People
        _people = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.personCards.length; i++) {
	    _people.add(new NotesheetItem(Game.personCards[i], false));
	}

        // Places
        _places = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.placeCards.length; i++) {
	    _places.add(new NotesheetItem(Game.placeCards[i], false));
	}

        // Weapons
        _weapons = new ArrayList<NotesheetItem>();
	for (int i = 0; i < Game.weaponCards.length; i++) {
	    _weapons.add(new NotesheetItem(Game.weaponCards[i], false));
	}
    }

    public ArrayList<NotesheetItem> itemsOfCardType(int cardType) {
        if (cardType == 0) { return _people; }
        if (cardType == 1) { return _places; }
        return _weapons;
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
	Scanner scan = new Scanner(System.in);
	String retStr = "";
	retStr += "PEOPLE:\n";
	for (int i=0; i < _people.size(); i++) {
	    retStr += _people.get(i).toString() + "\n";
	}
	retStr += "\nPLACES:\n";
	for (int i = 0; i < _places.size(); i++) {
	    retStr += _places.get(i).toString() + "\n";
	}
	retStr += "\nWEAPONS:\n";
	for (int i = 0; i < _weapons.size(); i++) {
	    retStr += _weapons.get(i).toString() + "\n";
	}
	retStr += "\nYour comments:\n" + _comments;
	return retStr;
    }

    public void manageComments() {
	Scanner scan = new Scanner(System.in);
        System.out.println("\nWould you like to add to your comments? (Y/N)");
	String ans = scan.nextLine();;
	if (ans.equals("Y")) {
	    System.out.println("What would you like to add?");
	    _comments += scan.nextLine();
	    System.out.println("Great notes! They have been saved.");
	} else {
	    System.out.println("Alrighty, moving on then.");
	}
    }
}
