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

    public MurderSituation suspect() {
        return null; // TODO
    }

    public MurderSituation accuse() {
        return null; // TODO
    }
}
