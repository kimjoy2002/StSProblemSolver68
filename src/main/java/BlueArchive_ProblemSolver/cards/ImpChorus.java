package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.RemovePowerToAllAllyAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class ImpChorus extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(ImpChorus.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ImpChorus.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;
    public ImpChorus() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }


    public static int getImpCount() {
        int impcount = 0;
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.hasPower(ImpPower.POWER_ID)) {
                    impcount+=ps.getPower(ImpPower.POWER_ID).amount;
                }
            }

        } else {
            if(AbstractDungeon.player.hasPower(ImpPower.POWER_ID)) {
                impcount+=AbstractDungeon.player.getPower(ImpPower.POWER_ID).amount;
            }
        }

        return impcount;
    }
    // Actions the card should do.
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = getImpCount();
        for (int i = 0 ; i < count; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }


    public void applyPowers() {
        super.applyPowers();
        int count = getImpCount();
        if(count > 0) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1] + count + cardStrings.EXTENDED_DESCRIPTION[2];
        } else {
            this.rawDescription = cardStrings.DESCRIPTION;
        }
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
    public void atTurnStartPreDraw() {
        initializeDescription();
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        int count = getImpCount();
        if(count == 0){
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p,m);
    }
}
