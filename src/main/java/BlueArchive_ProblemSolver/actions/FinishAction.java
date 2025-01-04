package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.PreparedPerfectionPower;
import BlueArchive_ProblemSolver.powers.FinishPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

public class FinishAction extends AbstractGameAction {
    AbstractCard powerToCard;

    ArrayList<AbstractGameAction> actions;
    FinishPower.FinishString text;
    boolean isDebuf;
    public FinishAction(AbstractCard powerToCard, ArrayList<AbstractGameAction> actions, FinishPower.FinishString text, boolean isDebuf) {
        this.powerToCard = powerToCard;
        this.actions = actions;
        this.text = text;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.isDebuf = isDebuf;
    }


    public static int getFinishPowerNum(){
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            int num = 0;
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                num += getFinishPowerNum(ps);
            }
            return num;
        } else {
            return getFinishPowerNum(AbstractDungeon.player);
        }
    }
    public static int getFinishPowerNum(AbstractPlayer player){
        if(player.currentHealth <= 0)
            return 0;
        int num = 0;
        Iterator var2 = player.powers.iterator();

        for(AbstractPower p : player.powers) {
            if(p.ID.startsWith(FinishPower.POWER_ID)) {
                num++;
            }
        }
        return num;
    }

    @Override
    public void update() {
        int assultAmount = 1 + ProblemSolver68.getPowerValue(PreparedPerfectionPower.POWER_ID);
        //int getMaxFinish = getFinishPowerNum();
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            AbstractList<AbstractPower> finishList = new ArrayList<>();

            for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    for(AbstractPower p : ps.powers) {
                        if(p.ID.startsWith(FinishPower.POWER_ID)) {
                            finishList.add(p);
                        }
                    }
                }
            }

            finishList.sort((a, b) -> {
                int numberA = Integer.parseInt(a.ID.replace(FinishPower.POWER_ID, ""));
                int numberB = Integer.parseInt(b.ID.replace(FinishPower.POWER_ID, ""));
                return Integer.compare(numberA, numberB);
            });


            while (finishList.size() > assultAmount-1) {
                AbstractPower p_ = finishList.get(0);
                finishList.remove(0);
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p_.owner, p_.owner, p_.ID));
            }
        }
        else {
            AbstractList<AbstractPower> finishList = new ArrayList<>();

            for(AbstractPower p : AbstractDungeon.player.powers) {
                if(p.ID.startsWith(FinishPower.POWER_ID)) {
                    finishList.add(p);
                }
            }

            finishList.sort((a, b) -> {
                int numberA = Integer.parseInt(a.ID.replace(FinishPower.POWER_ID, ""));
                int numberB = Integer.parseInt(b.ID.replace(FinishPower.POWER_ID, ""));
                return Integer.compare(numberA, numberB);
            });

            while (finishList.size() > assultAmount-1) {
                AbstractPower p_ = finishList.get(0);
                finishList.remove(0);
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p_.ID));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FinishPower(AbstractDungeon.player, actions, text, isDebuf)));
        this.isDone = true;
    }
}
