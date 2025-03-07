package BlueArchive_ProblemSolver.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface ForSubPower {
    public default float atDamageGiveForSub(float damage, AbstractCard card, DamageInfo.DamageType type){return damage;};

    public default void onUseCardForSub(AbstractCard card, UseCardAction action){return;};
    public default void onApplyPowerSub(AbstractPower power, AbstractCreature target, AbstractCreature source){return;};
}
