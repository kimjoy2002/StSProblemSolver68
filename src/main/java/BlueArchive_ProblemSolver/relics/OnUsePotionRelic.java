package BlueArchive_ProblemSolver.relics;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public interface OnUsePotionRelic {
    public void OnUsePotion(AbstractPotion potion, AbstractCreature target);
}