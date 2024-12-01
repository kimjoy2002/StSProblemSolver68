package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.CorrectAdvice;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CheckApplyPowerAction extends AbstractGameAction {
    AbstractPower power;
    public CheckApplyPowerAction(AbstractPlayer target, AbstractPlayer source, AbstractPower power) {
        this.setValues(target, source, 0);
        this.power = power;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        this.isDone = true;
        if(target instanceof ProblemSolver68) {
            if(target.currentHealth > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, power));
            } else {
                AbstractPlayer front = ProblemSolver68.getFrontMember();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(front, front, power));
            }
        } else {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, power));
        }
    }
}
