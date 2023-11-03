package woodland.Animals;
import woodland.AnimalFunction.LongJumpable;
import woodland.Square;

import java.util.stream.IntStream;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 * Represents a Fox in the woodland game, which is a specific type of Animal.
 * Foxes have the ability to move normally and perform long jumps within the game board.
 */
public class Fox extends Animal implements LongJumpable {

    /**
     * Constructor for the Fox class that sets the name and description.
     *
     * @param name The name of the fox.
     */
	public Fox(String name) {
        super(name);
        setDescription("this is fox");
    }

    /**
     * Defines how a Fox moves on the board. Foxes can move to an adjacent square or perform a long jump
     * up to 3 squares away in a straight line if the move is not directly adjacent.
     *
     * @param oldRow The current row of the fox.
     * @param oldCol The current column of the fox.
     * @param newRow The desired new row for the fox.
     * @param newCol The desired new column for the fox.
     * @param board The game board on which the fox is moving.
     * @return true if the move is successful, false otherwise.
     */
    @Override
    public boolean move(int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
    	int v = abs(newRow - oldRow);
    	int h = abs(newCol - oldCol);
    	if (0 <= newRow && newRow <= 19 && 0 <= newCol && newCol <= 19) {
            if ((v + h) == 1) {
                if (board[newRow][newCol].hasAnimal()) {
                    return false;
                } else if (board[newRow][newCol].hasCreature()){
                    this.lifePoints -= board[newRow][newCol].getCreature().attackValue;
                    return true;
                } else { return true; }
            } else if ((v <= 3 && h == 0) ||(v == 0 && h <= 3)) {
                return jump(oldRow, oldCol, newRow, newCol, board);
            }
    	} else { return false; }
        return false;
    }

    /**
     * Allows the Fox to jump to a new location on the board. This action is successful
     * if the destination square does not contain an animal and if there's a creature, the fox
     * must survive the encounter. The method only supports horizontal or vertical jumps.
     *
     * @param oldRow The current row of the fox before jumping.
     * @param oldCol The current column of the fox before jumping.
     * @param newRow The row to which the fox wants to jump.
     * @param newCol The column to which the fox wants to jump.
     * @param board The game board on which the fox is jumping.
     * @return true if the jump is successful, false otherwise.
     */
    @Override
    public boolean jump (int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        if (oldRow == newRow) {
            int[] idxCols = IntStream.rangeClosed(min(oldCol, newCol), max(oldCol, newCol)).filter(i -> i != oldCol).toArray();
            for (int i : idxCols) {
                if (board[newRow][i].hasAnimal()) {
                    return false;
                } else if (board[newRow][i].hasCreature()) {
                    this.lifePoints -= board[newRow][i].getCreature().attackValue;
                    return true;
                }
            }
            return true;
        } else if (oldCol == newCol) {
            int[] idxRows = IntStream.rangeClosed(min(oldRow, newRow), max(oldRow, newRow)).filter(i -> i != oldRow).toArray();
            for (int i : idxRows) {
                if (board[i][newCol].hasAnimal()) {
                    return false;
                } else if (board[i][newCol].hasCreature()) {
                    this.lifePoints -= board[i][newCol].getCreature().attackValue;
                    return true;
                }
            }
            return  true;
        } else { return false; }
    }
}