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

    public ForAruSama() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new ForAruSamaPower(AbstractDungeon.player, 1), 1, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(!(p instanceof ProblemSolver68) ||
             !ProblemSolver68.hasCharacter(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)){

            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p,m);
    }
}
