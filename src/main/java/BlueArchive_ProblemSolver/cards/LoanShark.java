package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class LoanShark extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(LoanShark.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("LoanShark.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 0;
    public static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;
    public static final int MAGIC2 = 2;

    public LoanShark() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, -secondMagicNumber), -secondMagicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(p.hasPower(StrengthPower.POWER_ID)) {
            if(((StrengthPower)p.getPower(StrengthPower.POWER_ID)).amount < 0) {
                this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
                return false;
            }
        }
        return super.canUse(p, m);
    }
}
