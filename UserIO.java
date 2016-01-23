import java.util.Scanner;

public class UserIO {
    private LivingPlayer player;
    private Scanner scan;

    public UserIO(LivingPlayer p) {
        scan = new Scanner(System.in);
        player = p;
    }

    public String getLine(String prompt) {
        System.out.println(prompt);
        String answer = scan.nextLine().trim();
        if (answer.toLowerCase().equals("notes")) {
            player.enterNotes();
            // Once the user moves forward, ask
            // for the input again:
            getLine(prompt);
        }
        return answer;
    }

    public String getLineLowered(String prompt) {
        return getLine(prompt).toLowerCase();
    }

    public void enterToContinue() {
        System.out.println("(Press Enter to continue)");
        scan.nextLine();
    }
}
