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

    private String theRules = "\nRULES:\nAt the beginning of play, three cards — one suspect, one weapon, and one room card — are chosen at random and put into a special envelope, so that no one can see them. These cards represent the facts of the case. The remainder of the cards are distributed among the players. Because these cards are not in the envelope, it is known that these are alibis that these nouns are not apart of the murder.\n\nOn a player's turn, she makes a suspicion about the murder: an educated guess at the person, place, and weapon involved. If the player coming next has information, they must present it. If they do not, the next player tries, and so on and so forth until there are no players left. The player's suggestion only gets disproved once. So, though several players may hold cards disproving the suggestion, only the first one will show the suggesting player his or her card.\n\nOnce a player has sufficiently narrowed the solution, that player can make an accusation. According to the rules, 'When you think you have worked out which three cards are in the envelope, you may, on your turn, make an Accusation and name any three elements you want.' Players may name any room (unlike a Suggestion, where a player's character pawn must be in the room the player suggests). The accusing player checks the validity of the accusation by checking the cards, keeping them concealed from other players. If he has made an incorrect accusation, he plays no further part in the game except to reveal cards secretly to one of the remaining players when required to do so in order to disprove suggestions. If the player made a correct accusation, the solution cards are shown to the other players and the game ends.";

    private String theStory = "In 1954 New England, six strangers are invited to a party at a secluded New England mansion. They are met by the house butler Wadsworth, who reminds them that they have been given pseudonyms to protect their true identity. During dinner, the seventh attendee, Mr. Boddy, arrives. After dinner, Wadsworth takes everyone to the study and reveals the true nature of the party: all of the guests are being blackmailed:\n\n\t-Professor Plum is a psychiatrist who lost his medical license because he had an affair with a married female patient. He now works for the United Nations' WHO.\n\t-Mrs. Peacock is the wife of a U.S Senator who has been accused of accepting bribes to deliver her husband's vote. She claims she is innocent but she must pay blackmail money to avoid the story being used for a political witch hunt.\n\t-Miss Scarlet is a madam who operates an illegal brothel and escort service in Washington, D.C.\n\t-Colonel Mustard was a war profiteer who made his money from selling stolen radio components on the black market. He now works at the Pentagon on a private fusion bomb.\n\t-Mrs. White is an alleged 'black widow' who was drawn in to avoid a scandal regarding the mysterious death of her nuclear physicist husband. She was previously married to an illusionist, who also disappeared under mysterious circumstances.\n\t-Mr. Green is a homosexual, a secret that would cost him his job with the State Department if it were widely known.\n\n\nFinally, Wadsworth reveals Mr. Boddy's secret: he is the one who has been blackmailing the others. Wadsworth has gathered all the guests together to confront Mr. Boddy and turn him over to the police. He also reveals this plan is his revenge against Mr. Boddy, whose blackmail had resulted in the suicide of Wadsworth's wife.\n\nMr. Boddy reminds the guests that he can reveal their secrets in police custody and offers them an alternative proposition: by using weapons he has provided (the wrench, the candlestick, the lead pipe, the knife, the revolver and the rope), they can kill Wadsworth and destroy the evidence, keeping their secrets safe. Escape is not an option as Wadsworth holds the only key to the mansion. The guests disperse, and Mr. Boddy is found dead some time later. It is up to the guests, with the help of you detectives, to figure out the details of the murder so that they can save themselves."; //mostly "inspired" by Wikipedia

    private ArrayList<Card> _playingDeck;
    private int _cardsPerPlayer;
    private Player[] _players;
    private int _currentTurn; // Index in _players of which player's turn it is
    private MurderSituation _theTruth;

    public int initAutos(int numFriends) {
	Scanner scan = new Scanner(System.in);
	int total = numFriends + 1;
	System.out.println("How many auto-opponents would you like? (Enter a number from " + (3-total) + " to " + (6-total) + ")");
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

	// Initialize players
	int numFriends = initFriends();
	int numAutos = initAutos(numFriends);
	int numPlayers = numFriends + numAutos + 1;
	_cardsPerPlayer = 18 / numPlayers;
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
	Scanner scan = new Scanner(System.in);
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

	System.out.print(String.format("\033[2J")); //at bottom
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
	    System.out.println("The card revealed to " + _players[currTurn] + " was "+ cardsHad.get(ans).getName()
			       + ". Type anything to continue.");
	    scan.nextLine();
	    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nLet " + _players[currTurn]
			       + " have control now. " + _players[currTurn] + ", type anything to continue.");
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
	System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nLet "
			   + _players[currTurn] + " control now. Type anything to continue.");
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

    public static void main(String[] args) {
	Game emma = new Game();
	emma.initGame();

        System.out.println("ANSWER: " + emma._theTruth);

        while (emma.runTurn()) {}
    }
}
