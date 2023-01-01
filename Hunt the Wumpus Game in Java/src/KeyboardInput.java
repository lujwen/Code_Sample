import java.util.Scanner;

public class KeyboardInput implements GameInput{

    Scanner keyboard = new Scanner(System.in);

    // Helper function to transfer user's input to String
    private String read(){
        String answer = keyboard.nextLine();
        answer = answer.toUpperCase();
        return answer;
    }

    @Override
    // Prompt user to choose between shot and move
    public ShotOrMove shotOrMove() {
        System.out.println("Shot or move (S-M)? ");
        String answer = read();
        if (answer.equals("S")) {
            return ShotOrMove.SHOT;
        } else if (answer.equals("M")){
            return ShotOrMove.MOVE;
        }
        return null;
    }

    @Override
    // Prompt user to input which room he wants to move towards
    public int moveTowards() {
        System.out.println("Where to? ");
        String answer = read();
        return Integer.parseInt(answer);
    }

    @Override
    // Prompt user to input the distance he wants to shot to
    public int shotDistances() {
        System.out.println("Distance (1-5)? ");
        String answer = read();
        return Integer.parseInt(answer);
    }

    @Override
    // Prompt user to input which room he wants to shot towards
    public int shotTowards() {
        System.out.println("Towards cave? ");
        String answer = read();
        return Integer.parseInt(answer);
    }

}
