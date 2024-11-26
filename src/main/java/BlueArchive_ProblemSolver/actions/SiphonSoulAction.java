package BlueArchive_ProblemSolver.actions;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class SiphonSoulAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractPlayer owner;
    private int heal_amount;
    private int str_amount;

    public SiphonSoulAction(AbstractCreature target, AbstractPlayer owner, DamageInfo info, int heal_amount, int str_amount) {
        this.info = info;
        this.setValues(target, info);
        this.owner = owner;
        this.heal_amount = heal_amount;
        this.str_amount = str_amount;
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0)) {
                if(!owner.isDying && owner.currentHealth > 0 && !owner.halfDead) {
                    if(heal_amount > 0) {
                        TempHPField.tempHp.set(owner, (Integer)TempHPField.tempHp.get(owner) + heal_amount);
                        AbstractDungeon.effectsQueue.add(new HealEffect(owner.hb.cX - owner.animX, owner.hb.cY,heal_amount));
                    }
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(AbstractDungeon.player, str_amount)));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
