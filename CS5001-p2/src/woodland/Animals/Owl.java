package woodland.Animals;
import woodland.AnimalFunction.Flyable;
import woodland.Animals.Animal;
import woodland.Square;

import java.util.stream.IntStream;
import static java.lang.Math.*;

/**
 * Represents an Owl in the woodland game, which is a specific type of Animal.
 * Owls have the ability to move normally and fly over the game board.
 */
public class Owl extends Animal implements Flyable {

    /**
     * Constructor for the Owl class that sets the name and description.
     *
     * @param name The name of the owl.
     */
	public Owl(String name) {
        super(name);
        setDescription("this is owl");
    }

    /**
     * Defines how an Owl moves on the board. Owls can move to an adjacent square or fly over
     * the board in a straight line, either horizontally, vertically, or diagonally.
     *
     * @param oldRow The current row of the owl.
     * @param oldCol The current column of the owl.
     * @param newRow The desired new row for the owl.
     * @param newCol The desired new column for the owl.
     * @param board The game board on which the owl is moving.
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
            } else if ((v ==  0 && h > 0) || (v > 0 && h == 0) || (v == h && v > 0)) {
                return fly(oldRow, oldCol, newRow, newCol, board);
            } else { return false; }
    	} else { return false; }
    }

    /**
     * Allows the Owl to fly to a new location on the board. This action is successful
     * if the destination square does not contain an animal and the owl must survive any
     * creature encounters along the path. The fly method supports movement in any straight
     * line including diagonals.
     *
     * @param oldRow The current row of the owl before flying.
     * @param oldCol The current column of the owl before flying.
     * @param newRow The row to which the owl wants to fly.
     * @param newCol The column to which the owl wants to fly.
     * @param board The game board on which the owl is flying.
     * @return true if the fly is successful, false otherwise.
     */
    @Override
    public boolean fly (int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        if (oldRow == newRow) {
            int[] idxCols = IntStream.rangeClosed(min(oldCol, newCol), max(oldCol, newCol)).filter(i -> i != oldCol).toArray();
            for (int i : idxCols) {
                if (board[newRow][i].hasCreature()) {
                    if(board[newRow][i].hasAnimal()){
                        return false;
                    } else {
                        this.lifePoints -= board[newRow][i].getCreature().attackValue;
                        return true;
                    }
                }
            }
            if (board[newRow][newCol].hasAnimal()) {
                return false;
            } else { return true; }
        } else if (oldCol == newCol) {
            int[] idxRows = IntStream.rangeClosed(min(oldRow, newRow), max(oldRow, newRow)).filter(i -> i != oldRow).toArray();
            for (int i : idxRows) {
                if (board[i][newCol].hasCreature()) {
                    if (board[i][newCol].hasAnimal()) {
                        return false;
                    } else {
                        this.lifePoints -= board[i][newCol].getCreature().attackValue;
                        return true;
                    }
                }
            }
            if (board[newRow][newCol].hasAnimal()) {
                return false;
            } else { return true; }
        } else if (oldRow != newRow && oldCol != newCol) {
            int[] idxCols = IntStream.rangeClosed(min(oldCol, newCol), max(oldCol, newCol)).filter(i -> i != oldCol).toArray();
            int[] idxRows = IntStream.rangeClosed(min(oldRow, newRow), max(oldRow, newRow)).filter(i -> i != oldRow).toArray();
            for (int i = 0; i < idxRows.length; i++) {
                if (board[idxRows[i]][idxCols[i]].hasCreature()) {
                    if (board[idxRows[i]][idxCols[i]].hasAnimal()){
                        return false;
                    } else {
                        this.lifePoints -= board[idxRows[i]][idxCols[i]].getCreature().attackValue;
                        return true;
                    }
                }
            }
            if (board[newRow][newCol].hasAnimal()) {
                return false;
            } else { return true; }
        } else { return false; }
    }
}

