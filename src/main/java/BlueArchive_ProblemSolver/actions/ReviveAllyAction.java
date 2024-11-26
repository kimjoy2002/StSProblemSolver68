package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import java.util.ArrayList;
import java.util.Collections;

public class ReviveAllyAction extends AbstractGameAction {
    public ReviveAllyAction(int tempHealAmount) {
        this.amount = tempHealAmount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        this.isDone = true;
        if(!(AbstractDungeon.player instanceof ProblemSolver68)){
            TempHPField.tempHp.set(AbstractDungeon.player, (Integer)TempHPField.tempHp.get(AbstractDungeon.player) + this.amount);
            AbstractDungeon.effectsQueue.add(new HealEffect(AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX, AbstractDungeon.player.hb.cY,1 + this.amount));
            return;
        }

        if(ProblemSolver68.getDeadMember() == 0 || AbstractDungeon.player.hasRelic("Mark of the Bloom") ||
                ProblemSolver68.getMemberNum(false, true) >= ProblemSolver68.MAX_CHARACTER_NUM ) {
            TempHPField.tempHp.set(AbstractDungeon.player, (Integer)TempHPField.tempHp.get(AbstractDungeon.player) + this.amount);
            AbstractDungeon.effectsQueue.add(new HealEffect(AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX, AbstractDungeon.player.hb.cY,1 + this.amount));
            return;
        }
        AbstractPlayer p = ProblemSolver68.getRandomDeadMember();
        if(p != null) {
            p.powers.clear();
            p.heal(1);
            p.isDead = false;
            TempHPField.tempHp.set(p, (Integer)TempHPField.tempHp.get(p) + this.amount);
            AbstractDungeon.effectsQueue.add(new HealEffect(p.hb.cX - p.animX, p.hb.cY,1 + this.amount));
            p.healthBarUpdatedEvent();
            this.addToTop(new ChangeCharacterAction(p, false, true, true));
        } else if(ProblemSolver68.mayRevivePlayer.size() > 0) {
            ProblemSolver68 ps = ProblemSolver68.getRandomReviveMember();
            if(ps != null) {
                ProblemSolver68.reviveDeathCharacter(ps);
                TempHPField.tempHp.set(ps, (Integer)TempHPField.tempHp.get(ps) + this.amount);
                AbstractDungeon.effectsQueue.add(new HealEffect(ps.hb.cX - ps.animX, ps.hb.cY,1 + this.amount));
                this.addToTop(new ChangeCharacterAction(ps, false, true, true));
            }
        } else {
            TempHPField.tempHp.set(AbstractDungeon.player, (Integer)TempHPField.tempHp.get(AbstractDungeon.player) + this.amount);
            AbstractDungeon.effectsQueue.add(new HealEffect(AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX, AbstractDungeon.player.hb.cY,1 + this.amount));
        }
    }
}
