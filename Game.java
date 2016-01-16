import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    // Config:
    public static final Card[] personCards = {
        new Card("Col. Mustard", 0),
        new Card("Prof. Plum", 0),
        new Card("Mr. Green", 0),
        new Card("Mrs. Peacock", 0),
        new Card("Miss Scarlet", 0),
        new Card("Mrs. White", 0)
    };
    public static final Card[] placeCards = {
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
    public static final Card[] weaponCards = {
        new Card("Knife", 2),
        new Card("Candlestick", 2),
        new Card("Revolver", 2),
        new Card("Rope", 2),
        new Card("Lead Pipe", 2),
        new Card("Wrench", 2)
    };

    private ArrayList<Card> _playingDeck;
    private int _currentTurn;
    private int _cardsPerPlayer;
    private Player[] _players;
    private MurderSituation _theTruth;

    public void initGame() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Hello! Welcome to Who-dunitz. What is your name?");
	String p1name = scan.nextLine();
	System.out.println("Thanks, " + p1name + ". How many friends are you playing with? (0-5)");

        // Initialize players
	int numFriends = initFriends();
        int numPlayers = numFriends + 1;
	_cardsPerPlayer = 18 / numPlayers;
	_players = new Player[numPlayers];
	LivingPlayer p1 = new LivingPlayer(_cardsPerPlayer, p1name);
	_players = makePlayers(numFriends, p1);

        // Fill the deck
	_playingDeck = new ArrayList<Card>();
	for (int i=0; i<personCards.length; i++) { _playingDeck.add(personCards[i]); }
	for (int i=0; i<placeCards.length; i++) { _playingDeck.add(placeCards[i]); }
	for (int i=0; i<weaponCards.length; i++) { _playingDeck.add(weaponCards[i]); }

        // Put cards where they need to be
	fillEnvelope();
	dealCards(_cardsPerPlayer);
	startNotes();
    }

    public void startNotes() {
	for (int p=0; p<_players.length; p++) {
	    for (int i=0; i<_cardsPerPlayer; i++) {
		_players[p].getNotes().crossOff(_players[p].getCard(i));
	    }
	}
    }

    public Player[] makePlayers(int numFriends, Player p1) {
	Scanner scan = new Scanner(System.in);
	Player[] retArr = new Player[numFriends + 1];
	retArr[0] = p1;
	for (int i=1; i<=numFriends; i++) {
	    System.out.println("What is the name of player " + (i+1) + "?");
	    retArr[i] = new LivingPlayer(_cardsPerPlayer, scan.nextLine());
	}
	return retArr;
    }

    public void fillEnvelope() {
	Random generator = new Random();
	int rand = generator.nextInt(personCards.length);
	Card theWho = personCards[rand];
	_playingDeck.remove(theWho);

	rand = generator.nextInt(placeCards.length);
	Card thePlace = placeCards[rand];
	_playingDeck.remove(thePlace);

	rand = generator.nextInt(weaponCards.length);
	Card theWeapon = weaponCards[rand];
	_playingDeck.remove(theWeapon);

	_theTruth = new MurderSituation(theWho, thePlace, theWeapon);
    }

    public void dealCards(int _cardsPerPlayer) {
	for (int p=0; p<_players.length; p++) {
	    for (int i=0; i<_cardsPerPlayer; i++) {
		_players[p].addCard(_playingDeck.get(0));
		_playingDeck.remove(0);
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
	    int numFriends = Integer.parseInt(scan.nextLine());
	    if (!(numFriends > 0 && numFriends < 6)) {
		System.out.println("You did not enter a number from 0-5. Please try again.");
		return initFriends();
	    } else {
		return numFriends;
	    }
	} catch (NumberFormatException nfe) {
	    System.out.println("It seems you didn't enter a number. Please try again.");
	    return initFriends();
	}
    }

    public static void main(String[] args) {
	Game emma = new Game();
	emma.initGame();

	for (int j=0; j<emma._cardsPerPlayer; j++) {
	    System.out.println(emma._players[1].getCard(j).getName());
	}
	System.out.println(emma._players[1].getNotes().toString());
    }
}
