package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;
import static BlueArchive_ProblemSolver.patches.GameActionManagerPatch.evildeedThisTurn;


public class OneEvilDeedaDayPower extends AbstractPower implements CloneablePowerInterface, OnEvilDeedsPower, SharedPower {
    public static final String POWER_ID = DefaultMod.makeID("OneEvilDeedaDayPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("OneEvilDeedaDayPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("OneEvilDeedaDayPower32.png"));

    private static final Texture untex84 = TextureLoader.getTexture(makePowerPath("OneEvilDeedaDayUsedPower84.png"));
    private static final Texture untex32 = TextureLoader.getTexture(makePowerPath("OneEvilDeedaDayUsedPower32.png"));
    private boolean disabledUntilEndOfTurn = false;
    public OneEvilDeedaDayPower(final AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
        checkPower();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        for (int i = 0; i < amount; i++) {
            this.description += DESCRIPTIONS[1];
        }
        this.description += DESCRIPTIONS[2];
        if(disabledUntilEndOfTurn) {
            this.description += DESCRIPTIONS[3];
        }
    }


    private void enablePower() {
        disabledUntilEndOfTurn = false;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    private void disablePower() {
        disabledUntilEndOfTurn = true;
        this.region128 = new TextureAtlas.AtlasRegion(untex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(untex32, 0, 0, 32, 32);
        updateDescription();
    }



    private void checkPower() {
        if(this.amount > 0 && evildeedThisTurn < 1) {
            enablePower();
        } else {
            disablePower();
        }
    }


    public void atStartOfTurn() {
        checkPower();
    }
    @Override
    public void onEvilDeeds(AbstractCard card) {
        if (this.amount > 0 && evildeedThisTurn <= 1) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
            checkPower();
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new OneEvilDeedaDayPower(owner, amount);
    }

}
