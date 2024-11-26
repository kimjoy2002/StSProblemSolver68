package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.RemovePowerToAllAllyAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.powers.TauntPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class AttractAttention extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(AttractAttention.class.getSimpleName());
    public static final String IMG = makeCardPath("AttractAttention.png");


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int MAGIC = 10;
    private static final int UPGRADE_PLUS_MAGIC = 4;



    public AttractAttention() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, magicNumber), magicNumber));
        AbstractDungeon.actionManager.addToBottom(new RemovePowerToAllAllyAction(TauntPower.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TauntPower(p)));
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
