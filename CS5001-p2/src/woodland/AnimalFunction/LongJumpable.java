
package woodland.AnimalFunction;

import woodland.Square;

public interface LongJumpable {

	public abstract boolean jump (int oldRow, int oldCol, int newRow, int newCol, Square[][] board);
}
