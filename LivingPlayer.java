import java.util.Scanner;

public class LivingPlayer extends Player {

    public LivingPlayer(int numCards, String name) {
	super(numCards, name);
    }

    public String toString() {
	return super.toString();
    }

    public boolean accuseThisTurn() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Would you like to suspect or accuse? (S/A)");
	String ans = scan.nextLine();
	if (ans.equals("A")) {
	    return true;
	}
        if (ans.equals("S")) {
	    return false;
	}
        System.out.println("Unexpected input given. Try again.");
        return accuseThisTurn();
    }

    public MurderSituation suspect(Game game) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Who would you like to suspect?");
        String suspecteeName = scan.nextLine();
        return null; // TODO
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
            for (Card s : cards) {
                if (s.getName().equals(input)) {
                    return s;
                }
            }
        }
        return null;
    }

    public MurderSituation accuse(Game game) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Who would you like to accuse?");
        String personName = scan.nextLine();
        Card person = new Card(personName, 0);
        while (!game.cardExists(person)) {
            person = helpUserSelectCard(scan, "person", Game.personCards);
        }

        System.out.println("Where did they do the murder???");
        String placeName = scan.nextLine();
        Card place = new Card(placeName, 1);
        while (!game.cardExists(place)) {
            place = helpUserSelectCard(scan, "place", Game.placeCards);
        }

        // Note the exponentially rising intensity, thanks to question marks
        System.out.println("What did they do the murder with?????????");
        String weaponName = scan.nextLine();
        Card weapon = new Card(weaponName, 2);
        while (!game.cardExists(weapon)) {
            weapon = helpUserSelectCard(scan, "weapon", Game.weaponCards);
        }

        return new MurderSituation(person, place, weapon);
    }
}
