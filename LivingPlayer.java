import java.util.Scanner; //for text I/O

public class LivingPlayer extends Player {

    public LivingPlayer(int numCards, String name) {
	super(numCards, name);
    }

    public boolean accuseThisTurn() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Would you like to suspect or accuse? (S/A)");
	String ans = scan.nextLine();
	if (ans.equals("A")) {
	    return true;
	} if (ans.equals("S")) {
	    return false;
	} else {
	    System.out.println("Unexpected input given. Try again.");
	    return accuseThisTurn();
	}
    }

    public MurderSituation suspect() {
	Card perp = new Card("Scarlet",0);
	Card hall = new Card("Hall",1);
	Card wrench = new Card("Wrench",2);
	MurderSituation retSich = new MurderSituation(perp,hall,wrench);
	return retSich;
    }

    public MurderSituation accuse() {
	Card perp = new Card("Scarlet",0);
	Card hall = new Card("Hall",1);
	Card wrench = new Card("Wrench",2);
	MurderSituation retSich = new MurderSituation(perp,hall,wrench);
	return retSich;
    }
}
