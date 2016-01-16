import java.util.Scanner;
import java.util.*;

public class Game {
    public static Card[] personCards = {
        new Card("Col. Mustard", 0),
        new Card("Prof. Plum", 0),
        new Card("Mr. Green", 0),
        new Card("Mrs. Peacock", 0),
        new Card("Miss Scarlet", 0),
        new Card("Mrs. White", 0)
    };
    public static Card[] placeCards = {
        new Card("Hall", 1),
        new Card("Lounge", 1),
        new Card("Dining Room", 1),
        new Card("Kitchen", 1),
        new Card("Ball Room", 1),
        new Card("Conservatory", 1),
        new Card("Billiard Room", 1),
        new Card("Library", 1),
        new Card("Study", 1)
    };
    public static Card[] weaponCards = {
        new Card("Knife", 2),
        new Card("Candlestick", 2),
        new Card("Revolver", 2),
        new Card("Rope", 2),
        new Card("Lead Pipe", 2),
        new Card("Wrench", 2)
    };

    private ArrayList<Card> playingDeck;
    private int _currentTurn;
    private int numCards;
    private Player[] players;
    private MurderSituation _theTruth;

    public void initGame() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Hello there! Welcome to Who-dunitz. What is your name?");
	String p1name = scan.nextLine();
	System.out.println("Thanks!. How many friends are you playing with?");
	int numFriends = initFriends();

	numCards = 18/(numFriends+1);
	players = new Player[numFriends+1];
	LivingPlayer p1 = new LivingPlayer(numCards, p1name);
	players = makePlayers(numFriends, p1);

	playingDeck = new ArrayList<Card>();
	for (int i=0; i<personCards.length; i++) { playingDeck.add(personCards[i]); }
	for (int i=0; i<placeCards.length; i++) { playingDeck.add(placeCards[i]); }
	for (int i=0; i<weaponCards.length; i++) { playingDeck.add(weaponCards[i]); }
	
	fillEnvelope();
	dealCards(numCards);
    }

    public Player[] makePlayers(int numFriends, Player p1) {
	Scanner scan = new Scanner(System.in);
	Player[] retArr = new Player[numFriends + 1];
	retArr[0] = p1;
	for (int i=1; i<=numFriends; i++) {
	    System.out.println("What is the name of player " + (i+1) + "?");
	    retArr[i] = new LivingPlayer(numCards, scan.nextLine());
	}
	return retArr;
    }
    
    public void fillEnvelope() {
	Random generator = new Random();
	int rand = generator.nextInt(personCards.length);
	Card theWho = personCards[rand];
	playingDeck.remove(theWho);

	rand = generator.nextInt(placeCards.length);
	Card thePlace = placeCards[rand];
	playingDeck.remove(thePlace);

	rand = generator.nextInt(weaponCards.length);
	Card theWeapon = weaponCards[rand];
	playingDeck.remove(theWeapon);

	_theTruth = new MurderSituation(theWho, thePlace, theWeapon);
    }
    
    public void dealCards(int numCards) {
	for (int p=0; p<players.length; p++) {
	    for (int i=0; i<numCards; i++) {
		players[p].addCard(playingDeck.get(0));
		playingDeck.remove(0);
	    }
	}
    }
    
    public static ArrayList shuffle( ArrayList al ) {
	int randomIndex;
        for( int i = al.size()-1; i > 0; i-- ) {
	    //pick an index at random
            randomIndex = (int)( (i+1) * Math.random() );
	    //swap the values at position i and randomIndex
            al.set( i, al.set( randomIndex, al.get(i) ) );
        }
	return al;

	
    }

    public int initFriends() {
	Scanner scan = new Scanner(System.in);
	try {
	    return Integer.parseInt(scan.nextLine());
	} catch (NumberFormatException nfe) {
	    System.out.println("It seems you didn't enter a number. Please try again.");
	    return initFriends();
	}
    }

    public static void main(String[] args) {
	Game emma = new Game();
	emma.initGame();

	System.out.println("\nENVELOPE"
			   + emma._theTruth.getWho().getName() + "\n"
			   + emma._theTruth.getWhere().getName() + "\n"
			   + emma._theTruth.getWeapon().getName() + "\n");

	for (int i=0; i<emma.players.length; i++) {
	    System.out.println("PLAYER" + i + ":\n");
	    for (int j=0; j<emma.numCards; j++) {
		System.out.println(emma.players[i].getCard(j).getName());
	    }
	    System.out.println();
	}
    }
}
