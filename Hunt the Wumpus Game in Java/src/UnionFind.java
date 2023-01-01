public class UnionFind {
    private int[] parent; // Integer array to store the parent of an index

    // Constructor
    public UnionFind(int n){
        parent = new int[n];
        for (int i=0; i<n; i++){
            parent[i] = -1;
        }
    }

    // Returns the root of the set the room belongs to. Path-compression is employed.
    private int find(int roomIndex){
        int temp = roomIndex;
        int root;
        while(parent[temp] >= 0){
            temp = parent[temp];
        }
        root = temp;
        temp = roomIndex;
        while (parent[temp] >= 0){
            int currentRoot = parent[temp];
            parent[temp] = root;
            temp = currentRoot;
        }
        return root;
    }

    // Return if two rooms are connected.
    public boolean isConnected(int room1, int room2){
        if (find(room1) == find(room2)){
            return true;
        }
        return false;
    }

    // Connect two rooms.
    public void connect(int room1, int room2){
        int size1 = -parent[find(room1)];
        int size2 = -parent[find(room2)];
        int newSize = size1 + size2;
        if (size1 >= size2) {
            parent[find(room1)] = -newSize;
            parent[find(room2)] = find(room1);
        } else {
            parent[find(room2)] = -newSize;
            parent[find(room1)] = find(room2);
        }
    }
}
