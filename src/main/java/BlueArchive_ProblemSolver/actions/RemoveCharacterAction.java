package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class RemoveCharacterAction extends AbstractGameAction {

    public RemoveCharacterAction(ProblemSolver68 target) {
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        if(target == null)
            return;
        ProblemSolver68.removeCharacter((ProblemSolver68)target);
    }
}
