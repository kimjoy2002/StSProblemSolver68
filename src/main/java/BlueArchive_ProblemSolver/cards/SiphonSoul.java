package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.SiphonSoulAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.effects.UpdateHandEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static BlueArchive_ProblemSolver.targeting.ALLTargeting.CAN_ALL_TARGETING;
import static com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting.getTarget;
import static com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting.setCardTarget;

public class SiphonSoul extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(SiphonSoul.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("SiphonSoul.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CAN_ALL_TARGETING;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int MAGIC = 20;
    private static final int UPGRADE_PLUS_MAGIC = 5;
    private static final int MAGIC2 = 3;
    private static final int UPGRADE_PLUS_MAGIC2 = 1;


    public SiphonSoul() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature c = getTarget(this);

        applyPowers();
        if(c instanceof AbstractPlayer){
           if(c.hasPower(VulnerablePower.POWER_ID)) {
               damage *= 1.5f;
           }
        }
        else if(c instanceof AbstractMonster) {
            calculateCardDamage((AbstractMonster)c);
        }

        if(c != null) {
            this.addToBot(new SiphonSoulAction(c, p, new DamageInfo(p, this.damage, this.damageTypeForTurn), magicNumber, secondMagicNumber));
        }

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeSecondMagicNumber(UPGRADE_PLUS_MAGIC2);
            initializeDescription();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature c = getTarget(this);

        if(c == AbstractDungeon.player) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            setCardTarget(this, null);
            AbstractDungeon.effectsQueue.add(new UpdateHandEffect());
            return false;
        }
        return super.canUse(p, m);
    }
}
