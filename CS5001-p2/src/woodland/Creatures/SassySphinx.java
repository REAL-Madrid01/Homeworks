package woodland.Creatures;
import woodland.Creatures.Creature;

/**
 * Represents a SassySphinx, a riddle-loving creature in the woodland game.
 * This class can be used to instantiate sphinxes with distinct traits,
 * potentially incorporating riddle-based interactions or challenges.
 */
public class SassySphinx extends Creature {

    /**
     * Constructor for the SassySphinx class.
     * Initializes a new sphinx with the given attributes.
     *
     * @param name        The name of the sphinx.
     * @param shortname   A shorter name or abbreviation for the sphinx.
     * @param attackValue The attack value of the sphinx.
     * @param description A description of the sphinx.
     */
    public SassySphinx(String name, String shortname, int attackValue, String description) {
        super(name, shortname, attackValue, description);
    }
}
