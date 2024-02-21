package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GainBlockToAllAllyAction extends AbstractGameAction {
    public GainBlockToAllAllyAction(int blockAmount) {
        this.amount = blockAmount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        this.isDone = true;
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(p.currentHealth > 0) {
                    AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, amount));
                }
            }
        } else {
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount));
        }
    }
}
