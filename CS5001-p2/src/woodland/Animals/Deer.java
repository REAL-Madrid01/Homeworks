package woodland.Animals;
import woodland.AnimalFunction.LongJumpable;
import woodland.Square;

import java.util.stream.IntStream;

import static java.lang.Math.*;

/**
 * Represents a Deer in the woodland game, which is a specific type of Animal.
 * Deer have the ability to move normally and perform long jumps within the game board.
 */
public class Deer extends Animal implements LongJumpable {

    /**
     * Constructor for the Deer class that sets the name and description.
     *
     * @param name The name of the deer.
     */
	public Deer(String name) {
        super(name);
        setDescription("this is deer");
    }

    /**
     * Defines how a Deer moves on the board. Deer can move to an adjacent square or perform a long jump
     * up to 3 squares away in a straight line or diagonally if the move is not directly adjacent.
     *
     * @param oldRow The current row of the deer.
     * @param oldCol The current column of the deer.
     * @param newRow The desired new row for the deer.
     * @param newCol The desired new column for the deer.
     * @param board The game board on which the deer is moving.
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
                } else { return true; }
            } else if ((v <= 3 && h == 0) || (v == 0 && h <= 3) || (v <= 3 && v == h)) {
                return jump(oldRow, oldCol, newRow,newCol, board);
            }
    	} else { return false; }
        return false;
    }

    /**
     * Allows the Deer to jump to a new location on the board. This action is successful
     * if the destination square does not contain an animal and if there's a creature, the deer
     * must survive the encounter. Creatures in the path of the jump become visible.
     *
     * @param oldRow The current row of the deer before jumping.
     * @param oldCol The current column of the deer before jumping.
     * @param newRow The row to which the deer wants to jump.
     * @param newCol The column to which the deer wants to jump.
     * @param board The game board on which the deer is jumping.
     * @return true if the jump is successful, false otherwise.
     */
    @Override
    public boolean jump(int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        if (oldRow == newRow) {
            int[] idxCols = IntStream.rangeClosed(min(oldCol, newCol), max(oldCol, newCol)).filter(i -> i != oldCol).toArray();
            for (int i : idxCols) {
                if (board[newRow][i].hasCreature()) {
                    board[newRow][i].visible = true;
                }
            }
        } else if (oldCol == newCol) {
            int[] idxRows = IntStream.rangeClosed(min(oldRow, newRow), max(oldRow, newRow)).filter(i -> i != oldRow).toArray();
            for (int i : idxRows) {
                if (board[i][newCol].hasCreature()) {
                    board[i][newCol].visible = true;
                }
            }
        } else if (oldRow != newRow && oldCol != newCol) {
            int[] idxCols = IntStream.rangeClosed(min(oldCol, newCol), max(oldCol, newCol)).filter(i -> i != oldCol).toArray();
            int[] idxRows = IntStream.rangeClosed(min(oldRow, newRow), max(oldRow, newRow)).filter(i -> i != oldRow).toArray();
            for (int i = 0; i < idxRows.length; i++) {
                if (board[idxRows[i]][idxCols[i]].hasCreature()) {
                    board[idxRows[i]][idxCols[i]].visible = true;
                }
            }
        }
        if (board[newRow][newCol].hasAnimal()) {
                return false;
        } else if (board[newRow][newCol].hasCreature()) {
                this.lifePoints -= board[newRow][newCol].getCreature().attackValue;
                return true;
        } else { return true; }
    }
}