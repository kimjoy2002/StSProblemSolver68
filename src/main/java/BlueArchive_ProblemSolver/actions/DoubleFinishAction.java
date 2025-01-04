package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.DoubleYourBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.AbstractList;
import java.util.ArrayList;

public class DoubleFinishAction extends AbstractGameAction {
    public DoubleFinishAction() {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
    }



    @Override
    public void update() {
        AbstractList<FinishPower> finishList = new ArrayList<>();
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    for(AbstractPower p : ps.powers) {
                        if(p.ID.startsWith(FinishPower.POWER_ID)) {
                            finishList.add((FinishPower)p);
                        }
                    }
                }
            }
        }
        else {
            for(AbstractPower p : AbstractDungeon.player.powers) {
                if(p.ID.startsWith(FinishPower.POWER_ID)) {
                    finishList.add((FinishPower)p);
                }
            }
        }

        for(FinishPower fp : finishList) {
            fp.amount += fp.amount;
            ArrayList<AbstractGameAction> addActions = new ArrayList<AbstractGameAction>();
            for(AbstractGameAction action : fp.actions) {
                if(action instanceof DamageAllEnemiesAction) {
                    for(int i = 0; i < ((DamageAllEnemiesAction)action).damage.length; i++) {
                        ((DamageAllEnemiesAction)action).damage[i] += ((DamageAllEnemiesAction)action).damage[i];
                    }
                }
                else if(action instanceof ApplyPowerAction) {
                    action.amount += action.amount;
                    ApplyPowerAction app = ((ApplyPowerAction)action);
                    AbstractPower power = (AbstractPower) ReflectionHacks.getPrivate(app, ApplyPowerAction.class, "powerToApply");
                    power.amount += power.amount;
                }
                else if(action instanceof DoubleYourBlockAction) {
                    addActions.add(new DoubleYourBlockAction(action.target));
                }
                else if(action instanceof GainEnergyAction) {
                    GainEnergyAction gea = ((GainEnergyAction)action);
                    int energyGain = (int) ReflectionHacks.getPrivate(gea, GainEnergyAction.class, "energyGain");
                    ReflectionHacks.setPrivate(gea, GainEnergyAction.class, "energyGain", energyGain+energyGain);
                }
                else {
                    action.amount += action.amount;
                }
            }
            fp.updateDescription();
            if(!addActions.isEmpty())
                fp.actions.addAll(addActions);
        }

        this.isDone = true;
    }
}
