package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class KeepEnergyAction extends AbstractGameAction {
    public KeepEnergyAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        GameActionManagerPatch.keepEnergy += amount;
        if(GameActionManagerPatch.keepEnergy < 0)
            GameActionManagerPatch.keepEnergy = 0;
        this.isDone = true;
    }
}
