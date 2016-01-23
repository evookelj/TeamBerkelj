import java.util.Scanner;

public class LivingPlayer extends Player {
    private UserIO _io;

    public LivingPlayer(int numCards, String name) {
	super(numCards, name);
        _io = new UserIO(this);
    }

    public String toString() {
	return super.toString();
    }

    public boolean accuseAfterSuspect() {
        String ans = _io.getLineLowered(getName() + ", would you like to " +
                                        "make an accusation? (T/F)");
	return "t".equals(ans);
    }

    public boolean accuseThisTurn() {
        String ans = _io.getLineLowered("Would you like to suspect or accuse (S/A)?\n" +
                                        "If you need to review your notesheet, say 'notes'.");
        if (ans.equals("a")) {
	    return true;
	} if (ans.equals("s")) {
	    return false;
	}
        System.out.println("Unexpected input given. Try again.");
        return accuseThisTurn();
    }

    public MurderSituation suspect(Game game) {
        String personInput = _io.getLine("Who would you like to suspect?");
        Card person = new Card(personInput, 0);
	while (!game.cardExists(person)) {
	    person = helpUserSelectCard("person", Game.personCards);
	}

	String placeInput = _io.getLine("Where do you suspect " + person.getName() + " did it?");
	Card place = new Card(placeInput, 1);
	while (!game.cardExists(place)) {
	    place = helpUserSelectCard("place", Game.placeCards);
	}

	String weaponInput = _io.getLine("And what did " + person.getName() + " do it with?");
	Card weapon = new Card(weaponInput, 2);
	while (!game.cardExists(weapon)) {
	    weapon = helpUserSelectCard("weapon", Game.weaponCards);
	}

	return new MurderSituation(person, place, weapon);
    }

    public MurderSituation accuse(Game game) {
        String personInput = _io.getLine("Who would you like to accuse?");
        Card person = new Card(personInput, 0);
        while (!game.cardExists(person)) {
            person = helpUserSelectCard("person", Game.personCards);
        }

        String placeInput = _io.getLine("Where did " + person.getName() + " do the murder???");
        Card place = new Card(placeInput, 1);
        while (!game.cardExists(place)) {
            place = helpUserSelectCard("place", Game.placeCards);
        }

        // Note the exponentially rising intensity, thanks to question marks
        String weaponPrompt =
            "What did " + person.getName()
            + " do the murder in the " + place.getName()
            + " with?????????";
        String weaponInput = _io.getLine(weaponPrompt);
        Card weapon = new Card(weaponInput, 2);
        while (!game.cardExists(weapon)) {
            weapon = helpUserSelectCard("weapon", Game.weaponCards);
        }

        return new MurderSituation(person, place, weapon);
    }

    public void enterNotes() {
        System.out.println(getNotes());
        _io.enterToContinue();
    }

    private Card helpUserSelectCard(String cardTypeNeeded, Card[] cards) {
        System.out.println("Sorry, that isn't the name of a " + cardTypeNeeded + ".");
        System.out.println("Here are the " + cardTypeNeeded + "s:");
        for (int i = 0; i < cards.length; i += 1) {
            System.out.println("#" + (i + 1) + ". " + cards[i].getName());
        }
        String input = _io.getLine("Enter a " + cardTypeNeeded + "'s name or number:");
        try {
            int num = Integer.parseInt(input);
            return cards[num - 1];
        } catch (Exception e) {
            String name = Card.normalizeName(input);
            for (Card s : cards) {
                if (s.getName().equals(name)) {
                    return s;
                }
            }
        }
        return null;
    }
}
