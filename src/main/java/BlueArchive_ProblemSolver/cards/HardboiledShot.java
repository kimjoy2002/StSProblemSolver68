package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class HardboiledShot extends EvilDeedsCard {
    public static final String ID = DefaultMod.makeID(HardboiledShot.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("HardboiledShot.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    public static final int MAGIC = 4;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public HardboiledShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setRequireEvil(1);
        limit = false;
    }

    @Override
    public CardStrings getCardStrings() {
        return cardStrings;
    }


    public void updateVal () {
        this.baseDamage = DAMAGE;
        this.baseDamage += magicNumber * evil;
    }
    public void applyPowers() {
        updateVal();
        super.applyPowers();
        if(evil > 0) {
            this.isDamageModified = true;
        }
        this.makeDescrption();
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        this.baseDamage = DAMAGE;
        super.onMoveToDiscard();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateVal ();
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        if(p instanceof ProblemSolver68 && ((ProblemSolver68)p).solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU) {
            AnimationState.TrackEntry e = AbstractDungeon.player.state.setAnimation(0, Aru.SKILL_ANIMATION, false);
            AbstractDungeon.player.state.addAnimation(0, Aru.BASE_ANIMATION, true, e.getEndTime());
        }

        super.use(p, m);
    }

    @Override
    public void onEvilDeeds(AbstractPlayer p, AbstractMonster m) {
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            makeDescrption();
        }
    }
}
