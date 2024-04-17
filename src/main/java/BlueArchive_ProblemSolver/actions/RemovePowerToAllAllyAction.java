package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RemovePowerToAllAllyAction extends AbstractGameAction {
    String powerToRemove;

    AbstractPlayer exclude;

    PowerToCharacterType type = PowerToCharacterType.NONE;

    public enum PowerToCharacterType {
        NONE,
        ONLY_MERCENARY
    }
    public RemovePowerToAllAllyAction(String powerToRemove) {
        this.powerToRemove = powerToRemove;
        this.exclude = null;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public RemovePowerToAllAllyAction(String powerToRemove, AbstractPlayer exclude) {
        this.powerToRemove = powerToRemove;
        this.exclude = exclude;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public RemovePowerToAllAllyAction(String powerToRemove,PowerToCharacterType type) {
        this.powerToRemove = powerToRemove;
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

                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p, p, powerToRemove));
                }
            }
        } else {
            if(exclude != AbstractDungeon.player) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, powerToRemove));
            }
        }
        this.isDone = true;
    }
}
