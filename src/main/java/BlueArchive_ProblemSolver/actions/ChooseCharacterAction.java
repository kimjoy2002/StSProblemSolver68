package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.ChooseAru;
import BlueArchive_ProblemSolver.cards.ChooseHaruka;
import BlueArchive_ProblemSolver.cards.ChooseKayoko;
import BlueArchive_ProblemSolver.cards.ChooseMutsuki;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ChooseCharacterAction extends AbstractGameAction {
    private ArrayList<AbstractPlayer> exclude;

    public ChooseCharacterAction() {
        this(null);
    }
    public ChooseCharacterAction(ArrayList<AbstractPlayer> exclude) {
        if(exclude == null) {
            exclude = new ArrayList<AbstractPlayer>();
        }
        this.duration = Settings.ACTION_DUR_FAST;
    }


    private void chooseOption() {
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> optionChoices = new ArrayList();
        Map<Aru.ProblemSolver68Type, AbstractCard> ableChoices = new HashMap<>();
        ableChoices.put(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU, new ChooseAru());
        ableChoices.put(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI, new ChooseMutsuki());
        ableChoices.put(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO, new ChooseKayoko());
        ableChoices.put(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA, new ChooseHaruka());

        for (Map.Entry<Aru.ProblemSolver68Type, AbstractCard> choice : ableChoices.entrySet()) {
            boolean exist = false;
            for (AbstractPlayer player : exclude) {
                if(player instanceof Aru) {
                    Aru aru = (Aru) player;
                    if(aru.solverType == choice.getKey()) {
                        exist = true;
                    }
                }
            }
            if(!exist) {
                optionChoices.add(choice.getValue());
            }
        }
        this.addToBot(new ChooseOneAction(optionChoices));
    }
    public void update() {
        chooseOption();
        this.isDone = true;
    }
}
