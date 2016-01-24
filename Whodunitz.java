public class Whodunitz {
    public static void main(String[] args) {
	Game emma = new Game();
	emma.initGame();
        while (emma.runTurn()) {}
    }
}
