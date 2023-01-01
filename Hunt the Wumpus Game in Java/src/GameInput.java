public interface GameInput {

    // Prompt user to choose between shot and move
    ShotOrMove shotOrMove();

    // Prompt user to input which room he wants to move towards
    int moveTowards();

    // Prompt user to input the distance he wants to shot to
    int shotDistances();

    // Prompt user to input which room he wants to shot towards
    int shotTowards();
}
