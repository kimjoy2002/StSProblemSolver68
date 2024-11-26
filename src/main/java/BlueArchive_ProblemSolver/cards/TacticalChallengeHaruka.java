package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddChellengeCountAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TacticalChallengeHaruka extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(TacticalChallengeHaruka.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("TacticalChallengeHaruka.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;


    public TacticalChallengeHaruka() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);

        this.tags.add(EnumPatch.TACTICAL_CHALLENGE);
    }


    public void applyPowersToBlock() {
        super.applyPowersToBlock();
        this.block += magicNumber * GameActionManagerPatch.tacticalChallengeCount;
        if(block != baseBlock)
            isBlockModified = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.calculateCardDamage(m);
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        AbstractDungeon.actionManager.addToBottom(new AddChellengeCountAction());
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
