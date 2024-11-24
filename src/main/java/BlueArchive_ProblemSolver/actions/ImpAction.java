package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import BlueArchive_ProblemSolver.powers.MineChainPower;
import BlueArchive_ProblemSolver.powers.SharedPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class ImpAction extends AbstractGameAction {
    int imp_amount = -1;
    public ImpAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    public ImpAction(int amount, int imp_amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.imp_amount = imp_amount;
    }

    public static void resetImp() {
        int amount = 0;
        int value = 0;
        ArrayList<AbstractPower> newPowers = new ArrayList<AbstractPower>();

        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for(AbstractPlayer ps :  ProblemSolver68.problemSolverPlayer ) {

                if(ps != AbstractDungeon.player) {
                    for(AbstractPower power_ : ps.powers) {
                        if(power_ instanceof CloneablePowerInterface && power_ instanceof SharedPower){
                            newPowers.add(((CloneablePowerInterface)power_).makeCopy());
                            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(ps, ps, power_));
                        }
                    }
                }

                if(ps != AbstractDungeon.player &&  ps.hasPower(ImpPower.POWER_ID)) {
                    amount += ps.getPower(ImpPower.POWER_ID).amount;
                    value += ((ImpPower)ps.getPower(ImpPower.POWER_ID)).amount_imp;
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(ps, ps, ImpPower.POWER_ID));
                }
            }
        }
        if(amount>0) {
            ImpPower power = new ImpPower(AbstractDungeon.player, amount);
            power.amount_imp = value;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power, amount));
        }
        for(AbstractPower power_ : newPowers) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, power_));
        }
    }



    public void update() {
        this.isDone = true;
        if(amount <= 0) {
            return;
        }
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            AbstractPlayer p = ProblemSolver68.getFrontMember();
            if(p != null) {
                ImpPower imp = new ImpPower(p, amount);
                if(imp_amount != -1)
                    imp.amount_imp = imp_amount;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, imp, amount));
            }
        } else {
            ImpPower imp = new ImpPower(AbstractDungeon.player, amount);
            if(imp_amount != -1)
                imp.amount_imp = imp_amount;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, imp, amount));
        }
    }
}
