package woodland.Creatures;

/**
 * Represents a ComplicatedCentaur, a specialized type of Creature in the woodland game.
 * This class currently does not extend the base Creature class with new functionality,
 * but can be used to instantiate centaurs with specific names, shortnames, attack values,
 * and descriptions.
 */
public class ComplicatedCentaur extends Creature {

    /**
     * Constructor for the ComplicatedCentaur class.
     *
     * @param name        The name of the centaur.
     * @param shortname   A shorter name or abbreviation for the centaur.
     * @param attackValue The attack value of the centaur.
     * @param description A description of the centaur.
     */
    public ComplicatedCentaur(String name, String shortname, int attackValue, String description) {
        super(name, shortname, attackValue, description);
    }

}