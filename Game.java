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
    private int _cardsPerPlayer;
    private Player[] _players;
    private int _currentTurn; // Index in _players of which player's turn it is
    private MurderSituation _theTruth;

    public int initAutos(int numFriends) {
	Scanner scan = new Scanner(System.in);
	int total = numFriends + 1;
	System.out.println("How many auto-opponents would you like? (Enter a number from "
			   + Integer.toString(3-total) + " to " + Integer.toString(6-total));
	int ans;
	try { ans = Integer.parseInt(scan.nextLine());}
	catch (NumberFormatException nfe) {
	    System.out.println("You did not enter a valid number, so we will assume the minimum.");
	    ans = 3-total;
	}
	return ans;
    }

    public void initGame() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Hello! Welcome to Who-dunitz. What is your name?");
	String p1name = scan.nextLine();
	System.out.println("Thanks, " + p1name + ". How many friends are you playing with? (0-5)");

        // Initialize players
	int numFriends = initFriends();
	int numAutos = initAutos(numFriends);
        int numPlayers = numFriends + numAutos + 1;
	_cardsPerPlayer = 18 / numPlayers;
	_players = new Player[numPlayers];
	LivingPlayer p1 = new LivingPlayer(_cardsPerPlayer, p1name);
	_players = makePlayers(numFriends, p1, numAutos);
        _currentTurn = 0;

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

    public Player[] makePlayers(int numFriends, Player p1, int numAutos) {
	Scanner scan = new Scanner(System.in);
	Player[] retArr = new Player[numFriends + 1 + numAutos];
	retArr[0] = p1;
	for (int i=1; i<=numFriends; i++) {
	    System.out.println("What is the name of player " + (i+1) + "?");
	    retArr[i] = new LivingPlayer(_cardsPerPlayer, scan.nextLine());
	}
	for (int i=numFriends+1; i<=(numFriends+numAutos); i++) {
	    retArr[i] = new AutoPlayer(_cardsPerPlayer, i);
	    System.out.println("AutoPlayer " + (i-1) + " created.");
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
	_playingDeck = shuffle(_playingDeck);
	for (int p=0; p<_players.length; p++) {
	    for (int i=0; i<_cardsPerPlayer; i++) {
		_players[p].addCard(_playingDeck.get(0));
		_playingDeck.remove(0);
	    }
	}
	for (int i=0; i<_playingDeck.size(); i++) { //leftover cards are
	    for (int p=0; p<_players.length; p++) { //shown to all
		_players[p].addCard(_playingDeck.get(0));
	    }
	    _playingDeck.remove(0);
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

    public Player runAccusation(Player activePlayer) {
	MurderSituation guess = activePlayer.accuse(this);
	if (guess.equals(_theTruth)) {
	    System.out.println("Confetti noises! Whoooo! Everyone but " + activePlayer.getName() + " lost!");
	    return activePlayer;
	}
	System.out.println(":( You got it wrong " + activePlayer.getName() + ".");
	System.out.println("It was tragic, but the rest of us have to move on.");
	return null;
	// TODO: mark activePlayer as out while keeping cards available for turn by turn
    }
    
    // runTurn returns a Player to indicate that that Player won the game, or
    // null to indicate that the game will continue
    public Player runTurn() {
	Scanner scan = new Scanner(System.in);
        Player activePlayer = _players[_currentTurn];
        System.out.println("\n\nIt's " + activePlayer.getName() + "'s turn");
        boolean accuse = activePlayer.accuseThisTurn();
        if (accuse) {
            return runAccusation(activePlayer);
        } else {
            MurderSituation guess = activePlayer.suspect(this);
	    getInfo(_currentTurn, guess);
	    System.out.println(activePlayer.getName() +  ", would you like to make an accusation? (T/F)");
	    String ans = scan.nextLine();
	    if (ans.equals("T")) {
		return runAccusation(activePlayer);
	    }
        }
        _currentTurn = (_currentTurn + 1) % _players.length;
        return null;
    }

    public void getInfoMoreLiving(ArrayList<Card> cardsHad, int currTurn) {
	Scanner scan = new Scanner(System.in);
	int ans;
	for (int i=0; i<cardsHad.size(); i++) {
	    System.out.println(i + ": " + cardsHad.get(i).getName());
	}
	System.out.println("Which of these cards would you like to show? ("
			   + 0 + "-" + (cardsHad.size()-1) + ")");
	try { ans = Integer.parseInt(scan.nextLine()); }
	catch (NumberFormatException e) {
	    System.out.println("You did not enter a number. Please try again.");
	    getInfoMoreLiving(cardsHad, currTurn);
	    return ;
	}
	if (!(ans >= 0 && ans < cardsHad.size())) {
	    System.out.println("You did not enter a number in the specified range. Please try again.");
	    getInfoMoreLiving(cardsHad, currTurn);
	    return ;
	} else {
	    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nThe card revealed to "
			       + _players[currTurn].toString() + " was "
			       + cardsHad.get(ans).getName());
	    _players[currTurn].getNotes().crossOff(cardsHad.get(ans));
	    scan.nextLine();
	}
    }

    public void getInfoMoreAuto(ArrayList<Card> cardsHad, int currTurn) {
	Card revealed = cardsHad.get((int)(Math.random() * cardsHad.size()));
	_players[currTurn].getNotes().crossOff(revealed);
	System.out.println("The card revealed to "
			   + _players[currTurn].toString() + " was "
			   + revealed.getName());
    }

    public void getInfoOneLiving(ArrayList<Card> cardsHad, int currTurn, Player toCheck) {
	Scanner scan = new Scanner(System.in);
	System.out.println(toCheck.getName() + ", you only have one card you could show the player." +
			   "You will be revealing " + cardsHad.get(0).getName()
			   + ". Type anything to continue.");
	scan.nextLine();
	System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nThe card revealed to "
			   + _players[currTurn].toString() + " was "
			   + cardsHad.get(0).getName());
	scan.nextLine();
	_players[currTurn].getNotes().crossOff(cardsHad.get(0));
    }

    public void getInfoOneAuto(ArrayList<Card> cardsHad, int currTurn, Player toCheck) {
	_players[currTurn].getNotes().crossOff(cardsHad.get(0));
	System.out.println(toCheck.getName() + " revealed " + cardsHad.get(0).getName() + " to "
			   + _players[currTurn].getName());
    }

    public void getInfo(int currTurn, MurderSituation guess) {
	int changedCurr = currTurn + 0;
	Scanner scan = new Scanner(System.in);
	ArrayList<Card> cardsHad = new ArrayList<Card>();
	int ans = 0;
	boolean bool = true;
	while (bool) {
	    Player toCheck = _players[(changedCurr+1)%_players.length];
	    if (toCheck.getName().equals(_players[currTurn].getName())) {
		System.out.println("No other players have information to negate that suspicion." +
				   " Type anything to continue.");
		scan.nextLine();
		return ;
	    }
	    
	    System.out.println(toCheck.getName() + ", here is the suspicion: \n" + guess.toString());
	    if (toCheck.hasCard(guess.getWho())) { cardsHad.add(guess.getWho()); }
	    if (toCheck.hasCard(guess.getWhere())) { cardsHad.add(guess.getWhere()); }
	    if (toCheck.hasCard(guess.getWeapon())) { cardsHad.add(guess.getWeapon()); }

	    if (cardsHad.size() > 1) {
		if (toCheck instanceof LivingPlayer) { getInfoMoreLiving(cardsHad, currTurn); }
		else { getInfoMoreAuto(cardsHad, currTurn); }
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		return ;
	    } if (cardsHad.size() == 1) {
		if (toCheck instanceof LivingPlayer) { getInfoOneLiving(cardsHad, currTurn, toCheck); }
		else { getInfoOneAuto(cardsHad, currTurn, toCheck); }
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		return ;
	    } else {
		System.out.println(_players[(changedCurr+1)%_players.length].getName() +
				   " possesses no cards involved in the suspicion. Type anything " +
				   "to continue to the next person's attempt to share information.");
	        scan.nextLine();
		changedCurr++;
	    }
	}
    }

    public boolean cardExists(Card s) {
        for (Card c : personCards) {
            if (c.equals(s)) { return true; }
        }
        for (Card c : placeCards) {
            if (c.equals(s)) { return true; }
        }
        for (Card c : weaponCards) {
            if (c.equals(s)) { return true; }
        }
        return false;
    }

    public static void main(String[] args) {
	Game emma = new Game();
	emma.initGame();

        System.out.println("ANSWER: " + emma._theTruth);

        while (emma.runTurn() == null) {}
    }
}
