package woodland.Spells;
import woodland.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Spell in the woodland game.
 * Spells are likely to be cast by creatures or animals and might have various effects,
 * which should be detailed in the game's logic.
 */
public class Spell {
    public String spellName;
    public String description;
    public Square square;

    /**
     * Constructor for the Spell class.
     * Initializes a new spell with the specified name.
     *
     * @param spellName The name of the spell.
     */
    public Spell(String spellName) {
        this.spellName = spellName;
    }

    /**
     * Sets the square associated with this spell.
     * This might represent the square on which the spell is cast.
     *
     * @param square The square to associate with the spell.
     */
    public void setSquare(Square square) {
        this.square = square;
    }

    /**
     * Gets the square associated with this spell.
     * This might represent the square on which the spell is active.
     *
     * @return The square associated with the spell.
     */
    public Square getSpell() {
        return this.square;
    }
}
