package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.DiscardLeftAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.actions.common.DrawCardAction.drawnCards;

public class RouletteMisdirection extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(RouletteMisdirection.class.getSimpleName());
    public static final String IMG = makeCardPath("RouletteMisdirection.png");


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public static final int MAGIC = 2;

    public static final int MAGIC2 = 1;

    public RouletteMisdirection() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        this.addToBot(new DrawCardAction(magicNumber));
        this.addToBot(new DiscardLeftAction(secondMagicNumber));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
