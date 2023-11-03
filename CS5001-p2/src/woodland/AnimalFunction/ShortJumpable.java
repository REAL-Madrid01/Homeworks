package woodland.AnimalFunction;

import woodland.Square;

public interface ShortJumpable {

	public abstract boolean jump (int oldRow, int oldCol, int newRow, int newCol, Square[][] board);
}
