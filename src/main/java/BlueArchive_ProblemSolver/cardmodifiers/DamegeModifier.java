package BlueArchive_ProblemSolver.cardmodifiers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cards.PokerFace;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DamegeModifier extends AbstractCardModifier {
    public static final String MODIFIER_ID = DefaultMod.makeID("DamegeModifier");
    public int damage;

    public String identifier(AbstractCard card) {
        return MODIFIER_ID;
    }
    public DamegeModifier(int damage) {
        this.damage = damage;
    }


    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage + (float)this.damage;
    }

    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DamegeModifier(damage);
    }
}
