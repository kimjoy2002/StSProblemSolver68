package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.CreateTacticalChallengeAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ForeignInfluenceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TacticalChallengePS68 extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(TacticalChallengePS68.class.getSimpleName());
    public static final String IMG = makeCardPath("TacticalChallengePS68.png");


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 3;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;
    public TacticalChallengePS68() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        baseMagicNumber = magicNumber = MAGIC;

        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);

        this.tags.add(EnumPatch.TACTICAL_CHALLENGE);

        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new CreateTacticalChallengeAction(this.upgraded));
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
