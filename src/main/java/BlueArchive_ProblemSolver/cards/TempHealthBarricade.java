package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FearPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TempHealthBarricade extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(TempHealthBarricade.class.getSimpleName());
    public static final String IMG = makeCardPath("TempHealthBarricade.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int BLOCK = 14;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    private int val = 0;

    public TempHealthBarricade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseBlock = BLOCK;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
        exhaust = true;
    }

    public void updateVal () {
        val = 0;
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var3.hasNext()) {
            AbstractMonster mo = (AbstractMonster)var3.next();
            for(AbstractPower power_ : mo.powers) {
                if(power_.type == AbstractPower.PowerType.DEBUFF) {
                    val += magicNumber;
                }
            }
        }

        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                for(AbstractPower power_ : ps.powers) {
                    if (power_.type == AbstractPower.PowerType.DEBUFF) {
                        val += magicNumber;
                    }
                }
            }
        } else {
            for(AbstractPower power_ : AbstractDungeon.player.powers) {
                if (power_.type == AbstractPower.PowerType.DEBUFF) {
                    val += magicNumber;
                }
            }
        }
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateVal ();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, val));
    }

    public void applyPowers() {
        updateVal();
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + val + cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }
    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
