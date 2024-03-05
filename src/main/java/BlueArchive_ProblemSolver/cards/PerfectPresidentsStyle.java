package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ApplyPowerToAllAllyAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.powers.OutlawsRockPower;
import BlueArchive_ProblemSolver.powers.PerfectPresidentsIntegiblePower;
import BlueArchive_ProblemSolver.powers.PerfectPresidentsStrPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class PerfectPresidentsStyle extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(PerfectPresidentsStyle.class.getSimpleName());
    public static final String IMG = makeCardPath("PerfectPresidentsStyle.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 3;
    private static final int MAGIC2 = 1;



    public PerfectPresidentsStyle() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToAllAllyAction(new StrengthPower(AbstractDungeon.player, magicNumber),magicNumber, ApplyPowerToAllAllyAction.PowerToCharacterType.ONLY_MERCENARY));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToAllAllyAction(new IntangiblePlayerPower(AbstractDungeon.player, secondMagicNumber),secondMagicNumber, ApplyPowerToAllAllyAction.PowerToCharacterType.ONLY_MERCENARY));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PerfectPresidentsStrPower(AbstractDungeon.player, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PerfectPresidentsIntegiblePower(AbstractDungeon.player, secondMagicNumber), secondMagicNumber));
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
