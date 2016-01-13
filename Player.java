public abstract class Player {

    //private Notesheet _notes
    private Card[] cards;
    private String _name;

    abstract boolean accuseThisTurn();
    abstract MurderSituation suspect();
    abstract MurderSituation accuse();
}
