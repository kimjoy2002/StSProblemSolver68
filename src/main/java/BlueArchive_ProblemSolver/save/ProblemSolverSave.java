package BlueArchive_ProblemSolver.save;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ProblemSolverSave implements CustomSavable<ArrayList<SaveData>> {
    public static ArrayList<SaveData> currentCharacters = new ArrayList<SaveData>();

    @Override
    public ArrayList<SaveData> onSave() {
        //update
        if (AbstractDungeon.player instanceof ProblemSolver68) {
            ArrayList<SaveData> temp = new ArrayList<SaveData>();
            if(ProblemSolver68.problemSolverPlayer.size() == currentCharacters.size()) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    for(SaveData saveData: currentCharacters) {
                        if(ProblemSolver68.stringToEnum(saveData.name) == ps.solverType) {
                            temp.add(saveData);
                        }
                    }
                }
                if(temp.size() == currentCharacters.size())  {
                    currentCharacters = temp;
                }
            }


            for (SaveData data : currentCharacters) {
                ProblemSolver68 char_ = ProblemSolver68.getCharacter(data.name);
                if(char_ != null) {
                    data.hp = char_.currentHealth;
                    data.max_hp = char_.maxHealth;
                }
            }
        }

        return currentCharacters;
    }
    @Override
    public void onLoad(ArrayList<SaveData> characters) {
        currentCharacters.clear();
        if(characters != null) {
            for(SaveData c : characters) {
                AbstractPlayer ps =  ProblemSolver68.addCharacter(ProblemSolver68.stringToEnum(c.name), c.hp, c.max_hp, false);
            }
        }
    }
    public void addCharacter(String character, int hp, int max_hp){
        currentCharacters.add(new SaveData(character, hp, max_hp));
    }
}