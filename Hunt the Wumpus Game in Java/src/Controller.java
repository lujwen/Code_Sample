public class Controller {
    private Model model;
    private GameInput input;
    private ShotOrMove actionChoice;
    private int roomChoice;
    private int shotDist;

    // Constructor
    public Controller(int mazeC, int mazeR, int batsNum, int pitNum, int arrowNum){
        this.model = new RoomMazeModel(mazeC, mazeR, batsNum, pitNum, arrowNum);
        this.input = new KeyboardInput();
        this.actionChoice = null;
        this.roomChoice = 0;
    }

    // Call different functions in model based on different inputs
    public void startGame(){
        model.startGame();
        while(!model.getGameOver()){
            model.checkGameState();
            actionChoice = input.shotOrMove(); //prompt user to make a choice between shot and move;
            if (actionChoice == ShotOrMove.SHOT){
                actionShot();
            } else if (actionChoice == ShotOrMove.MOVE){
                actionMove();
            }
        }
        model.gameOver();
    }

    // Shot and update game situation
    private void actionShot(){
        shotDist = input.shotDistances();
        roomChoice = input.shotTowards();
        model.shotTowards(shotDist, roomChoice);
    }

    // Move and update game situation
    private void actionMove(){
        roomChoice = input.moveTowards();
        model.moveTowards(roomChoice);
    }

}
