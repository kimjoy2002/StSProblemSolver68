package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.FearEscapeAction;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class FearPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = DefaultMod.makeID("FearPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("FearPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("FearPower32.png"));

    public FearPower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
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

    public void checkFear() {
        if(!owner.isEscaping && !owner.isDying && owner instanceof AbstractMonster &&
            amount >= owner.currentHealth) {
            if(owner instanceof AwakenedOne) {
                boolean form1 = ReflectionHacks.getPrivate(owner, AwakenedOne.class, "form1");
                if(form1) {
                    AbstractDungeon.actionManager.addToBottom(new InstantKillAction(owner));
                    return;
                }
            }
            if(owner.id == "Darkling") {
                AbstractDungeon.actionManager.addToBottom(new InstantKillAction(owner));
            } else {
                AbstractDungeon.actionManager.addToBottom(new FearEscapeAction((AbstractMonster)owner));
            }
        }
    }
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        checkFear();
    }


    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkFear();
    }
    public int onLoseHp(int damageAmount) {
        checkFear();
        return damageAmount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new FearPower(owner, amount);
    }

}
