import java.util.Scanner;

public class UserIO {
    private Scanner scan;

    public UserIO() {
        scan = new Scanner(System.in);
    }

    public String getLine(Player p) {
        String s = scan.nextLine().trim();
        if ("notes".equals(s)) {
            System.out.println(p.getNotes());
        }
        return s;
    }
}
