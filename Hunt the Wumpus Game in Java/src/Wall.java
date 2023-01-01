public class Wall {
    private int orient; // 0 = horizontal, 1 = vertical;
    private int wallType; // 0 = closed, 1 = open, 2 = hallway

    // Constructor
    public Wall(int orientation){
        orient = orientation;
        wallType = 0;
    }

    // Set a wall type: 0 = closed, 1 = open, 2 = hallway
    public void setWallType(int wallType) {
        //throw exception for illegal arguments
        if (wallType != 1 && wallType != 2){
            throw new IllegalArgumentException("Set wall type to be 1(open) or 2(hallway)!");
        }
        this.wallType = wallType;
    }

    // Return wall type
    public int getWallType() {
        return wallType;
    }

    @Override
    public String toString(){
        String wallRep = "";
        if (orient == 0){
            switch (wallType){
                case 0 -> wallRep = "------";
                case 1 -> wallRep = "      ";
                case 2 -> wallRep = "      ";
//                case 2 -> wallRep = " -  - ";
            }
        } else if (orient == 1){
            switch (wallType){
                case 0 -> wallRep = "|";
                case 1 -> wallRep = " ";
                case 2 -> wallRep = " ";
//                case 2 -> wallRep = ":";
            }
        }
        return wallRep;
    }
}
