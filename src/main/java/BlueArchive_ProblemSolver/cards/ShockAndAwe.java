package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FearPower;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.DoubleYourBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class ShockAndAwe extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(ShockAndAwe.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ShockAndAwe.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;

    private static final int MAGIC = 2;

    public ShockAndAwe() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {

            public void updateFearAndImp(AbstractCreature c) {
                if(c.hasPower(FearPower.POWER_ID)) {
                    this.addToTop(new ApplyPowerAction(c, p, new FearPower(c, c.getPower(FearPower.POWER_ID).amount), c.getPower(FearPower.POWER_ID).amount));
                }
                if(c.hasPower(ImpPower.POWER_ID)) {
                    this.addToTop(new ApplyPowerAction(c, p, new ImpPower(c, c.getPower(ImpPower.POWER_ID).amount), c.getPower(ImpPower.POWER_ID).amount));
                }
            }

            @Override
            public void update() {

                Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                while(var3.hasNext()) {
                    AbstractMonster mo = (AbstractMonster)var3.next();
                    updateFearAndImp(mo);
                }

                if(AbstractDungeon.player instanceof ProblemSolver68) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        updateFearAndImp(ps);
                    }
                } else {
                    updateFearAndImp(AbstractDungeon.player);
                }

                this.isDone = true;
            }
        });
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
