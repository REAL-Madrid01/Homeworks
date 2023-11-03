package woodland.Creatures;
import woodland.Creatures.Creature;

/**
 * Represents an UnderAppreciatedUnicorn, a magical equine creature in the woodland game.
 * This class can be utilized to create unicorns with unique properties, potentially with
 * abilities related to healing or other magical effects in keeping with unicorn lore.
 */
public class UnderAppreciatedUnicorn extends Creature {

    /**
     * Constructor for the UnderAppreciatedUnicorn class.
     * Initializes a new unicorn with the provided name, shortname, attack value, and description.
     *
     * @param name        The name of the unicorn.
     * @param shortname   A shorter name or abbreviation for the unicorn.
     * @param attackValue The attack value of the unicorn.
     * @param description A description of the unicorn.
     */
    public UnderAppreciatedUnicorn(String name, String shortname, int attackValue, String description) {
        super(name, shortname, attackValue, description);
    }

}
