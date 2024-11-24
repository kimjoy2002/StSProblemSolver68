package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;
import static com.megacrit.cardcrawl.actions.common.DrawCardAction.drawnCards;


//Gain 1 dex for the turn for each card played.

public class DestinyDrawPower extends AbstractPower implements CloneablePowerInterface, OnRefreshHandPower, SharedPower {
    public static final String POWER_ID = DefaultMod.makeID("DestinyDrawPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("DestinyDrawPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("DestinyDrawPower32.png"));

    private static final Texture untex84 = TextureLoader.getTexture(makePowerPath("DestinyDrawUsedPower84.png"));
    private static final Texture untex32 = TextureLoader.getTexture(makePowerPath("DestinyDrawUsedPower32.png"));
    private boolean disabledUntilEndOfTurn = false;

    public DestinyDrawPower(final AbstractCreature owner, int amount) {
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

    public void updateDescription() {
        if(!disabledUntilEndOfTurn) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        }
    }

    private void enablePower() {
        this.disabledUntilEndOfTurn = false;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    private void disablePower() {
        this.disabledUntilEndOfTurn = true;
        this.region128 = new TextureAtlas.AtlasRegion(untex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(untex32, 0, 0, 32, 32);
        updateDescription();
    }

    public void atStartOfTurnPostDraw() {
        enablePower();
    }

    public void onRefreshHand() {
        if (AbstractDungeon.actionManager.actions.isEmpty() && AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded && !AbstractDungeon.player.hasPower("No Draw") && !AbstractDungeon.isScreenUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !this.disabledUntilEndOfTurn && (AbstractDungeon.player.discardPile.size() > 0 || AbstractDungeon.player.drawPile.size() > 0)) {
            this.flash();
            disablePower();
            this.addToBot(new DrawCardAction(amount, new AbstractGameAction() {
                @Override
                public void update() {
                    for(AbstractCard card : drawnCards){
                        if(card.cost > 0 && !card.freeToPlayOnce) {
                            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(card.cost));
                        }
                    }
                    isDone = true;
                }
            }));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DestinyDrawPower(owner, amount);
    }
}
