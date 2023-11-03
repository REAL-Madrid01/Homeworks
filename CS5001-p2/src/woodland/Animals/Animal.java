package woodland.Animals;

import woodland.Spells.Spell;
import woodland.Square;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Math.min;

/**
 * Represents an animal in the woodland game. Each animal has a name, a set of spells,
 * life points, and occupies a square on the game board.
 */
public class Animal {

	public String name = "";

	public Map<Integer, Spell> spells = new LinkedHashMap<>();
	public int lifePoints = 100;
	public String description = "";
	public Square square;

	/**
	 * Constructor for creating a new Animal with a given name.
	 * It also initializes the animal with a set of default spells.
	 *
	 * @param name The name of the animal.
	 */
	public Animal (String name) {
		this.name = name;
		Spell Detect = new Spell("Detect");
		Spell Heal = new Spell("Heal");
		Spell Shield = new Spell("Shield");
		Spell Confuse = new Spell("Confuse");
		Spell Charm = new Spell("Charm");
		this.spells.put(0, Detect);
		this.spells.put(10, Heal);
		this.spells.put(20, Shield);
		this.spells.put(30, Confuse);
		this.spells.put(40, Charm);
	}

	/**
	 * Retrieves the square that the animal is currently on.
	 *
	 * @return The current square of the animal.
	 */
	public Square getSquare() {
		return this.square;
	}

	/**
	 * Attempts to move the animal from one square to another.
	 * This method is a placeholder and needs to be implemented.
	 *
	 * @param oldRow The current row of the animal.
	 * @param oldCol The current column of the animal.
	 * @param newRow The desired new row for the animal.
	 * @param newCol The desired new column for the animal.
	 * @param board The game board on which the animal is moving.
	 * @return true if the move is successful, false otherwise.
	 */
	public boolean move(int oldRow, int oldCol, int newRow, int newCol, Square[][] board) {
        return false;
    }

	/**
	 * Heals the animal by increasing its life points, up to a maximum of 100.
	 */
	public void heal() {
		this.lifePoints = min(this.lifePoints + 10, 100);
	}

	/**
	 * Reduces the animal's life points by a specified attack value.
	 *
	 * @param attackValue The amount of damage to inflict on the animal.
	 */
	public void attacked(int attackValue) {
		this.lifePoints -= attackValue;
	}

	/**
	 * Checks if the animal is still alive based on its life points.
	 *
	 * @return true if the animal is alive (life points > 0), false otherwise.
	 */
	public boolean isAlive() {
		if (this.lifePoints <= 0){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Updates the usage count of a spell for this animal.
	 *
	 * @param animal The animal whose spell usage is being updated.
	 * @param spell The spell to update.
	 * @param flag If true, increment the usage count; if false, decrement it.
	 */
	public void updateSpell(Animal animal, Spell spell, boolean flag) {
		for (Map.Entry<Integer, Spell> entry : animal.spells.entrySet()) {
			if (entry.getValue().spellName.contains(spell.spellName)) {
				int count = 0;
				if (flag) {
					count = entry.getKey() + 1;
				} else { count = entry.getKey() - 1; }
				deleteSpell(spell, animal);
				animal.spells.put(count, spell);
				break;
			}
		}
	}

	/**
	 * Removes a spell from the animal's list of spells.
	 *
	 * @param spell The spell to remove.
	 * @param animal The animal from whose spell list to remove the spell.
	 */
	public void deleteSpell(Spell spell, Animal animal) {
		int match = 0;
		for (Map.Entry<Integer, Spell> entry : animal.spells.entrySet()) {
			if (entry.getValue().spellName.contains(spell.spellName)) {
					match = entry.getKey();
					break;
			}
		}
		animal.spells.remove(match);
	}

	/**
	 * Sets the description for the animal.
	 *
	 * @param description The new description for the animal.
	 */
	public void setDescription(String description) {
        this.description = description;
    }

	/**
	 * Sets the current square of the animal on the game board.
	 *
	 * @param square The square to place the animal on.
	 */
	public void setSquare(Square square) {
		this.square = square;
	}
}