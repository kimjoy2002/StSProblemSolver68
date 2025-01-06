package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ImpAmountAction;
import BlueArchive_ProblemSolver.cards.ImpChorus;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class ImpPower extends AbstractPower implements CloneablePowerInterface, ForSubPower {
    public static final String POWER_ID = DefaultMod.makeID("ImpPower");

    public int amount_imp = 0;
    public boolean moving = false;
    private Color greenColor_imp = new Color(0.0F, 1.0F, 0.0F, 1.0F);
    private Color normalColor_imp = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("ImpPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("ImpPower32.png"));

    public ImpPower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount_imp = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }
    public ImpPower(final AbstractCreature owner, int amount, boolean moving) {
        this(owner, amount);
        this.moving = moving;
    }

    public float atDamageGiveForSub(float damage, AbstractCard card, DamageInfo.DamageType type) {
        return (type == DamageInfo.DamageType.NORMAL && !(card instanceof ImpChorus)) ? damage + (float)this.amount_imp : damage;
    }

    public void onUseCardForSub(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && amount_imp > 0 && !(card instanceof ImpChorus)) {
            this.flash();
            this.addToBot(new ImpAmountAction(0, this, false));
        }
    }


    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.amount_imp += stackAmount;
    }

    public void reducePower(int reduceAmount) {
        if (this.amount - reduceAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount = 0;
        } else {
            this.fontScale = 8.0F;
            this.amount -= reduceAmount;
        }
        this.addToBot(new ImpAmountAction(-1, this, false));

    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        if (this.amount > 0) {
            c = this.greenColor_imp;

            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x, y, this.fontScale, c);
            if(amount_imp == 0){
                c = this.normalColor_imp;
            }
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount_imp), x- (float)this.region48.packedWidth / 2.0F-FontHelper.layout.width / 2.0F, y, this.fontScale, c);
        }

    }

    public void atStartOfTurnPostDraw() {
        this.addToBot(new ImpAmountAction(999, null, false));
    }

    @Override
    public void updateDescription() {
        if(amount_imp > 0) {
            description = DESCRIPTIONS[0] + amount_imp + DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
        }
    }


    @Override
    public AbstractPower makeCopy() {
        ImpPower temp = new ImpPower(owner, amount);
        temp.amount_imp = amount_imp;
        return temp;
    }

}
