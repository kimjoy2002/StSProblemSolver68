package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ApplyPowerToSpecificAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ForAruSamaPower;
import BlueArchive_ProblemSolver.powers.OutlawsRockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class ForAruSama extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ForAruSama.class.getSimpleName());
    public static final String IMG = makeCardPath("ForAruSama.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);



    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;
    private static final int MAGIC2 = 1;


    public ForAruSama() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new FrailPower(AbstractDungeon.player, secondMagicNumber, false), secondMagicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new DexterityPower(AbstractDungeon.player, magicNumber), magicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new WeakPower(AbstractDungeon.player, secondMagicNumber, false), secondMagicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
