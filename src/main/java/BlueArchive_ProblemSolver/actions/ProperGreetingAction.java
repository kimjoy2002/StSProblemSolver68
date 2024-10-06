package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ProperGreetingAction extends AbstractGameAction {
    public ProperGreetingAction() {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(p.currentHealth > 0 && p.hasPower(FinishPower.POWER_ID)) {
                    FinishPower power = (FinishPower) p.getPower(FinishPower.POWER_ID);
                    power.atEndOfTurn(true);
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, FinishPower.POWER_ID));
                }
            }
        } else {
            if(AbstractDungeon.player.currentHealth > 0 && AbstractDungeon.player.hasPower(FinishPower.POWER_ID)) {
                FinishPower power = (FinishPower) AbstractDungeon.player.getPower(FinishPower.POWER_ID);
                power.atEndOfTurn(true);
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, FinishPower.POWER_ID));
            }
        }
        this.isDone = true;
    }
}
