package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class SupportShotPower extends AbstractPower implements CloneablePowerInterface, ForSubPower {
    public static final String POWER_ID = DefaultMod.makeID("SupportShotPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("SupportShotPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("SupportShotPower32.png"));
    AbstractCreature target;
    private static int supportOffset;
    public SupportShotPower(final AbstractCreature owner, AbstractCreature target, int amount) {
        name = NAME;
        ID = POWER_ID + supportOffset;
        ++supportOffset;

        this.target = target;
        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }


    @Override
    public void onUseCardForSub(AbstractCard card, UseCardAction action) {
        if(AbstractDungeon.player != owner && card.type == AbstractCard.CardType.ATTACK
                && target != null && target.currentHealth > 0 && !target.halfDead && !target.isDying) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(owner));
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(target, new DamageInfo(this.owner, amount, DamageInfo.DamageType.THORNS),
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }
    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
    }
    @Override
    public AbstractPower makeCopy() {
        return new SupportShotPower(owner, target, amount);
    }

}
