public interface Model {

    // return if game is over
    boolean getGameOver();

    // update player's position to the starting point of the maze.
    void startGame();

    // Check player's position, where he can go to and provide warnings
    void checkGameState();

    // change player's position based on user's roomChoice input
    void moveTowards(int roomChoice);

    // shot an arrow based on user's shotDist and roomChoice input
    void shotTowards(int shotDist, int roomChoice);

    // determines what will happen when game is over
    void gameOver();

}
