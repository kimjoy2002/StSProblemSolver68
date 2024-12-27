package BlueArchive_ProblemSolver.save;

import BlueArchive_ProblemSolver.cards.ChooseAru;
import BlueArchive_ProblemSolver.cards.ChooseHaruka;
import BlueArchive_ProblemSolver.cards.ChooseKayoko;
import BlueArchive_ProblemSolver.cards.ChooseMutsuki;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.effects.SaveEffect;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

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
        if(ProblemSolver68.problemSolverPlayer.size() < 2) {
            ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
            if(!ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU))
                cards.add(new ChooseAru());
            if(!ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI))
                cards.add(new ChooseMutsuki());
            if(!ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO))
                cards.add(new ChooseKayoko());
            if(!ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA))
                cards.add(new ChooseHaruka());
            Collections.shuffle(cards, new java.util.Random());

            if(ProblemSolver68.problemSolverPlayer.size() == 0 && cards.size() > 1) {
                if((cards.get(0) instanceof ChooseMutsuki && cards.get(1) instanceof ChooseAru)  ||
                        (cards.get(0) instanceof ChooseKayoko && (cards.get(1) instanceof ChooseAru || cards.get(1) instanceof ChooseMutsuki)) ||
                        (cards.get(0) instanceof ChooseHaruka)) {
                    cards.get(1).onChoseThisOption();
                    cards.get(0).onChoseThisOption();
                } else {
                    cards.get(0).onChoseThisOption();
                    cards.get(1).onChoseThisOption();
                }
            }
            else if (ProblemSolver68.problemSolverPlayer.size() == 1 && cards.size() > 0) {
                cards.get(0).onChoseThisOption();
            } else {
                new ChooseAru().onChoseThisOption();
            }
            AbstractDungeon.topLevelEffectsQueue.add(new SaveEffect());
        }


    }
    public void addCharacter(String character, int hp, int max_hp){
        currentCharacters.add(new SaveData(character, hp, max_hp));
    }
}