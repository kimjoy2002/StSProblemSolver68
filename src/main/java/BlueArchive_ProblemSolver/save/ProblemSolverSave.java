package BlueArchive_ProblemSolver.save;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.abstracts.CustomSavable;

import java.util.ArrayList;

public class ProblemSolverSave implements CustomSavable<ArrayList<SaveData>> {
    public static ArrayList<SaveData> currentCharacters = new ArrayList<SaveData>();

    @Override
    public ArrayList<SaveData> onSave() {
        return currentCharacters;
    }
    @Override
    public void onLoad(ArrayList<SaveData> characters) {
        currentCharacters.clear();
        for(SaveData c : characters) {
            ProblemSolver68.addCharacter(ProblemSolver68.stringToEnum(c.name), c.hp, c.max_hp, false);
        }
    }
    public void addCharacter(String character, int hp, int max_hp){
        currentCharacters.add(new SaveData(character, hp, max_hp));
    }
}