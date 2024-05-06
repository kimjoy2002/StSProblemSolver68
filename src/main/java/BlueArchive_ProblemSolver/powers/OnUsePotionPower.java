package BlueArchive_ProblemSolver.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnUsePotionPower {
    public void OnUsePotion(AbstractPotion potion, AbstractCreature target);
}