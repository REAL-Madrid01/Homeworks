package woodland.Animals;
import woodland.AnimalFunction.ShortJumpable;
import woodland.Square;

import static java.lang.Math.abs;

/**
 * Represents a Rabbit in the woodland game, which is a specific type of Animal.
 * Rabbits have the ability to move normally and perform short jumps within the game board.
 */
public class Rabbit extends Animal implements ShortJumpable {

    /**
     * Constructor for the Rabbit class that sets the name and description.
     *
     * @param name The name of the rabbit.
     */
	public Rabbit(String name) {
        super(name);
        setDescription("this is rabbit");
    }

    /**
     * Defines how a Rabbit moves on the board. Rabbits can move to an adjacent square or perform a short jump
     * up to 2 squares away in any direction if the move is not directly adjacent.
     *
     * @param oldRow The current row of the rabbit.
     * @param oldCol The current column of the rabbit.
     * @param newRow The desired new row for the rabbit.
     * @param newCol The desired new column for the rabbit.
     * @param board The game board on which the rabbit is moving.
     * @return true if the move is successful, false otherwise.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
    	int v = abs(newRow - oldRow);
    	int h = abs(newCol - oldCol);
    	if (0 <= newRow && newRow <= 19 && 0 <= newCol && newCol <= 19) {
            if (v <= 1 && h <= 1 && (v + h) != 0) {
                if (board[newRow][newCol].hasAnimal()) {
                    return false;
                } else if (board[newRow][newCol].hasCreature()){
                    this.lifePoints -= board[newRow][newCol].getCreature().attackValue;
                    return true;
                } else {
                    return true;
                }
            } else if ((v == 2 && h == 0) || (v == 0 && h == 2) || (v == 2 && h == 2)) {
                return jump(oldRow, oldCol, newRow, newCol, board);
            }
    	} else { return false; }
        return false;
    }

    /**
     * Allows the Rabbit to jump to a new location on the board. This action is successful
     * if the destination square does not contain an animal and if there's a creature in the path,
     * the rabbit must survive the encounter. The jump method supports up to 2 squares away in any direction.
     *
     * @param oldRow The current row of the rabbit before jumping.
     * @param oldCol The current column of the rabbit before jumping.
     * @param newRow The row to which the rabbit wants to jump.
     * @param newCol The column to which the rabbit wants to jump.
     * @param board The game board on which the rabbit is jumping.
     * @return true if the jump is successful, false otherwise.
     */
    @Override
    public boolean jump (int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        int idx1Row = oldRow + (newRow - oldRow) / 2;
        int idx1Col = oldCol + (newCol - oldCol) / 2;
    	if (board[idx1Row][idx1Col].hasAnimal()) {
            return false;
        } else if (board[newRow][newCol].hasAnimal()) {
            return false;
        } else if (board[idx1Row][idx1Col].hasCreature()){
            this.lifePoints -= board[idx1Row][idx1Col].getCreature().attackValue;
            return true;
        } else if (board[newRow][newCol].hasCreature()) {
            this.lifePoints -= board[newRow][newCol].getCreature().attackValue;
            return true;
        } else { return true; }
    }
}