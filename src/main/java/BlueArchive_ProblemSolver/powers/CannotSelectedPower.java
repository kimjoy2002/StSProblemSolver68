package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.actions.DelayAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class CannotSelectedPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = DefaultMod.makeID("CannotSelectedPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("CannotSelectedPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("CannotSelectedPower32.png"));

    public CannotSelectedPower(final AbstractCreature owner, int amount) {
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
        if (amount == 1) {
            description = DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0];
        }
    }
    public void stackPower(int stackAmount) {
        if (this.amount == -1) {
        } else if (stackAmount == 0) {
            this.amount = 0;
        } else {
            this.fontScale = 8.0F;
            this.amount = stackAmount;
        }
    }
    public void atEndOfRound() {
        if(amount > 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CannotSelectedPower(owner, amount);
    }
}
