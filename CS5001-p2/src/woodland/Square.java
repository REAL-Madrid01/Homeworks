package woodland;
import woodland.Animals.Animal;
import woodland.Creatures.Creature;
import woodland.Spells.Spell;

/**
 * Represents a square on the woodland game board.
 * A square may be visible or hidden and can contain an animal, a creature, or a spell.
 */
public class Square {
	public int row;
	public int col;
	public boolean visible;
	public Animal animal = null;
	public Creature creature = null;
	public Spell spell = null;

	/**
	 * Constructs a Square with specified row, column, and visibility.
	 *
	 * @param row   The row of the square on the board.
	 * @param col   The column of the square on the board.
	 * @param flag  The initial visibility state of the square.
	 */
	public Square(int row, int col, boolean flag) {
		this.row = row;
		this.col = col;
		this.visible = flag;
	}


	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * Reveals the square, changing its visibility to true.
	 */
	public void reveal() {
		this.visible = true;
	}

	public boolean hasCreature() {
		return this.creature != null;
	}

	public boolean hasAnimal() {
		return this.animal != null;
	}

	public boolean hasSpell() { return this.spell != null; }

	public Animal getAnimal() {
		return this.animal;
	}

	public Creature getCreature() { return this.creature; }

	public Spell getSpell() { return this.spell; }

	/**
	 * Sets a creature to the square and associates the square with the creature.
	 *
	 * @param creature The creature to place on the square.
	 */
	public void setCreature(Creature creature) {
		this.creature = creature;
		if (creature != null) {
			creature.setSquare(this);
		}
//		reveal();
	}

	/**
	 * Sets an animal to the square, associates the square with the animal,
	 * and reveals the square.
	 *
	 * @param animal The animal to place on the square.
	 */
	public void setAnimal(Animal animal) {
		this.animal = animal;
		if (animal != null) {
			animal.setSquare(this);
		}
		reveal();
	}

	/**
	 * Sets a spell to the square and associates the square with the spell.
	 *
	 * @param spell The spell to associate with the square.
	 */
	public void setSpell(Spell spell) {
		this.spell = spell;
		if (spell != null) {
			spell.setSquare(this);
		}
//		reveal();
	}
}