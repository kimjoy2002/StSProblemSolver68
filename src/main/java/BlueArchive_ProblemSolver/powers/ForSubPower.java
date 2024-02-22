package BlueArchive_ProblemSolver.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;

public interface ForSubPower {
    public default float atDamageGiveForSub(float damage, DamageInfo.DamageType type){return damage;};

    public default void onUseCardForSub(AbstractCard card, UseCardAction action){return;};
}
