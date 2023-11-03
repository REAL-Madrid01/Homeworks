package woodland.Creatures;
import woodland.Creatures.Creature;

/**
 * Represents a PrecociousPhoenix, a mythical bird-like creature in the woodland game.
 * This class is designed to create phoenix instances with unique characteristics, potentially
 * allowing for unique resurrection or rebirth abilities typical of phoenix mythology.
 */
public class PrecociousPhoenix extends Creature {

    /**
     * Constructor for the PrecociousPhoenix class.
     * Initializes a new phoenix with the specified attributes.
     *
     * @param name        The name of the phoenix.
     * @param shortname   A shorter name or abbreviation for the phoenix.
     * @param attackValue The attack value of the phoenix.
     * @param description A description of the phoenix.
     */
    public PrecociousPhoenix(String name, String shortname, int attackValue, String description) {
        super(name, shortname, attackValue, description);
    }
}
