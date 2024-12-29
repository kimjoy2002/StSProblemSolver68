package BlueArchive_ProblemSolver.potions;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import basemod.abstracts.CustomPotion;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static BlueArchive_ProblemSolver.DefaultMod.DEFAULT_BLACK_RED;

public class TagPotion extends CustomPotion  {
    public static final String POTION_ID = DefaultMod.makeID("TagPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public TagPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.M, PotionColor.SMOKE);
        this.labOutlineColor = DEFAULT_BLACK_RED;

        potency = getPotency();

        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

        isThrown = false;
    }


    @Override
    public void use(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player,AbstractDungeon.player, potency));
        this.addToBot(new ChangeCharacterAction(AbstractDungeon.player, false, true, false));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TagPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 12;
    }

    public void initializeData() {
        this.potency = this.getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        this.tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
