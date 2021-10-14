import java.util.Arrays;

public class State {

    int myPoints;

    int oppPoints;

    private int[][] arr = new int[2][7];

    public State() {
        this.myPoints = 0;
        this.oppPoints = 0;
    }

    public int evalFunction() {
        return this.myPoints - oppPoints;
    }

    @Override
    public int hashCode() {
        int res = Arrays.deepHashCode(arr);
        return res;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            throw new NullPointerException("Objekt = 0 in der equals method");
        }

        if (!(o instanceof State)) {
            return false;
        }

        State param = (State) o;

        for (int i = 0; i < this.arr.length; i++) {
            for (int j = 0; j < this.arr[i].length; j++) {
                if (this.arr[i][j] != param.arr[i][j]) {
                    return false;
                }
            }
        }

        return true;

    }

}
