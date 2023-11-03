package woodland.Animals;
import woodland.AnimalFunction.Digable;
import woodland.Animals.Animal;
import woodland.Square;

import static java.lang.Math.abs;

/**
 * Represents a Badger in the woodland game, which is a specific type of Animal.
 * Badgers have the ability to move and dig within the game board.
 */
public class Badger extends Animal implements Digable {

    /**
     * Constructor for the Badger class that sets the name and description.
     *
     * @param name The name of the badger.
     */
	public Badger(String name) {
        super(name);
        setDescription("this is badger");
    }

    /**
     * Defines how a Badger moves on the board. Badgers can move to an adjacent square or dig
     * two squares away in any direction if the move is not directly adjacent.
     *
     * @param oldRow The current row of the badger.
     * @param oldCol The current column of the badger.
     * @param newRow The desired new row for the badger.
     * @param newCol The desired new column for the badger.
     * @param board The game board on which the badger is moving.
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
            } else if ((v == 2 && h == 0) || (v == 0 && h == 2) || (v == 2 && h == 2)) {
                return dig(oldRow, oldCol, newRow, newCol, board);
            }
    	} else { return false; }
        return false;
    }

    /**
     * Allows the Badger to dig to a new location on the board. This action is only successful
     * if the destination square does not contain an animal or if there's a creature, the badger
     * must survive the encounter.
     *
     * @param oldRow The current row of the badger before digging.
     * @param oldCol The current column of the badger before digging.
     * @param newRow The row to which the badger wants to dig.
     * @param newCol The column to which the badger wants to dig.
     * @param board The game board on which the badger is digging.
     * @return true if the dig is successful, false otherwise.
     */
    @Override
    public boolean dig(int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        if (board[newRow][newCol].hasAnimal()) {
            return false;
        } else if (board[newRow][newCol].hasCreature()){
            this.lifePoints -= board[newRow][newCol].getCreature().attackValue;
            return true;
        } else { return true; }
    }
}