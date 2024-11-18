package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import BlueArchive_ProblemSolver.powers.FixedImpPower;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import javax.swing.*;
import java.util.ArrayList;

public class FinishAction extends AbstractGameAction {
    AbstractCard powerToCard;

    ArrayList<AbstractGameAction> actions;
    String text;
    boolean isDebuf;
    public FinishAction(AbstractCard powerToCard, ArrayList<AbstractGameAction> actions, String text,boolean isDebuf) {
        this.powerToCard = powerToCard;
        this.actions = actions;
        this.text = text;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.isDebuf = isDebuf;
    }
    @Override
    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            boolean applyed_ = false;
            for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(p.currentHealth > 0 && p.hasPower(FinishPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, FinishPower.POWER_ID));
                    applyed_ = true;
                    break;

                }
            }
        }
        else {
            if(AbstractDungeon.player.hasPower(FinishPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, FinishPower.POWER_ID));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FinishPower(AbstractDungeon.player, actions, text, isDebuf)));
        this.isDone = true;
    }
}
