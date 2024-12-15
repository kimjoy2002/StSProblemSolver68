package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.cards.UseCardActionPatch;
import BlueArchive_ProblemSolver.powers.FearPower;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;

import java.util.Iterator;

public class CheckFearAction extends AbstractGameAction {
    AbstractCreature owner;
    public CheckFearAction(AbstractCreature owner) {
        this.setValues(owner, owner, amount);
        this.owner = owner;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void loseSurround() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster)var1.next();
                if (!m.isDead && !m.isDying) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        if(ps != AbstractDungeon.player) {
                            if (ps.hasPower("Surrounded")) {
                                ps.flipHorizontal = m.drawX < ps.drawX;
                                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(ps, ps, "Surrounded"));
                            }
                        }
                    }
                }
            }
        }
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if (!m.isDead && !m.isDying) {
                if (AbstractDungeon.player.hasPower("Surrounded")) {
                    AbstractDungeon.player.flipHorizontal = m.drawX < AbstractDungeon.player.drawX;
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Surrounded"));
                }

                if (m.hasPower("BackAttack")) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, m, "BackAttack"));
                }
            }
        }

    }



    public boolean areMonstersBasicallyDead(AbstractCreature exclude) {
        if(AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().monsters == null)
            return false;

        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        AbstractMonster m;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            m = (AbstractMonster)var1.next();
        } while(m == exclude || m.currentHealth <= 0 || m.isDying || m.isEscaping || (m.hasPower(FearPower.POWER_ID) &&
                owner.getPower(FearPower.POWER_ID).amount >= m.currentHealth));

        return false;
    }

    public void update() {
        if(!owner.hasPower(FearPower.POWER_ID)) {
            this.isDone = true;
            return;
        }
        amount = owner.getPower(FearPower.POWER_ID).amount;

        if(!owner.isEscaping && !owner.isDying && owner instanceof AbstractMonster &&
                amount >= owner.currentHealth) {
            ((FearPower)owner.getPower(FearPower.POWER_ID)).already_feared = true;
            if(owner instanceof SpireShield || owner instanceof SpireSpear) {
                loseSurround();
            }
            if(owner instanceof AwakenedOne) {
                boolean form1 = ReflectionHacks.getPrivate(owner, AwakenedOne.class, "form1");
                if(form1) {
                    AbstractDungeon.actionManager.addToBottom(new InstantKillAction(owner));
                    this.isDone = true;
                    return;
                }
            } else if(owner.id.equals("BlueArchive_Hoshino:GozBunsin") ||
                    (owner.id.equals("BlueArchive_Hoshino:Goz") && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().cannotLose ) ||
                    owner.id.equals("BlueArchive_Hoshino:PeroroHifumi")||
                    owner.id.equals("BlueArchive_Hoshino:Siro")||
                    owner.id.startsWith("BlueArchive_Hoshino:Kaitenger") ||
                    owner.id.equals("BlueArchive_Hoshino:Hieronymus")) {
                AbstractDungeon.actionManager.addToBottom(new InstantKillAction(owner));
                this.isDone = true;
                return;
            } else if(areMonstersBasicallyDead(owner) && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().cannotLose) {
                //특수 처리(보통 여기로 타면 좀 문제가 있음)
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
            AbstractDungeon.actionManager.addToBottom(new FearEscapeAction((AbstractMonster)owner));
        }
        this.isDone = true;
    }
}
