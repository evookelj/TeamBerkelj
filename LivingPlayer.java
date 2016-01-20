import java.util.Scanner;

public class LivingPlayer extends Player {

    public LivingPlayer(int numCards, String name) {
	super(numCards, name);
    }

    public String toString() {
	return super.toString();
    }

    public boolean accuseAfterSuspect() {
	Scanner scan = new Scanner(System.in);
	System.out.println(getName() + 
			   ", would you like to make an accusation? (T/F)");
	String ans = scan.nextLine();
	return "T".equals(ans.toUpperCase());
    }

    public boolean accuseThisTurn() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Would you like to suspect or accuse (S/A)?\n" +
			   "If you need to review your notesheet, say 'notes'.");
	String ans = scan.nextLine();
	if (ans.equals("A")) {
	    return true;
	} if (ans.equals("S")) {
	    return false;
	} if (ans.equals("notes")) {
	    System.out.println(getNotes().toString());
	    getNotes().manageComments();
	    return accuseThisTurn();
	}
        System.out.println("Unexpected input given. Try again.");
        return accuseThisTurn();
    }

    public MurderSituation suspect(Game game) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Who would you like to suspect?");
        String personInput = scan.nextLine();
        Card person = new Card(personInput, 0);
	while (!game.cardExists(person)) {
	    person = helpUserSelectCard(scan, "person", Game.personCards);
	}

	System.out.println("Where do you suspect " + person.getName() + " did it?");
	String placeInput = scan.nextLine();
	Card place = new Card(placeInput, 1);
	while (!game.cardExists(place)) {
	    place = helpUserSelectCard(scan, "place", Game.placeCards);
	}

	System.out.println("And what did " + person.getName() + " do it with?");
	String weaponInput = scan.nextLine();
	Card weapon = new Card(weaponInput, 2);
	while (!game.cardExists(weapon)) {
	    weapon = helpUserSelectCard(scan, "weapon", Game.weaponCards);
	}

	return new MurderSituation(person, place, weapon);
    }

    public MurderSituation accuse(Game game) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Who would you like to accuse?");
        String personInput = scan.nextLine().trim();
        Card person = new Card(personInput, 0);
        while (!game.cardExists(person)) {
            person = helpUserSelectCard(scan, "person", Game.personCards);
        }

        System.out.println("Where did " + person.getName() + " do the murder???");
        String placeInput = scan.nextLine().trim();
        Card place = new Card(placeInput, 1);
        while (!game.cardExists(place)) {
            place = helpUserSelectCard(scan, "place", Game.placeCards);
        }

        // Note the exponentially rising intensity, thanks to question marks
        System.out.println("What did " + person.getName()
            + " do the murder in the " + place.getName()
            + " with?????????");
        String weaponInput = scan.nextLine().trim();
        Card weapon = new Card(weaponInput, 2);
        while (!game.cardExists(weapon)) {
            weapon = helpUserSelectCard(scan, "weapon", Game.weaponCards);
        }

        return new MurderSituation(person, place, weapon);
    }

    private Card helpUserSelectCard(Scanner scan, String thingNeeded, Card[] cards) {
        System.out.println("Sorry, that isn't any " + thingNeeded + "'s name.");
        System.out.println("Here are the current " + thingNeeded + "s:");
        for (int i = 0; i < cards.length; i += 1) {
            System.out.println("#" + (i + 1) + ". " + cards[i].getName());
        }
        System.out.println("Enter a " + thingNeeded + "'s name or number:");
        String input = scan.nextLine().trim();
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
