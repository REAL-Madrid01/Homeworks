package woodland.AnimalFunction;

import woodland.Square;

public interface Digable {

	public abstract boolean dig (int oldRow, int oldCol, int newRow, int newCol, Square[][] board);
}
