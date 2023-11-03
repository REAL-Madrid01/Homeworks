package woodland.Creatures;

/**
 * Represents a DeceptiveDragon, which is a specific type of Creature in the woodland game.
 * This class can be used to create dragons with distinct names, shortnames, attack values,
 * and descriptions, potentially to be utilized for special behaviors or rules that apply only
 * to dragons in the game.
 */
public class DeceptiveDragon extends Creature {

    /**
     * Constructor for the DeceptiveDragon class, initializing it with the provided name,
     * shortname, attack value, and description.
     *
     * @param name        The name of the dragon.
     * @param shortname   A shorter name or abbreviation for the dragon.
     * @param attackValue The attack value of the dragon.
     * @param description A description of the dragon.
     */
    public DeceptiveDragon(String name, String shortname, int attackValue, String description) {
        super(name, shortname, attackValue, description);
    }
}
