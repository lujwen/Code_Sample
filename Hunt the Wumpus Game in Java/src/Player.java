import java.util.Random;

public class Player {
    private int r;// starting point at the beginning //row
    private int c;// starting point at the beginning //column
    private int arrowSum;

    // constructor
    public Player(int arrowNum){
        arrowSum = arrowNum;
    }

//    public void movePlayerTo(int row, int col){
//        this.r = row;
//        this.c = col;
//    }

    // Subtract one from player's arrow sum
    public void shot(){
        arrowSum--;
    }

    // Return arrowSum
    public int getArrowSum(){
        return arrowSum;
    }

    @Override
    public String toString(){
//        return "Player at row: " + r + " col: " + c +
        return "Player now has " + this.arrowSum +" arrows.";
    }

}
