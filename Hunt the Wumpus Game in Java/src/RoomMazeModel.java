import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class RoomMazeModel implements Model{

    private RoomMaze maze;
    private final int col; // size of maze
    private final int row; // size of maze

    private Player player;
    private int playerR; // player position
    private int playerC; // player position
    private Room playerPos; // player position
    private Room arrowPos; // arrow position
    private boolean gameOver; // whether to end the game

    // constructor
    public RoomMazeModel(int mazeC, int mazeR, int batsNum, int pitNum, int arrowNum){
        maze = new RoomMaze(mazeC, mazeR, batsNum, pitNum);
        player = new Player(arrowNum);
        col = mazeC;
        row = mazeR;
        gameOver = false;
    }

    @Override
    // return if game is over
    public boolean getGameOver() {
        return gameOver;
    }

    @Override
    // update player's position to the starting point of the maze.
    public void startGame() {
        Room start = maze.getStart();
        updatePlayerAt(maze.roomGetInd(start));
    }

    @Override
    // Check player's position, where he can go to and provide warnings
    public void checkGameState() {
        System.out.println(maze.toString()); //print the maze
        int roomIndex = maze.roomGetInd(playerPos);
        HashSet<Room> nextRooms = maze.nextRooms(playerR,playerC);
        ArrayList nextRoomInt = new ArrayList<>();
        String warning = "";
        for (Room room : nextRooms){
//            System.out.println(room.toString());
            nextRoomInt.add(maze.roomGetInd(room));
            if (room.hasWumpus()){
                warning += "\nYou smell a wumpus!";
            }
            if (room.hasPit()){
                warning += "\nYou fell a cold wind blowing!";
            }
        }
        Collections.sort(nextRoomInt);
        System.out.println("You are in cave " + roomIndex + " that connects to "
                + nextRoomInt.toString() + warning);
    }

    @Override
    // change player's position based on user's roomChoice input
    public void moveTowards(int roomChoice) {
        updatePlayerAt(roomChoice);
        if (playerPos.hasWumpus()){
            System.out.println("Chomp, chomp, chomp, thanks for feeding the Wumpus!");
            System.out.println("Better luck next time");
            gameOver = true;
        }
        if(playerPos.hasBats()){
            Random rand = new Random();
            playerPos.removeBats();
            double p = rand.nextDouble(1);
            if (p<0.5){
                System.out.println("You are whisked away by superbats and ...");
                int newRoomIndex = randomRoomIndex();
                moveTowards(newRoomIndex);
            } else {
                System.out.println("Whoa -- you successfully duck superbats that try to grab you");
            }
        }
        if(playerPos.hasPit()){
            System.out.println("You fell into a pit!");
            System.out.println("Better luck next time");
            gameOver = true;
        }
    }

    @Override
    // shot an arrow based on user's shotDist and roomChoice input
    public void shotTowards(int shotDist, int roomChoice) {
        player.shot();
        arrowPos = maze.getRoom(roomChoice / col, roomChoice % col);
        Directions d = maze.getDirection2Rooms(arrowPos, playerPos);
        arrowPos = maze.moveByDist(arrowPos, d.reverse(), shotDist-1);
        if(arrowPos == null){
            System.out.println("Woops! Your arrow got stuck on a wall.");
            System.out.println(player.toString());
        } else if(arrowPos.hasWumpus()){
            System.out.println("Hee hee hee, you got the wumpus!");
            System.out.println("Next time you won't be so lucky");
            gameOver = true;
        } else if(arrowPos.hasPit()){
            System.out.println("Your arrow fell into a bottomless hole.");
            System.out.println(player.toString());
        } else {
            System.out.println("Your arrow missed in a room.");
            System.out.println(player.toString());
        }
        if (player.getArrowSum() == 0){
            System.out.println("Your ran out of arrows!");
            gameOver = true;
        }
    }

    @Override
    // determines what will happen when game is over
    public void gameOver(){
        System.out.println("Game over!");
    }

    // Helper function to update player's position based on the room index number
    private void updatePlayerAt(int index){
        playerR = index / col;
        playerC = index % col;
//        player.movePlayerTo(playerR, playerC);
        playerPos = maze.getRoom(playerR, playerC);
    }

    // Helper function to generate a random room number
    // Used when player caught by bats
    private int randomRoomIndex(){
        Random rand = new Random();
        int roomIndex = rand.nextInt(maze.getRoomList().size());
        return roomIndex;
    }

//    public static void main(String[] args) {
//        RoomMazeModel m = new RoomMazeModel(6,4,3,3,3);
//        m.startGame();
//        m.checkGameState();
//    }
}
