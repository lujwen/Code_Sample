public enum Directions {
    W, E, N, S;

    // return the reverse direction
    public Directions reverse(){
        switch (this){
            case S : return N;
            case N : return S;
            case E : return W;
            case W : return E;
        }
        return null;
    }
}
