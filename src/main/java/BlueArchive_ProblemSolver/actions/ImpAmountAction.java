package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

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
        ArrayList<ImpPower> powers = new ArrayList<>();

        if(power == null) {
            powers.addAll(ProblemSolver68.getPowers(ImpPower.POWER_ID, ImpPower.class));
        } else {
            powers.add(power);
        }

        for(ImpPower imppower : powers) {
            if(add) {
                imppower.amount_imp += amount;
            }
            else if(amount >= 999) {
                imppower.amount_imp = imppower.amount;
            }
            else if(amount != -1) {
                imppower.amount_imp = amount;
            }
            if(imppower.amount_imp > imppower.amount) {
                imppower.amount_imp = imppower.amount;
            }
            imppower.updateDescription();
        }
        AbstractDungeon.player.hand.applyPowers();
        this.isDone = true;
    }
}
