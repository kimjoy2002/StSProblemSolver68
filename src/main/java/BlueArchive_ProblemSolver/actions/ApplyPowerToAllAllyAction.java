package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ApplyPowerToAllAllyAction extends AbstractGameAction {
    AbstractPower powerToApply;
    AbstractPlayer exclude;

    PowerToCharacterType type = PowerToCharacterType.NONE;

    public enum PowerToCharacterType {
        NONE,
        ONLY_MERCENARY
    }
    public ApplyPowerToAllAllyAction(AbstractPower powerToApply) {
        this(powerToApply, 0);
    }
    public ApplyPowerToAllAllyAction(AbstractPower powerToApply, int stackAmount) {
        this.amount = stackAmount;
        this.powerToApply = powerToApply;
        this.exclude = null;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public ApplyPowerToAllAllyAction(AbstractPower powerToApply, int stackAmount, AbstractPlayer exclude) {
        this.amount = stackAmount;
        this.powerToApply = powerToApply;
        this.exclude = exclude;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public ApplyPowerToAllAllyAction(AbstractPower powerToApply, int stackAmount, PowerToCharacterType type) {
        this.amount = stackAmount;
        this.powerToApply = powerToApply;
        this.type = type;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(exclude != p && p.currentHealth > 0) {
                    if(type == PowerToCharacterType.ONLY_MERCENARY &&
                        ProblemSolver68.isProblemSolver(p.solverType)) {
                        continue;
                    }

                    AbstractPower copy_ = powerToApply;
                    if(powerToApply instanceof CloneablePowerInterface) {
                        copy_ = ((CloneablePowerInterface)powerToApply).makeCopy();
                    }
                    copy_.owner = p;
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, copy_));
                }
            }
        } else {
            if(exclude != AbstractDungeon.player) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, powerToApply, amount));
            }
        }
        this.isDone = true;
    }
}
