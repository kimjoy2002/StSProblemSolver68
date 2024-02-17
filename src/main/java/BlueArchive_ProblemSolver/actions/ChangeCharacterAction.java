package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ChangeCharacterAction extends AbstractGameAction {
    AbstractPlayer targetPlayer;
    public ChangeCharacterAction(AbstractPlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        if(targetPlayer == AbstractDungeon.player) {
            return;
        }
        AbstractDungeon.player = targetPlayer;
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
    }
}
