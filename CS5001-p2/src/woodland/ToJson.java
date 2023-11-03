package woodland;
import woodland.Animals.Animal;
import woodland.Creatures.Creature;
import woodland.Spells.Spell;

import java.util.Map;

import static java.lang.Math.abs;

public class ToJson {

    public static String ToJson(Game game, String action) {
        Square[][] board = game.getBoard();
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"board\"" + ":").append("[");
        for (int i = 0; i < 20; i++) {
            json.append("[");
            for (int j = 0; j < 20; j++) {
                json.append("[");
                if (board[i][j].hasAnimal()) {
                    json.append(Animal2Json(board[i][j].getAnimal()));
                    if (board[i][j].hasCreature() && board[i][j].isVisible()) {
                        json.append(",");
                    }
                }
                if (board[i][j].hasCreature() && board[i][j].isVisible()) {
                    json.append(Creature2Json(board[i][j].getCreature()));
                }
                if (j == 19) {
                    json.append("]");
                } else { json.append("],"); }
            }
            if (i == 19) {
                json.append("]");
            } else { json.append("],"); }
        }
        json.append("],");
        json.append("\"gameOver\"" + ":").append(game.gameOver()).append(",");
        if (action.equals("Spell") ) {
            json.append("\"currentAnimalTurn\"" + ":").append("\"" + game.getAnimalFromTurn((game.turn + 4) % 5).name + "\"").append(",");
            json.append("\"nextAnimalTurn\"" + ":").append("\""+ game.getAnimalFromTurn(game.turn).name + "\"").append(",");
        } else if (action.equals("Move")) {
            json.append("\"currentAnimalTurn\"" + ":").append("\"" + game.getAnimalFromTurn(game.turn).name + "\"").append(",");
            json.append("\"nextAnimalTurn\"" + ":").append("\""+ game.getAnimalFromTurn(game.turn + 1).name + "\"").append(",");
        }
        json.append("\"status\"" + ":").append("\"" + game.status + "\"").append(",");
        //json.append("\"extendedStatus\"" + ":").append("\"" + game.extendStatus + "\"").append(",");
        json.append("\"currentAnimalTurnType\"" + ":").append("\"" + action + "\"");
        json.append("}");
        return json.toString();
    }

    public static String Square2Json(Square square) {
        StringBuilder json = new StringBuilder();

        json.append("[");
        if (square.hasAnimal()) {
            json.append(Animal2Json(square.getAnimal()));
            if (square.hasCreature()) {
                json.append(",");
            }
        }
        if (square.hasCreature()) {
            json.append(Creature2Json(square.getCreature()));
        }
        json.append("]");

        json.append("]");
        return json.toString();
    }

    public static String Animal2Json(Animal animal) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\"" + ":").append("\""+animal.name+"\"").append(",");
        json.append("\"type\"" + ":").append("\"Animal\"").append(",");
        json.append("\"description\"" + ":").append("\"" + animal.description + "\"").append(",");
        json.append("\"life\"" + ":").append(animal.lifePoints).append(",");
        json.append("\"spells\"" + ":" + "[");
        json.append(Spell2json(animal));
        json.append("]");
        json.append("}");
        return json.toString();
    }

    public static String Spell2json(Animal animal) {
        StringBuilder json = new StringBuilder();
        boolean flag = false;
        for (Map.Entry<Integer, Spell> entry : animal.spells.entrySet()) {
            if ((entry.getKey() % 10) > 0) {
                if (flag) {
                    json.append(",{");
                } else {
                    json.append("{");
                }
                json.append("\"name\"" + ":" + "\"" + entry.getValue().spellName + "\"").append(",");
                json.append("\"description\"" + ":" + "\"" + entry.getValue().description + "\"").append(",");
                json.append("\"amount\"" + ":" + entry.getKey() % 10);
                json.append("}");
                flag = true;
            }
        }
        return json.toString();
    }

    public static String Creature2Json(Creature creature) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\"" + ":").append("\"" + creature.name + "\"").append(",");
        json.append("\"type\"" + ":"+ "\""+ "Creature" + "\"" +",");
        json.append("\"shortName\"" + ":").append("\"" + creature.shortname + "\"").append(",");
        json.append("\"description\"" +":").append("\"" + creature.description + "\"").append(",");
        json.append("\"attack\"" + ":").append(creature.storeAttackValue).append(",");
        json.append("\"confused\"" + ":").append(creature.confused).append(",");
        json.append("\"charmed\"" + ":" + "[");
        json.append(Charmed2json(creature));
        json.append("]");
        json.append("}");
        return json.toString();
    }

    public static String Charmed2json(Creature creature) {
        StringBuilder json = new StringBuilder();
        boolean flag = false;
        for (Map.Entry<String, Animal> entry : creature.charmAnimal.entrySet()) {
            if (flag) {
                json.append(",{");
            } else {
                json.append("{");
            }
            json.append("\"name\"" + ":" + "\"" + entry.getValue().name + "\"").append(",");
            json.append("\"turnsLeft\"" + ":" + abs(creature.attackValid));
            json.append("}");
            flag = true;
        }
        return json.toString();
    }
}