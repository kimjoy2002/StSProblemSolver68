package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.actions.SolverTalkAction;
import BlueArchive_ProblemSolver.cards.YouWillPayForThis;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.Random;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class DeadStrPower extends AbstractPower implements CloneablePowerInterface, OnDeadPower {
    public static final String POWER_ID = DefaultMod.makeID("DeadStrPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DeadStrPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DeadStrPower32.png"));

    public DeadStrPower(final AbstractCreature owner, int amount) {
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
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }


    @Override
    public void onDead(AbstractPlayer dead) {
        if(dead != owner && dead instanceof ProblemSolver68) {
            int increase = amount;
            if(ProblemSolver68.isProblemSolver(((ProblemSolver68)dead).solverType)) {
                if(YouWillPayForThis.cardStrings.EXTENDED_DESCRIPTION.length > 0) {
                    String text = YouWillPayForThis.cardStrings.EXTENDED_DESCRIPTION[MathUtils.random(YouWillPayForThis.cardStrings.EXTENDED_DESCRIPTION.length-1)];
                    text = String.format(text, ProblemSolver68.getLocalizedName((ProblemSolver68)dead));
                    AbstractDungeon.actionManager.addToBottom(new SolverTalkAction(owner, text, 2.0F, 2.0F));
                }
                increase *= 2;
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, increase), increase));
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new DeadStrPower(owner, amount);
    }

}
