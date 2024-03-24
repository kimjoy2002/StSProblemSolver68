package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddChellengeCountAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TacticalChallengeKayoko extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(TacticalChallengeKayoko.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("TacticalChallengeKayoko.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int MAGIC = 2;
    private static final int MAGIC2 = 4;
    private static final int UPGRADE_PLUS_MAGIC2 = -1;



    public TacticalChallengeKayoko() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);

        this.tags.add(EnumPatch.TACTICAL_CHALLENGE);
    }

    public void updateVal () {
        this.magicNumber = this.baseMagicNumber = MAGIC;
        this.magicNumber += GameActionManagerPatch.tacticalChallengeCount /secondMagicNumber;
        if (baseMagicNumber != magicNumber) {
            this.isMagicNumberModified = true;
        }
    }
    public void applyPowers() {
        updateVal();
        super.applyPowers();
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        updateVal();
        super.applyPowers();
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateVal();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(magicNumber));
        AbstractDungeon.actionManager.addToBottom(new AddChellengeCountAction());
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_MAGIC2);
            initializeDescription();
        }
    }
}
