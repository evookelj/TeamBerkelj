import java.util.*;

public class AutoPlayer extends Player{
    private ArrayList<Card> _peopleLeft;
    private ArrayList<Card> _placesLeft;
    private ArrayList<Card> _weaponsLeft;

    public AutoPlayer(int numCards, int numPlayers) {
	super(numCards,"Auto" + Integer.toString(numPlayers));
    }

    public boolean accuseAfterSuspect() {
	return accuseThisTurn();
    }

    public void updateVars() {
	ArrayList<Card> people = new ArrayList<Card>();
	ArrayList<Card> places = new ArrayList<Card>();
	ArrayList<Card> weapons = new ArrayList<Card>();
	Notesheet _notes = super.getNotes();
	for (int i=0; i<_notes.itemsOfCardType(0).size(); i++) {
	    if (!(_notes.itemsOfCardType(0).get(i).getIsInnocent())) {
		people.add(_notes.itemsOfCardType(0).get(i).getCard());
	    }
	}
	for (int i=0; i<_notes.itemsOfCardType(1).size(); i++) {
	    if (!(_notes.itemsOfCardType(1).get(i).getIsInnocent())) {
		places.add(_notes.itemsOfCardType(1).get(i).getCard());
	    }
	}
	for (int i=0; i<_notes.itemsOfCardType(2).size(); i++) {
	    if (!(_notes.itemsOfCardType(2).get(i).getIsInnocent())) {
		weapons.add(_notes.itemsOfCardType(2).get(i).getCard());
	    }
	}
	//update instance vars accordingly
	_peopleLeft = people;
	_placesLeft = places;
	_weaponsLeft = weapons;
    }
    
    public boolean accuseThisTurn() {
	updateVars();
	if (_peopleLeft.size()==1
	    && _placesLeft.size()==1
	    && _weaponsLeft.size()==1) {
	    return true;
	} else {
	    return false;
	}
	
    }

    public Card pickFrom(int cardType) {
	if (cardType == 0) {
	    return _peopleLeft.get((int)(Math.random() * _peopleLeft.size()));
	} if (cardType == 1) {
	    return _placesLeft.get((int)(Math.random() * _placesLeft.size()));
	} if (cardType == 2) {
	    return _weaponsLeft.get((int)(Math.random() * _weaponsLeft.size()));
	} return null;
    }

    public MurderSituation suspect(Game game) {
	updateVars();
	Card person = pickFrom(0);
	Card place = pickFrom(1);
	Card weapon = pickFrom(2);
	MurderSituation retSituation = new MurderSituation(person, place, weapon);
	return retSituation;
    }

    public MurderSituation accuse(Game game) {
        updateVars();
	Card thePerson = _peopleLeft.get(0);
	Card thePlace = _placesLeft.get(0);
	Card theWeapon = _placesLeft.get(0);
	return new MurderSituation(thePerson,thePlace,theWeapon);
    }
}
