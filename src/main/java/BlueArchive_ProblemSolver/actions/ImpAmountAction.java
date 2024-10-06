package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ImpAmountAction extends AbstractGameAction {
    ImpPower power;
    boolean add;
    public ImpAmountAction(int amount, ImpPower power, boolean add) {
        this.setValues(this.target, this.source, amount);
        this.power = power;
        this.add = add;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(add) {
            power.amount_imp += amount;
        }
        else if(amount == -1) {
            if(power.amount_imp > power.amount) {
                power.amount_imp = power.amount;
            }
        } else {
            power.amount_imp = amount;
        }
        power.updateDescription();
        AbstractDungeon.player.hand.applyPowers();
        this.isDone = true;
    }
}
