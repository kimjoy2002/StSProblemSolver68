package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.effects.HealOnlyEffect;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class Debt extends EvilDeedsCard {
    public static final String ID = DefaultMod.makeID(Debt.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Debt.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;

    public Debt() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        setRequireEvil(3);
        exhaust = true;
        selfRetain = true;
    }

    @Override
    public CardStrings getCardStrings() {
        return cardStrings;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
    }

    @Override
    public void onEvilDeeds(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(1));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            updateRequireEvil(-1);
            makeDescrption();
        }
    }
}
