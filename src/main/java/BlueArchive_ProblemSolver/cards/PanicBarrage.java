package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.PanicBarrageAction;
import BlueArchive_ProblemSolver.actions.SweepAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class PanicBarrage extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(PanicBarrage.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("PanicBarrage.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int DAMAGE = 4;

    public static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;



    public PanicBarrage() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for(int i = 0; i < magicNumber; i++) {
            this.addToBot(new PanicBarrageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }
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
