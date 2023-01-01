public class Driver {

    private static final int mazeC = 5;
    private static final int mazeR = 5;
    private static final int batsNum = 2;
    private static final int pitNum = 2;
    private static final int arrowNum = 3;

    public static void main(String[] args) {
        Controller c = new Controller(mazeC, mazeR, batsNum, pitNum, arrowNum);
        c.startGame();
    }
}
