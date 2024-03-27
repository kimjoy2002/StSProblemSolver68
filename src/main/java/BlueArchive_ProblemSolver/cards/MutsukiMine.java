package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.DelayAction;
import BlueArchive_ProblemSolver.actions.RushOnOffAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.MineExpertPower;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static java.lang.Math.abs;

public class MutsukiMine extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(MutsukiMine.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("MutsukiMine.png");


    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;
    private static final int MAGIC = 5;
    private static final int UPGRADE_PLUS_MAGIC = 2;



    public MutsukiMine() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        selfRetain = true;
    }
    public void updateVal () {
        this.magicNumber = this.baseMagicNumber = MAGIC;
        this.magicNumber += ProblemSolver68.getPowerValue(MineExpertPower.POWER_ID);
        if (baseMagicNumber != magicNumber) {
            this.isMagicNumberModified = true;
        }
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
        this.addToBot(new DamageRandomEnemyAction(new DamageInfo(p, MathUtils.floor(magicNumber), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        this.addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(AbstractDungeon.player.hand.contains(c)) {
            int index_ = AbstractDungeon.player.hand.group.indexOf(c);
            int index2_ = AbstractDungeon.player.hand.group.indexOf(this);
            if(index2_ >= 0 && index_ >= 0 && abs(index_ - index2_)<=1) {
                this.addToBot(new DelayAction(new DiscardSpecificCardAction(this),3));
            }
        }
    }

    public void triggerOnManualDiscard() {
        use(AbstractDungeon.player, null);
    }
    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
