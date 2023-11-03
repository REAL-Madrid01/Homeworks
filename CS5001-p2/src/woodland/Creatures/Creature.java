package woodland.Creatures;
import woodland.Animals.Animal;
import woodland.Square;

import java.util.*;

/**
 * Represents a Creature in the woodland game.
 * Creatures have an attack value and can charm animals, be confused, and lose or recover magic.
 */
public class Creature {
	public String name;
	public String shortname;
	public int attackValue;
	public int storeAttackValue;
	public int attackValid;
	public Map<String, Animal> charmAnimal = new LinkedHashMap<>();
	public boolean confused = false;
	public String description;
	public List<Animal> shieldAnimal;
	public Square square;

	/**
	 * Constructor for the Creature class.
	 *
	 * @param name        The name of the creature.
	 * @param shortname   A shorter name or abbreviation for the creature.
	 * @param attackValue The attack value of the creature.
	 * @param description A description of the creature.
	 */
	public Creature (String name, String shortname, int attackValue, String description) {
		this.name = name;
		this.shortname = shortname;
		this.attackValue = attackValue;
		this.storeAttackValue = attackValue;
		this.description = description;
		this.attackValid = 0;
	}

	/**
	 * Gets the current square on which the creature is located.
	 *
	 * @return The square object representing the creature's location.
	 */
	public Square getSquare(){
		return this.square;
	}

	/**
	 * Sets the description for the creature.
	 *
	 * @param description The new description of the creature.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the square on which the creature is located.
	 *
	 * @param square The square object representing the creature's new location.
	 */
	public void setSquare(Square square) {
		this.square = square;
	}

	/**
	 * Causes the creature to lose its magic, setting its attack value to zero.
	 */
	public void loseMagic() {
		this.attackValue = 0;
	}

	/**
	 * Causes the creature to recover its magic, restoring its original attack value.
	 */
	public void recoverMagic() {
		this.attackValue = storeAttackValue;
	}

	/**
	 * Adds an animal to the list of animals charmed by this creature.
	 *
	 * @param animal The animal that is being charmed.
	 */
	public void addCharmedAnimals(Animal animal) {
		this.charmAnimal.put(animal.name, animal);
	}

	/**
	 * Clears the list of animals charmed by this creature.
	 */
	public void clearCharmedAnimals() {
		this.charmAnimal.clear();
	}
}