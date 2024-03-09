package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ApplyPowerToSpecificAction extends AbstractGameAction {
    AbstractPower powerToApply;
    Aru.ProblemSolver68Type solverType;
    public ApplyPowerToSpecificAction(AbstractPower powerToApply, int stackAmount, Aru.ProblemSolver68Type solverType) {
        this.amount = stackAmount;
        this.powerToApply = powerToApply;
        this.solverType = solverType;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
    }
    @Override
    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(p.solverType == solverType && p.currentHealth > 0) {
                    AbstractPower copy_ = powerToApply;
                    if(powerToApply instanceof CloneablePowerInterface) {
                        copy_ = ((CloneablePowerInterface)powerToApply).makeCopy();
                    }
                    copy_.owner = p;
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, copy_));
                }
            }
        }
        this.isDone = true;
    }
}
