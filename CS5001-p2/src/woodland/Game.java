package woodland;
import woodland.Animals.Animal;
import woodland.Animals.Rabbit;
import woodland.Animals.Fox;
import woodland.Animals.Owl;
import woodland.Animals.Deer;
import woodland.Animals.Badger;
import woodland.Creatures.Creature;
import woodland.Creatures.ComplicatedCentaur;
import woodland.Creatures.DeceptiveDragon;
import woodland.Creatures.PrecociousPhoenix;
import woodland.Creatures.SassySphinx;
import woodland.Creatures.UnderAppreciatedUnicorn;
import woodland.Spells.Spell;

import java.util.*;
import java.util.Map;
import java.util.LinkedHashMap;

import static java.lang.Math.*;

/**
 * This class represents the main game engine for a board game involving animals, creatures, and spells.
 * It manages the game state, including the game board, players, and turns, and handles the game logic
 * for moving animals, casting spells, and determining win/loss conditions.
 */
public class Game {
	int ROW = 20;
	int COL = 20;
	Square[][] board = new Square[20][20];
	int turn = 0;
	public long seed;
	String status = "";
	String extendStatus = "";
	Map<String, Creature> creatures = new LinkedHashMap<>();
	Map<String, Animal> animals = new LinkedHashMap<>();
	Map<Integer, Spell> spells = new LinkedHashMap<>();

	/**
	 * Constructs a new Game with a specified seed for random number generation.
	 * This constructor initializes the game board and distributes players and spells on the board.
	 *
	 * @param seed The seed for the random number generator to ensure reproducible game setups.
	 */
	public Game(long seed) {
		this.seed = seed;
		for(int i = 0; i < this.ROW; i++) {
			for(int j = 0; j < this.COL; j++){
				 this.board[i][j] = new Square(i, j, false);
			}
		}
		generatePlayers();
		initializeBoard(board, this.seed);
	}

	/**
	 * Initializes the game board with animals, creatures, and spells placed at random positions.
	 * The method ensures that each square can only hold one entity at a time.
	 *
	 * @param board The game board to initialize.
	 * @param seed The seed for random placement of entities on the board.
	 */
	public void initializeBoard(Square[][] board, long seed){
		Random random = new Random(seed);
		int col;
		int row;
		boolean flag = false;
		for (Map.Entry<String, Animal> entry : animals.entrySet()){
			flag = false;
			do {
				col = random.nextInt(20);
				if (!board[19][col].hasAnimal()){
					board[19][col].setAnimal(entry.getValue());
					flag = true;
				}
			} while (!flag);
		}
		for (Map.Entry<String, Creature> entry : creatures.entrySet()){
			flag = false;
			 do {
				row = random.nextInt(18) + 1;
				col = random.nextInt(20);
				if (!board[row][col].hasAnimal() && !board[row][col].hasCreature()) {
					board[row][col].setCreature(entry.getValue());
					flag = true;
				}
			} while (!flag);
		}
		int pick;
		for (int i = 0; i < 10; i++) {
			flag = false;
			do {
				pick = random.nextInt(5);
				Spell spell = pick2Spell(pick);
				row = random.nextInt(18) + 1;
				col = random.nextInt(20);
				if (!board[row][col].hasAnimal() && !board[row][col].hasCreature() && !board[row][col].hasSpell()) {
					board[row][col].setSpell(spell);
					System.out.println(board[row][col].getSpell().spellName + "at row " + row + " col " + col);
					flag = true;
				}
			} while (!flag);
		}
	}

	public Square[][] getBoard() {
		return this.board;
	}

	public Spell pick2Spell(int pick) {
		Spell spell = null;
		int iter = 0;
		for (Map.Entry<Integer, Spell> entry : this.spells.entrySet()) {
			if (iter == pick) {
				spell = entry.getValue();
			}
			iter++;
		}
		return spell;
	}

	public Square getSquare(int row, int col) {
		return this.board[row][col];
	}

	/**
	 * Attempts to move an animal on the game board from one square to another.
	 * If the move is valid and successful, the game state is updated accordingly.
	 *
	 * @param animal The animal attempting to move.
	 * @param oldRow The row index of the animal's current position.
	 * @param oldCol The column index of the animal's current position.
	 * @param newRow The row index of the animal's desired new position.
	 * @param newCol The column index of the animal's desired new position.
	 * @param action A string representing the type of action being taken.
	 * @return The new action status after attempting the move.
	 */
	public String moveAnimal(Animal animal, int oldRow, int oldCol, int newRow, int newCol, String action) {
		boolean flag = false;
		boolean flagMoveJudge = false;
		int originTurn = this.turn;
		flagMoveJudge = moveJudge(animal);
		int originLifepoints = animal.lifePoints;
		flag = animal.move(oldRow, oldCol, newRow, newCol, this.board);
		if (!flag) {
			this.turn = originTurn;
		}
		if (flagMoveJudge && flag) {
			if (action.equals("Move")) {
				action = "Spell";
			}
		}

		if (flagMoveJudge && flag) {
			for (Map.Entry<String, Creature> entry : creatures.entrySet()) {
				if (entry.getValue().attackValid < 0) {
					entry.getValue().attackValid += 1;
				}
				if (entry.getValue().attackValid < 0) {
					entry.getValue().loseMagic();
				} else {
					entry.getValue().recoverMagic();
					entry.getValue().confused = false;
					entry.getValue().clearCharmedAnimals();
				};
			}
			int lifepointsJudge = abs(animal.lifePoints - originLifepoints);
			Square creatureSquare = getCreateSquare(this.creatures, lifepointsJudge);
			animal.getSquare().setAnimal(null);
			animal.getSquare().visible = false;
			if (animal.getSquare().hasCreature()) {
				animal.getSquare().visible = true;
			}
			if (creatureSquare != null) {
				newRow = creatureSquare.getRow();
				newCol = creatureSquare.getCol();
			}
			this.board[newRow][newCol].setAnimal(animal);
			checkPickSpell(animal);
			this.turn = (this.turn + 1) % 5;
			if (lifepointsJudge > 0) {
				this.status =  "The last move was interrupted by a creature.";
			} else {
				this.status = "The last move was successful.";
			}
		} else {
			this.status = "The last move was invalid.";
		}
		return action;
	}

	/**
	 * Casts a spell with the specified animal. The effect of the spell varies depending on its type.
	 *
	 * @param animal The animal that is casting the spell.
	 * @param spell  The spell being cast.
	 */
	public void spellAnimal(Animal animal, Spell spell) {
		String spellName = spell.spellName;
		int row = animal.getSquare().getRow();
		int col = animal.getSquare().getCol();
		int[] newRowCols = {min(row+1, 19), col,
				min(row+1, 19), min(col+1, 19),
				min(row+1, 19), max(col-1, 0),
				row, min(col+1, 19),
				row, max(col-1, 0),
				max(row-1, 0), col,
				max(row-1, 0), min(col+1, 19),
				max(row-1, 0), max(col-1, 0)};
		if (spellName.equals("Detect")) {
			for (int i = 0; i < newRowCols.length; i+=2) {
				if (this.board[newRowCols[i]][newRowCols[i+1]].hasCreature()) {
					System.out.println("creature was found nearby animal by using the Detect");
					this.board[newRowCols[i]][newRowCols[i+1]].reveal();
				}
			}
		} else if (spellName.equals("Heal")) {
			animal.lifePoints = min(100, animal.lifePoints + 10);
		} else if (spellName.equals("Shield")) {
			animal.lifePoints  = min(100, animal.lifePoints + animal.getSquare().getCreature().attackValue);
		} else if (spellName.equals("Confuse")) {
			for (int i = 0; i < newRowCols.length; i+=2) {
				if (this.board[i][i+1].hasCreature()) {
					this.board[i][i+1].getCreature().attackValid = -1;
					this.board[i][i+1].getCreature().confused = true;
				}
			}
		} else if (spellName.equals("Charm")) {
			for (int i = 0; i < newRowCols.length; i+=2) {
				if (this.board[i][i+1].hasCreature()) {
					this.board[i][i+1].getCreature().attackValid = -3;
					this.board[i][i+1].getCreature().addCharmedAnimals(animal);
				}
			}
		}
		this.status = "The last spell was successful.";
	}

	/**
	 * Simulates an attack by a creature on an animal, reducing the animal's life points by the creature's attack value.
	 *
	 * @param creature The creature that is attacking.
	 * @param animal   The animal that is being attacked.
	 */
	public void attackAnimal(Creature creature, Animal animal) {
		animal.lifePoints -= creature.attackValue;
	}

	/**
	 * Retrieves the animal whose turn it currently is, based on a turn index.
	 *
	 * @param turn The current turn index.
	 * @return The animal whose turn it is.
	 */
	public Animal getAnimalFromTurn (int turn) {
		turn = turn % 5;
		int iter = 0;
		for (Map.Entry<String, Animal> entry : this.animals.entrySet()) {
			if (iter == turn) { return entry.getValue(); }
			iter++;
		}
		return null;
	}

	/**
	 * Finds the square where a creature that matches a specific attack value is located.
	 *
	 * @param creatures A map of creatures to search through.
	 * @param value     The attack value to match against the creatures.
	 * @return The square where the creature with the matching attack value is found, or null if no match is found.
	 */
	public Square getCreateSquare(Map<String, Creature> creatures, int value) {
		Square square = null;
		for (Map.Entry<String, Creature> entry : creatures.entrySet()) {
			if (entry.getValue().attackValue  == value) {
				System.out.println("creature who attcked was find");
				square =  entry.getValue().getSquare();
			}
		}
		return square;
	}

	/**
	 * Clears the maps of creatures, animals, and spells, effectively resetting the game state.
	 */
	public void clearMap(){
		creatures.clear();
		animals.clear();
		spells.clear();
	}

	/**
	 * Judges whether the move action is valid based on the current turn and the animal attempting to move.
	 *
	 * @param animal The animal attempting to move.
	 * @return True if the move is valid for the current turn, false otherwise.
	 */
	public boolean moveJudge(Animal animal) {
		boolean flag = false;
		int iter = 0;
		for (Map.Entry<String, Animal> entry : this.animals.entrySet()) {
			if (iter == this.turn) {
				if (animal.name.contains(entry.getKey())) {
					this.turn = iter;
					flag = true;
					break;
				}
			}
			iter++;
		}
		return flag;
	}

	/**
	 * Generates the players for the game, including creatures, animals and spells and initializes their respective descriptions.
	 */
	public void generatePlayers(){
		UnderAppreciatedUnicorn UU = new UnderAppreciatedUnicorn("Under-appreciated Unicorn", "UU", 14,
				"The UU is a unicorn that is under-appreciated by the other " +
						"mythical creatures because it is often mistaken for a horse with a horn.");
		ComplicatedCentaur CC = new ComplicatedCentaur("Complicated Centaur","CC", 36,
				"The CC is a centaur that has mixed feeling about its love interest, a " +
						"horse. The centaur is unsure whether they can love them fully.");
		DeceptiveDragon DD = new DeceptiveDragon("Deceptive Dragon","DD", 29,
				"The DD is a dragon that practices social engineering. The dragon is " +
						"very good at sending phishing emails pretending to be a prince.");
		PrecociousPhoenix PP = new PrecociousPhoenix("Precocious Phoenix","PP", 42,
				"The PP is a phoenix that is very precocious. The phoenix understands " +
						"the meaning of life and the universe.");
		SassySphinx SS = new SassySphinx("Sassy Sphinx","SS", 21,
				"The SS is a sphinx that is very sassy. The sphinx is very good at giving " +
						"sarcastic answers to questions.");
		creatures.put("UnderAppreciatedUnicorn", UU);
		creatures.put("ComplicatedCentaur", CC);
		creatures.put("DeceptiveDragon", DD);
		creatures.put("PrecociousPhoenix", PP);
		creatures.put("SassySphinx", SS);

		Rabbit rabbit = new Rabbit("Rabbit");
		rabbit.description = "The rabbit has fluffy ears and tail. The rabbit really likes to eat grass.";
		Fox fox = new Fox("Fox");
		fox.description = "The fox has a bushy tail. The fox really enjoys looking at butterflies in the sunlight.";
		Deer deer = new Deer("Deer");
		deer.description = "The deer has antlers. The deer is recently divorced and is looking for a new partner.";
		Owl owl = new Owl("Owl");
		owl.description = "The owl has wings. The owl has prescription contact lenses but cannot put them on.";
		Badger badger = new Badger("Badger");
		badger.description = "The badger has a black and white face. The badger is a often mistaken for a very small " +
				"panda. The badger wears a t-shirt that says “I am not a panda” to combat this.";
		animals.put("Rabbit", rabbit);
		animals.put("Fox", fox);
		animals.put("Deer", deer);
		animals.put("Owl", owl);
		animals.put("Badger", badger);

		Spell Detect = new Spell("Detect");
		Detect.description = "The detect spell allows the animal to detect the mythical creatures on the adjacent squares.";
		Spell Heal = new Spell("Heal");
		Heal.description = " The heal spell allows the animal to heal 10 life points.";
		Spell Shield = new Spell("Shield");
		Shield.description = "The shield spell allows the animal to block a mythical creature attack for that turn.";
		Spell Confuse = new Spell("Confuse");
		Confuse.description = "The confuse spell allows the animal to confuse a mythical creature on a square adjacent " +
				"to the animal but not the square the animal is occupying. The mythical creature will not attack any " +
				"animal for the next turn.";
		Spell Charm = new Spell("Charm");
		Charm.description = "The charm spell allows the animal to charm a mythical creature on a square adjacent to " +
				"the animal but not the square the animal is occupying. The mythical creature will not attack the " +
				"charming animal for the next three turns.";
		spells.put(0, Detect);
		spells.put(10, Heal);
		spells.put(20, Shield);
		spells.put(30, Confuse);
		spells.put(40, Charm);
	}

	/**
	 * Checks if the animal has picked up a spell on its square and applies the spell effect if so.
	 *
	 * @param animal The animal to check for a picked-up spell.
	 */
	public void checkPickSpell(Animal animal) {
		if (animal.getSquare().hasSpell()) {
			animal.updateSpell(animal, animal.getSquare().getSpell(), true);
			animal.getSquare().spell = null;
		}
	}

	/**
	 * Determines if the game is over, which occurs if any animal has died.
	 *
	 * @return True if the game is over, false otherwise.
	 */
	public boolean gameOver() {
		for (Map.Entry<String, Animal> entry : this.animals.entrySet()) {
			if (!entry.getValue().isAlive()) {
				this.status = "You have lost the game.";
				this.extendStatus = "The " +entry.getValue().name+ " has died.";
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the game has been won, which occurs when all animals have reached row 0 on the board.
	 *
	 * @return True if the game has been won, false otherwise.
	 */
	public boolean gameWin() {
		for (Map.Entry<String, Animal> entry : this.animals.entrySet()) {
			if (!(entry.getValue().getSquare().getRow() == 0)) {
				return false;
			}
		}
		this.status = "You have won the game.";
		return true;
	}
}