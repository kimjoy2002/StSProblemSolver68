package BlueArchive_ProblemSolver.save;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.abstracts.CustomSavable;

import java.util.ArrayList;

public class ProblemSolverSave implements CustomSavable<ArrayList<saveData>> {
    public static ArrayList<saveData> currentCharacters = new ArrayList<saveData>();

    @Override
    public ArrayList<saveData> onSave() {
        return currentCharacters;
    }
    @Override
    public void onLoad(ArrayList<saveData> characters) {
        currentCharacters.clear();
        for(saveData c : characters) {
            ProblemSolver68.addCharacter(ProblemSolver68.stringToEnum(c.name), c.hp, c.max_hp);
        }
    }
    public void addCharacter(String character, int hp, int max_hp){
        currentCharacters.add(new saveData(character, hp, max_hp));
    }
}