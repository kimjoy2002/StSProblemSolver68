package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ProperGreetingAction extends AbstractGameAction {
    public ProperGreetingAction() {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    for(AbstractPower p : ps.powers) {
                        if(p.ID.startsWith(FinishPower.POWER_ID)) {
                            FinishPower power = (FinishPower) p;
                            power.atEndOfTurn(true);
                            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(ps, ps, power));
                        }
                    }
                }
            }
        } else {
            if(AbstractDungeon.player.currentHealth > 0) {
                for(AbstractPower p : AbstractDungeon.player.powers) {
                    if(p.ID.startsWith(FinishPower.POWER_ID)) {
                        FinishPower power = (FinishPower) p;
                        power.atEndOfTurn(true);
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, power));
                    }
                }
            }
        }
        this.isDone = true;
    }
}
