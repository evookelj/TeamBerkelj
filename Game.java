import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

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
    private String theStory = readFile("theStory.txt");
    private String theRules = readFile("theRules.txt");

    public String readFile(String fileName) {
        String retStr = "";
        String line = "";
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                retStr += line + "\n";
                // "\n" is appended because BufferedReader does not include
                // the line-termination character(s) in its return value
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) { retStr="Unable to open file '" + fileName + "'"; }
	catch(IOException ex) { retStr="Error reading file '" + fileName + "'"; }
	return retStr;
    }

    public int initAutos(int numFriends) {
	Scanner scan = new Scanner(System.in);
	int total = numFriends + 1;
	System.out.println("How many auto-opponents would you like? (Enter a number from " + (3-total) + " to " + (6-total) + ")");
	int ans;
	try {
	    ans = Integer.parseInt(scan.nextLine());
	    if (!((ans>=(3-total)) && (ans<=(6-total)))) {
		    System.out.println("You did not enter a valid number. Try again");
		    ans = initAutos(numFriends);
	    }
	}
	catch (NumberFormatException nfe) {
	    System.out.println("You did not enter a valid number. Try again");
	    ans = initAutos(numFriends);
	}
	return ans;
    }

    public void initGame() {
	Scanner scan = new Scanner(System.in);
	System.out.println("Hello! Welcome to Who-dunitz. Would you like to start with the rules, the backstory,"
			   + " or go straight into gameplay? (R for rules, B for backstory, G for gamplay)");
	String start = scan.nextLine();
	if (start.equals("R")) {
	    System.out.println(theRules);
	    initGame();
	    return;
	}
	else if (start.equals("B")) {
	    System.out.println(theStory);
	    initGame();
	    return;
	}
	else if (!start.equals("G")) {
	    System.out.println("\nUnexpected input recieved. Please try again.");
	    initGame();
	    return;
	}
	System.out.println("Great! To have the best game experience, please refrain from scrolling up."
			   + "What is your name?");
	String p1name = scan.nextLine();
	System.out.println("Thanks, " + p1name + ". How many friends do you have? (0-5)");

        // Ask user for meta-data about players:
        int numFriends = initFriends();
        String[] friendNames = initFriendNames(numFriends);
        int numAutos = initAutos(numFriends);

        int numPlayers = 1 + numFriends + numAutos;

        // Fill actual state with Players and related infromation
        _cardsPerPlayer = 18 / numPlayers;
        _players = makePlayers(p1name, friendNames, numAutos);
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

    public String[] initFriendNames(int numFriends) {
	Scanner scan = new Scanner(System.in);
	String[] names = new String[numFriends];
	for (int i = 0; i < numFriends; i++) {
	    System.out.println("What is the name of player " + (i+2) + "?");
	    names[i] = scan.nextLine();
	}
	return names;
    }

    public Player[] makePlayers(String p1name, String[] friends, int numAutos) {
        Player[] ps = new Player[1 + friends.length + numAutos];

        // Add player1
        ps[0] = new LivingPlayer(_cardsPerPlayer, p1name);

        // Add friends
        for (int i = 0; i < friends.length; i ++) {
            ps[i + 1] = new LivingPlayer(_cardsPerPlayer, friends[i]);
        }

        // Create and add AutoPlayers
        for (int i = 1 + friends.length; i < ps.length; i++) {
	    ps[i] = new AutoPlayer(_cardsPerPlayer, i);
	    System.out.println("AutoPlayer " + (i-1) + " created.");
        }

        return ps;
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
            if (numFriends == 0) {
                System.out.println("Wow sorry to hear it...\n*cough* Onward.");
            }
	    if (numFriends < 0 || numFriends >= 6) {
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

    // runAccusation returns whether the accusation was correct
    public boolean runAccusation(Player activePlayer) {
	MurderSituation guess = activePlayer.accuse(this);
	if (guess.equals(_theTruth)) {
	    System.out.println("Confetti noises! Whoooo! Everyone but " + activePlayer.getName() + " lost!");
	    return true;
	}
        activePlayer.setStillPlaying(false);
	System.out.println(":( You got it wrong " + activePlayer.getName() + ".");
	System.out.println("It was tragic, but the rest of us have to move on. Type anything to continue.");
        (new Scanner(System.in)).nextLine();
	return false;
    }

    private void advanceTurn() {
        _currentTurn = (_currentTurn + 1) % _players.length;
    }

    private boolean everyLivingPlayerIsOut() {
        for (Player pl : _players) {
            if (pl instanceof LivingPlayer &&
                pl.getStillPlaying()) {
                return false;
            }
        }
        return true;
    }

    // result of runTurn() represents whether the game is still being played
    public boolean runTurn() {
        if (everyLivingPlayerIsOut()) {
            System.out.println("No more players are in the game!");
            return false;
        }
        // Start the turn of the first player, looking forward from the
        // one at, _currentTurn, which has not yet lost the game
        Player activePlayer = _players[_currentTurn];
        while (!activePlayer.getStillPlaying()) {
            advanceTurn();
            activePlayer = _players[_currentTurn];
        }

	System.out.print(String.format("\033c")); //at bottom
	System.out.println("------------------------------------------------------------");
        System.out.println("It's " + activePlayer.getName() + "'s turn");
        boolean accuse = activePlayer.accuseThisTurn();
        if (accuse) {
            boolean success = runAccusation(activePlayer);
            return !success; // The game still goes if player did not succeed
        } else {
	    MurderSituation guess = activePlayer.suspect(this);
	    getInfo(_currentTurn, guess);
	    if (activePlayer.accuseAfterSuspect()) {
		return runAccusation(activePlayer);
	    }
        }
        advanceTurn();
        return true;
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
	    _players[currTurn].getNotes().crossOff(cardsHad.get(ans));
	    System.out.println("\033cThe card revealed to " 
			       + _players[currTurn] + " was "+ cardsHad.get(ans).getName() + ". Type anything to continue.");
	    scan.nextLine();
	    System.out.println(_players[currTurn] + " have control now. " 
			       + _players[currTurn] + ", type anything to continue.");
	    scan.nextLine();
	}
    }

    public void getInfoMoreAuto(ArrayList<Card> cardsHad, int currTurn) {
	Card revealed = cardsHad.get((int)(Math.random() * cardsHad.size()));
	_players[currTurn].getNotes().crossOff(revealed);
	if (_players[currTurn] instanceof LivingPlayer) {
	    System.out.println("The card revealed to you, "  + _players[currTurn] + ", was "
			       + revealed.getName() + ". Type anything to continue.");
	}
	else {
	    System.out.println("A card was revealed to " + _players[currTurn]
			       + ". Type anything to continue.");
	}
	(new Scanner(System.in)).nextLine();
    }

    public void getInfoOneLiving(ArrayList<Card> cardsHad, int currTurn, Player toCheck) {
	Scanner scan = new Scanner(System.in);
	System.out.println("You only have one card you could show the player. You will be revealing "
			   + cardsHad.get(0).getName() + ". Type anything to continue");
	scan.nextLine();
	System.out.println("\033cLet " + _players[currTurn] 
			   + " control now. Type anything to continue.");
	scan.nextLine();
	System.out.println("The card revealed to "  + _players[currTurn] + " was " + cardsHad.get(0).getName());
	scan.nextLine();
	_players[currTurn].getNotes().crossOff(cardsHad.get(0));
    }

    public void getInfoOneAuto(ArrayList<Card> cardsHad, int currTurn, Player toCheck) {
	_players[currTurn].getNotes().crossOff(cardsHad.get(0));
	if (_players[currTurn] instanceof LivingPlayer) {
	    System.out.println(toCheck + " revealed " + cardsHad.get(0).getName() + " to you, "
			       + _players[currTurn] + ". Type anything to continue.");
	}
	else {
	    System.out.println(toCheck + " revealed a card to " + _players[currTurn] + ". Type anything to continue.");
	}
	(new Scanner(System.in)).nextLine();
    }

    public void getInfo(int currTurn, MurderSituation guess) {
	System.out.println();
	int changedCurr = currTurn + 0;
	Scanner scan = new Scanner(System.in);
	ArrayList<Card> cardsHad = new ArrayList<Card>();
	int ans = 0;
	boolean bool = true;
	while (bool) {
	    Player toCheck = _players[(changedCurr+1)%_players.length];
	    if (toCheck instanceof LivingPlayer) {
		System.out.println("Let " +toCheck+ " control the game now. " +toCheck+ ", type once you have control.");
		scan.nextLine();
	    }
	    if (toCheck.getName().equals(_players[currTurn].getName())) {
		System.out.println("No other players have information to negate that suspicion." +
				   " Type anything to continue.");
		scan.nextLine();
		return ;
	    }

	    if (toCheck instanceof LivingPlayer) {
		System.out.println(toCheck + ", " + _players[currTurn] + " suspected that it was " + guess);
	    } else {
		System.out.println(toCheck + " has received the suspicion: " + guess);
	    }
	    if (toCheck.hasCard(guess.getWho())) { cardsHad.add(guess.getWho()); }
	    if (toCheck.hasCard(guess.getWhere())) { cardsHad.add(guess.getWhere()); }
	    if (toCheck.hasCard(guess.getWeapon())) { cardsHad.add(guess.getWeapon()); }

	    if (cardsHad.size() > 1) {
		if (toCheck instanceof LivingPlayer) { getInfoMoreLiving(cardsHad, currTurn); }
		else { getInfoMoreAuto(cardsHad, currTurn); }
		return ;
	    } if (cardsHad.size() == 1) {
		if (toCheck instanceof LivingPlayer) { getInfoOneLiving(cardsHad, currTurn, toCheck); }
		else { getInfoOneAuto(cardsHad, currTurn, toCheck); }
		return ;
	    } else {
		System.out.println("You possess no cards involved in the suspicion.");
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
}
