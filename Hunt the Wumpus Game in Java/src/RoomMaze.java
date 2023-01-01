/*
 1.player can't move between two rooms with a wall;
 2.player can't cross bound
 3.bottomless pit can be at the starting room;
 4.if there's only one accessible direction for the starting room, the starging.next room can have bottomless pit;
 5. tunnel: example's room 11 to room 5 through east tunnel ??
*/

import java.util.*;

public class RoomMaze {

    private final int row; // Number of rows of the maze
    private final int col; // Number of columns of the maze

//    private int mazeSize;
    private UnionFind set; // Records which rooms are connected
    private Room[][] rooms; // Rooms in the maze
    private Room start; // The start room
    private ArrayList<Room> roomList; // Records the rooms (not hallway)

    private final int batsNum;
    private final int pitNum;
    private static final int wumpusNum = 1;

    private Wall[][] horizontalWalls; // Horizontal walls
    private Wall[][] verticalWalls; // Vertical walls
    private final int horizontalWallNum; // Number of horizontal walls
    private final int verticalWallNum; // Number of vertical walls
    private final int wallNum; // number of total inner walls
    private List<Integer> remainingWalls; //remaining walls that are closed
    private Wall[] bound; // outer walls
    private final double hallwayRate = 0.5; // Rate of remaining walls turned into hallways

    // Constructor
    public RoomMaze(int c, int r, int bats, int pit){ //maze(col,row)
        if(c<3 || r<3){
            throw new IllegalArgumentException("Maze size should be at least 3*3.");
        }
        col = c;
        row = r;
        batsNum = bats;
        pitNum = pit;
        set = new UnionFind(r*c);
        horizontalWallNum = col * (row - 1);
        verticalWallNum = (col - 1) * row;
        wallNum = horizontalWallNum + verticalWallNum;
        mazeInitialize();
        mazeSetSpecialEvent();
    }

    // To initialize a maze.
    private void mazeInitialize(){
        generateRooms();
        generateHorizontalWalls();
        generateVerticalWalls();
        initializeBound((col +row) * 2);
        initializeRemainingWalls();
        removeWalls();
        setHallway();
        initiateRoomList();
    }

    // Set special events in the maze
    private void mazeSetSpecialEvent(){
        setStart();
        setWumpus();
        setBats();
        setPit();
    }

    // Return the start room
    public Room getStart(){
        return start;
    }

    // Return the specific room based on the r, c index
    public Room getRoom(int r, int c){
        return rooms[r][c];
    }

    // Return a room's index
    public int roomGetInd(Room r){
        return r.getC() + r.getR() * this.col;
    }

    //  translate room's index to column coordinates
    public int roomIndToCol(int ind){
        return ind % this.col;
    }

    //  translate room's index to row coordinates
    public int roomIndToRow(int ind){
        return ind / this.col;
    }

    // Return the room(not hallway) list in the maze
    public ArrayList<Room> getRoomList(){
        return roomList;
    }

    // Return available rooms(not hallway) connected to current location
    public HashSet<Room> nextRooms(int row, int col){
        HashSet<Room> rooms = new HashSet<>();
        Room self = this.rooms[row][col];
        HashMap<Directions, Room> selfAccessibleNeighbours = getAccessibleCells(row, col);
        for (Room r :selfAccessibleNeighbours.values()){
            HashSet<Room> visited = new HashSet<>();
            visited.add(self);
            rooms.add(findNextRoom(r, visited));
        }
        return rooms;
    }

    // Returns the room in the distance to self in Direction Dir
    public Room moveByDist(Room self, Directions reverseDir, int dist) {
        Room dest = self;
        while (dist>0){
            dest = moveBy1Dist(dest, reverseDir);
            dist--;
            if (dest == null){
                return null; // Means can't reach the next room because there's a wall
            }
        }
        return dest;
    }

    // Find which directions to go from self to other
    public Directions getDirection2Rooms(Room self, Room other){
        HashSet<Room> rooms = new HashSet<>();
        HashMap<Directions, Room> selfAccessibleNeighbours = getAccessibleCells(self.getR(), self.getC());
        for (Directions d:selfAccessibleNeighbours.keySet()){
            HashSet<Room> visited = new HashSet<>();
            Room r = selfAccessibleNeighbours.get(d);
            visited.add(self);
            if (findNextRoom(r, visited) == other){
                return d;
            }
        }
        return null;
    }


    @Override
    public String toString(){
        String maze = "";
        String node = "+";
        String enter = "\n";
        String wallRep;
        String roomRep;
        //Line of top bound walls.
        for(int j=0; j<col; j++){
            maze += node;
            maze += bound[j].toString();
        }
        maze += node + enter;

        for (int i=0; i<row; i++){
            //Line of room and vertical walls.
            wallRep = bound[(i+col)*2].toString();
            maze += wallRep;
            for(int j=0; j<col; j++) {
                roomRep = rooms[i][j].toString();
                maze += roomRep;
                if(j<col-1){
                    wallRep = verticalWalls[i][j].toString();
                    maze += wallRep;
                }
            }
            wallRep = bound[(i+col)*2+1].toString();
            maze += wallRep + enter;
            //line of horizontal walls.
            if (i<row-1){
                for (int j=0; j<col; j++) {
                    maze += node;
                    wallRep = horizontalWalls[i][j].toString();
                    maze += wallRep;
                }
                maze += node + enter;
            }
        }
        //Line of bottom bound walls.
        for(int j=0; j<col; j++){
            maze += node;
            maze += bound[j+col].toString();
        }
        maze += node + enter;
        return maze;
    }

    /* --------------------Helper function of mazeInitialize()---------------------*/
    // To generate a 2D array of rooms in the maze.
    // i:row，j:column ==> [i][j]:row i and column j
    private void generateRooms(){
        rooms = new Room[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Room r = new Room(i, j);
                rooms[i][j] = r;
            }
        }
    }

    // To generate a 2D array of horizontal walls.
    private void generateHorizontalWalls(){
        horizontalWalls = new Wall[row-1][col];
        for (int i=0; i<row-1; i++) {
            for (int j = 0; j<col; j++) {
                horizontalWalls[i][j] = new Wall(0);
            }
        }
    }

    // To generate a 2D array of vertical walls.
    private void generateVerticalWalls(){
        verticalWalls = new Wall[row][col-1];
        for (int i=0; i<row; i++) {
            for (int j=0; j<col-1; j++) {
                verticalWalls[i][j] = new Wall(1);
            }
        }
    }

    // To generate the bound wall of maze.
    private void initializeBound(int n){
        bound = new Wall[n];
        for (int i=0; i<2*col; i++) {
            bound[i] = new Wall(0);
        }
        for (int i=2*col; i<n; i++){
            bound[i] = new Wall(1);
        }
    }

    // initialize the remaining walls(walls that are open)
    private void initializeRemainingWalls(){
        remainingWalls = new ArrayList<>();
        for (int i=0; i<wallNum; i++){
            remainingWalls.add(i);
        }
    }

    // randomly remove walls
    private void removeWalls(){
        Random rand = new Random();
//        rand.setSeed(0);
        int count = 0;
        int randNum;
        int randWall;
        ArrayList<Integer> savedWalls = new ArrayList<>();
        while (count < col * row - 1){
            randNum = rand.nextInt(remainingWalls.size());
            randWall = remainingWalls.remove(randNum);
            int[] roomPair = wallToRoomPair(randWall);
            int room1 = roomCoordToInd(roomPair[0], roomPair[1]);
            int room2 = roomCoordToInd(roomPair[2], roomPair[3]);

            if (set.isConnected(room1, room2)){
                savedWalls.add(randWall);
            } else {
                setWall(randWall, 1);
                set.connect(room1, room2);
                count ++;
            }
        }
        remainingWalls.addAll(savedWalls);
    }

    // randomly change walls to hallways
    private void setHallway(){
        Random rand = new Random();
//        rand.setSeed(0);
        int size = remainingWalls.size();
        int count = 0;
        int randNum;
        int randWall;
        while (count < size * hallwayRate) {
            randNum = rand.nextInt(remainingWalls.size());
            randWall = remainingWalls.remove(randNum);
            int[] roomPair = wallToRoomPair(randWall);
            int room1 = roomCoordToInd(roomPair[0], roomPair[1]);
            int room2 = roomCoordToInd(roomPair[2], roomPair[3]);
            setWall(randWall, 2);
            count++;
        }
    }

    // get room(exclude hallway) list
    private ArrayList<Room> initiateRoomList(){
        roomList = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if(!isHallway(i,j)){
                    roomList.add(rooms[i][j]);
                }
            }
        }
        return roomList;
    }
    /* --------------------Helper function of mazeInitialize()---------------------*/

    /* --------------------Helper function of mazeSetSpecialEvent()---------------------*/
    // Set the starting point at the first room(not hallway)
    private void setStart(){
        start = roomList.get(0);
        start.setStart();
    }

    // Set the wumpus at the last room(not hallway)
    private void setWumpus(){
//        Random rand = new Random();
//        int count = 0;
//        while (count < wumpusNum){
//            int r = rand.nextInt(row);
//            int c = rand.nextInt(col);
//            Room room = rooms[r][c];
//            if (!isHallway(r, c) && !room.isStart()){
//                room.setWumpus();
//                count ++;
//            }
//        }

//        Random rand = new Random();
//        int count = 0;
//        int randNum;
//        Room randRoom;
//        while (count < wumpusNum) {
//            randNum = rand.nextInt(roomList.size());
//            randRoom = roomList.get(randNum);
//            if (!isHallway(randRoom.getR(), randRoom.getC()) && !randRoom.isStart()){
//                randRoom.setWumpus();
//                count++;
//            }
//        }
        roomList.get(roomList.size()-1).setWumpus();
    }

    // Randomly generate the Bats
    private void setBats(){
//        Random rand = new Random();
//        int count = 0;
//        while (count < batsNum){
//            int r = rand.nextInt(row);
//            int c = rand.nextInt(col);
//            Room room = rooms[r][c];
//            if (!isHallway(r, c) && !room.hasWumpus() && !room.isStart()){
//                room.setBats();
//                count ++;
//            }
//        }
        Random rand = new Random();
        int count = 0;
        int randNum;
        Room randRoom;
        while (count < batsNum) {
            randNum = rand.nextInt(roomList.size());
            randRoom = roomList.get(randNum);
            if (!isHallway(randRoom.getR(), randRoom.getC()) && !randRoom.isStart() && !randRoom.hasWumpus()){
                randRoom.setBats();
                count++;
            }
        }
    }

    // Randomly generate the Pit
    private void setPit(){
        Random rand = new Random();
        int count = 0;
        int randNum;
        Room randRoom;
        while (count < pitNum) {
            randNum = rand.nextInt(roomList.size());
            randRoom = roomList.get(randNum);
            if (!isHallway(randRoom.getR(), randRoom.getC()) && !randRoom.isStart() && !randRoom.hasWumpus()){
                randRoom.setPit();
                count++;
            }
        }
    }
    /* --------------------Helper function of mazeSetSpecialEvent()---------------------*/

    /* --------------------Helper function of removeWalls() & setHallway()---------------------*/
    // To covert a 2D coordinate to 1D( each room's index)
    private int roomCoordToInd(int xCoord, int yCoord){
        return xCoord + yCoord * this.col;
    }

    // translate the wall's index to coordinates
    private int[] wallIndexTo2D(int index){
        int[] wall = new int[2];
        if (index < 0 || index >= wallNum){
            throw new IllegalArgumentException("Wall index illegal!");
        }
        if (index < horizontalWallNum){
            wall[0] = index % col; // column index
            wall[1] = index / col; // row index
        } else {
            index -= horizontalWallNum;
            wall[0] = index % (col-1); // column index
            wall[1] = index / (col-1); // row index
        }
        return wall;
    }

    // Return the 2 rooms that's divided by a wall.
    private int[] wallToRoomPair(int index){
        int[] wall = wallIndexTo2D(index);
        int[] roomPair = new int[4];
        roomPair[0] = wall[0];
        roomPair[1] = wall[1];
        if (index < horizontalWallNum){
            roomPair[2] = roomPair[0];
            roomPair[3] = roomPair[1] + 1;
        } else {
            roomPair[2] = roomPair[0] + 1;
            roomPair[3] = roomPair[1];
        }
        return roomPair;
    }

    // get the room pair list by remaining walls
    private ArrayList<ArrayList<Room>> roomPairWithWall(){
        ArrayList<ArrayList<Room>> roomPairList = new ArrayList<>();
        int[] roomPair;
        for(int w: remainingWalls){
            //System.out.println("remaining wall: " + w);
            roomPair = wallToRoomPair(w);
            ArrayList<Room> roomPairs = new ArrayList<Room>();  // two rooms with a wall
            roomPairs.add(rooms[roomPair[1]][roomPair[0]]);
            roomPairs.add(rooms[roomPair[3]][roomPair[2]]);
            roomPairList.add(roomPairs);
            // add reverse
            ArrayList<Room> roomPairsReverse = new ArrayList<Room>();  // two rooms with a wall
            roomPairsReverse.add(rooms[roomPair[3]][roomPair[2]]);
            roomPairsReverse.add(rooms[roomPair[1]][roomPair[0]]);
            roomPairList.add(roomPairsReverse);
        }
        return roomPairList;
    }

    // Set a wall to certain type.
    private void setWall(int index, int wallType){
        int[] wall = wallIndexTo2D(index);
        int x = wall[0];
        int y = wall[1];
        if (index < horizontalWallNum){
            horizontalWalls[y][x].setWallType(wallType);
        } else {
            verticalWalls[y][x].setWallType(wallType);
        }
    }
    /* --------------------Helper function of removeWalls() & setHallway()---------------------*/

    /* ---------------------Helper function to find room and hallway-----------------------*/
    // Helper function of nextRooms();
    // Recursively return a room or a hallway's connected room
    private Room findNextRoom(Room self, HashSet<Room> visited){
        if (!isHallway(self.getR(), self.getC()) && !visited.contains(self)){
            return self;
        }
        Room room = null;
        HashMap<Directions, Room> selfAccessibleNeighbours = getAccessibleCells(self.getR(), self.getC());
        for (Room r :selfAccessibleNeighbours.values()){
            if (!visited.contains(r)){
                visited.add(self);
                room = findNextRoom(r, visited);
                return room;
            }
        }
        return room;
    }

    // Helper function of moveByDist()
    // Returns the room next to self in Direction Dir
    private Room moveBy1Dist(Room self, Directions Dir){
        int selfR = self.getR();
        int selfC = self.getC();
        Room dest = null;
        int destR = selfR;
        int destC = selfC;

        switch (Dir){
            case E -> destC = selfC + 1;
            case W -> destC = selfC - 1;
            case S -> destR = selfR + 1;
            case N -> destR = selfR - 1;
        }
        if (destR >= 0 && destR < row && destC >= 0 && destC < col) {
            dest = rooms[destR][destC];
        }
        if (isAccessible(self, dest)){
            return dest;
        }
        return null;
    }

    // helper function for getAccessibleCells() function
    // help get 4 neighbor(n,s,w,e) cells of each cell,
    private HashMap<Directions, Room> get4Neighbors(int row, int col){
        HashMap<Directions, Room> fourNeighbors = new HashMap<>();
        if (row != 0){
            Room n =  rooms[row-1][col]; // the northern cell
            fourNeighbors.put(Directions.N,n);
        }
        if (row != this.row-1){
            Room s = rooms[row+1][col]; // the southern cell
            fourNeighbors.put(Directions.S,s);
        }
        if (col != 0){
            Room w = rooms[row][col-1]; // the western cell
            fourNeighbors.put(Directions.W,w);
        }
        if (col != this.col-1){
            Room e = rooms[row][col+1]; // the eastern cell
            fourNeighbors.put(Directions.E,e);
        }
        return fourNeighbors;
    }

    // 判断两个房间是否在roomPair里
    // determine whether 2 rooms are divided by an inner wall
    private boolean isAccessible(Room r1, Room r2){
        ArrayList<Room> roomPairs = new ArrayList<>();
        roomPairs.add(r1);
        roomPairs.add(r2);
        return !roomPairWithWall().contains(roomPairs);
    }

    // 判断一个cell能去到的四个邻居cell（没有被墙隔住）
    // find out the accessible neighbor cells(direction and room pair) in 4 neighbors cells
    private HashMap<Directions,Room> getAccessibleCells(int row, int col){
        HashMap<Directions, Room> accessibleCells = new HashMap<>();
        HashMap<Directions, Room> fourNeighbors = get4Neighbors(row, col);
        Room currRoom = rooms[row][col];// index of the room player's in
        for(Directions dir : fourNeighbors.keySet()){
            if (isAccessible(currRoom,fourNeighbors.get(dir))){
                accessibleCells.put(dir, fourNeighbors.get(dir));
            }
        }
        return accessibleCells;
    }

    // determine if a room is hallway
    private boolean isHallway(int row, int col){
        return this.getAccessibleCells(row, col).size() == 2;
    }
    /* ---------------------Helper function to find room and hallway-----------------------*/
}
