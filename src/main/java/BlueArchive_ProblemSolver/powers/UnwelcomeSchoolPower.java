package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.actions.UnwelcomeSchoolAction;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.patches.cards.UseCardActionPatch;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;


//Gain 1 dex for the turn for each card played.

public class UnwelcomeSchoolPower extends AbstractPower implements CloneablePowerInterface, OnFrontPower {
    public static final String POWER_ID = DefaultMod.makeID("UnwelcomeSchoolPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("UnwelcomeSchoolPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("UnwelcomeSchoolPower32.png"));
    boolean upgrade;
    public UnwelcomeSchoolPower(final AbstractCreature owner, boolean upgrade) {
        name = NAME;
        ID = POWER_ID + (upgrade?"+":"");

        this.owner = owner;
        this.amount = 1;
        this.upgrade = upgrade;

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
        if(upgrade) {
            description = DESCRIPTIONS[3] + amount  + (amount>1?DESCRIPTIONS[5]:DESCRIPTIONS[4]);
        } else {
            description = DESCRIPTIONS[0] + amount + (amount>1?DESCRIPTIONS[2]:DESCRIPTIONS[1]);
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new UnwelcomeSchoolPower(owner, upgrade);
    }


    private void costDown(boolean upgrade) {
        ArrayList<AbstractCard> groupCopy = new ArrayList();
        Iterator var4 = AbstractDungeon.player.hand.group.iterator();

        while (var4.hasNext()) {
            AbstractCard c = (AbstractCard) var4.next();
            if (c.cost > 0 && c.costForTurn > 0 && !c.freeToPlayOnce) {
                groupCopy.add(c);
            }
        }

        var4 = AbstractDungeon.actionManager.cardQueue.iterator();

        while (var4.hasNext()) {
            CardQueueItem i = (CardQueueItem) var4.next();
            if (i.card != null) {
                groupCopy.remove(i.card);
            }
        }

        AbstractCard c = null;
        if (!groupCopy.isEmpty()) {
            Iterator var9 = groupCopy.iterator();

            while (var9.hasNext()) {
                AbstractCard cc = (AbstractCard) var9.next();
            }

            c = (AbstractCard) groupCopy.get(AbstractDungeon.cardRandomRng.random(0, groupCopy.size() - 1));
        }

        if (c != null) {
            if(upgrade)
                c.setCostForTurn(0);
            else
                c.setCostForTurn(c.cost - 1);
        }
    }

    @Override
    public void OnMoving() {
        this.flash();
        if(owner instanceof AbstractPlayer) {
            costDown(upgrade);
        }
    }

    @Override
    public void OnFront() {

    }

    @Override
    public void OnBack() {

    }
}
