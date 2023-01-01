import java.util.Random;

public class Room {
    private final int r; // row
    private final int c; // column
    private SpecialEvent start;
    private SpecialEvent wumpus;
    private SpecialEvent bats;
    private SpecialEvent pit;
//    private boolean isHallway;

    // Constructor
    public Room(int r, int c){
        this.r = r;
        this.c = c;
        start = SpecialEvent.NONE;
        wumpus = SpecialEvent.NONE;
        bats = SpecialEvent.NONE;
        pit = SpecialEvent.NONE;
    }

    // Return column index of a room
    public int getC() {
        return c;
    }

    // Return row index of a room
    public int getR() {
        return r;
    }

    // Set the room to be maze start
    public void setStart() {
        this.start = SpecialEvent.START;
    }

    // Set the room to have a wumpus
    public void setWumpus() {
        this.wumpus = SpecialEvent.WUMPUS;
    }

    // Set the room to have bats
    public void setBats() {
        this.bats = SpecialEvent.BATS;
    }

    // Set the room to have a pit
    public void setPit() {
        this.pit = SpecialEvent.PIT;
    }

    // Return if a room is the start room
    public boolean isStart() {
        return start == SpecialEvent.START;
    }

    // Return if a room have bats
    public boolean hasBats() {
        return bats == SpecialEvent.BATS;
    }

    // Remove the bats of a room
    public void removeBats(){
        bats = SpecialEvent.NONE;
    }

    // Return if a room have a pit
    public boolean hasPit() {
        return pit == SpecialEvent.PIT;
    }

    // Return if a room have the wumpus
    public boolean hasWumpus() {
        return wumpus == SpecialEvent.WUMPUS;
    }

    @Override
    public String toString(){
        String roomRep = "";
        switch (bats) {
            case NONE -> roomRep += "  ";
            case BATS -> roomRep += "}{";
        }
        switch(start) {
            case NONE -> roomRep += " ";
            case START -> roomRep += "^";
        }
        switch(wumpus) {
            case NONE -> roomRep += " ";
            case WUMPUS -> roomRep += "*";
        }
        switch (pit){
            case NONE -> roomRep += "  ";
            case PIT -> roomRep += "()";
        }
//        roomRep += r + " " + c;
        return roomRep;
    }
}
