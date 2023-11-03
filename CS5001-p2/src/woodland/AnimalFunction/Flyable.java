package woodland.AnimalFunction;

import woodland.Square;

public interface Flyable {

	public abstract boolean fly (int oldRow, int oldCol, int newRow, int newCol, Square[][] board);
}
