package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddChellengeCountAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TacticalChallengeAru extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(TacticalChallengeAru.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("TacticalChallengeAru.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;



    public TacticalChallengeAru() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);

        this.tags.add(EnumPatch.TACTICAL_CHALLENGE);
    }

    public void updateVal () {
        this.baseDamage = DAMAGE;
        this.baseDamage += magicNumber * GameActionManagerPatch.tacticalChallengeCount;
    }
    public void applyPowers() {
        updateVal();
        super.applyPowers();
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        updateVal();
        super.applyPowers();
        this.initializeDescription();
    }
    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateVal();
        this.calculateCardDamage(m);
        if (m != null) {
            this.addToBot(new VFXAction(new ClawEffect(m.hb.cX, m.hb.cY, Color.CYAN, Color.WHITE), 0.1F));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));
        AbstractDungeon.actionManager.addToBottom(new AddChellengeCountAction());
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
