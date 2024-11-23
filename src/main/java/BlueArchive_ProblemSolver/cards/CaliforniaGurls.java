package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.CaliforniaGurlsPower;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class CaliforniaGurls extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(CaliforniaGurls.class.getSimpleName());
    public static final String IMG = makeCardPath("CaliforniaGurls.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 3;
    private static final int MAGIC = 2;



    public CaliforniaGurls() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPlayer target = null;
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0 &&  ps.solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI) {
                    target = ps;
                    break;
                }
            }
        }
        if(target != null) {
            AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(target, false, true, false));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, target, new CaliforniaGurlsPower(target, magicNumber), magicNumber));
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(2);
            initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(!(p instanceof ProblemSolver68) || !ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI)){
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p,m);
    }
}
