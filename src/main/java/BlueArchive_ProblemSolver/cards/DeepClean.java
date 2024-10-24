package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class DeepClean extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(DeepClean.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("DeepClean.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int DAMAGE_MUTSUKI = 5;
    private static final int UPGRADE_DMG_MUTSUKI = 5;
    private static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = 1;


    public DeepClean() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;

        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            if (((ProblemSolver68) AbstractDungeon.player).solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU) {
                this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
                return;
            } else if (((ProblemSolver68) AbstractDungeon.player).solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI) {
                for (int i = 0 ; i < magicNumber; i++) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                return;
            }
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
    public void applyPowers() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            if(((ProblemSolver68)AbstractDungeon.player).solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU) {
                baseDamage = DAMAGE + (upgraded ?UPGRADE_PLUS_DMG:0) ;
                this.isMultiDamage = true;
                this.target = CardTarget.ALL_ENEMY;
                super.applyPowers();
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
                this.initializeDescription();
                return;
            }
            else if(((ProblemSolver68)AbstractDungeon.player).solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI) {
                baseDamage = (upgraded ?UPGRADE_DMG_MUTSUKI:DAMAGE_MUTSUKI) ;
                this.isMultiDamage = false;
                this.target = CardTarget.ENEMY;
                super.applyPowers();
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
                this.initializeDescription();

                return;
            }
        }
        baseDamage = DAMAGE + (upgraded ?UPGRADE_PLUS_DMG:0) ;
        this.isMultiDamage = false;
        this.target = CardTarget.ENEMY;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        baseDamage = DAMAGE + (upgraded ?UPGRADE_PLUS_DMG:0) ;
        this.isMultiDamage = false;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
