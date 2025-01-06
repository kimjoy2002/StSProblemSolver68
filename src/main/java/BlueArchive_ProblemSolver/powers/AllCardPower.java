package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.cards.UseCardActionPatch;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;

public class AllCardPower extends AbstractPower implements CloneablePowerInterface, ForSubPower {
    public static final String POWER_ID = DefaultMod.makeID("AllCardPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("AllCardPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("AllCardPower32.png"));

    public AllCardPower(final AbstractCreature owner, int amount) {
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
        if (this.amount == 1) {
            this.description = powerStrings.DESCRIPTIONS[0];
        } else {
            this.description = powerStrings.DESCRIPTIONS[1] + this.amount + powerStrings.DESCRIPTIONS[2];
        }
    }



    public void onUseCard(AbstractCard card, UseCardAction action) {

        if (!card.purgeOnUse && (card.type == AbstractCard.CardType.SKILL || card.type == AbstractCard.CardType.ATTACK) && this.amount > 0) {
            this.flash();
            if (owner instanceof ProblemSolver68) {
                for(int i = 0; i < ProblemSolver68.problemSolverPlayer.size(); i++) {
                    ProblemSolver68 ps = ProblemSolver68.problemSolverPlayer.get(i);
                    if (ps != owner && ps.currentHealth > 0) {
                        AbstractMonster m = null;
                        if (action.target != null) {
                            m = (AbstractMonster) action.target;
                        }
                        AbstractCard tmp = card.makeSameInstanceOf();
                        AbstractDungeon.player.limbo.addToBottom(tmp);
                        tmp.current_x = card.current_x;
                        tmp.current_y = card.current_y;
                        tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                        tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                        if (m != null) {
                            tmp.calculateCardDamage(m);
                        }
                        UseCardActionPatch.AbstractCardField.castPlayer.set(tmp, ps);
                        tmp.purgeOnUse = true;
                        AbstractDungeon.actionManager.cardQueue.add(1, new CardQueueItem(tmp, m, card.energyOnUse, true, true));
                    }
                }
            }

            --this.amount;
            if (this.amount == 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, AllCardPower.POWER_ID));
            }
        }

    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, AllCardPower.POWER_ID));
        }

    }


    @Override
    public AbstractPower makeCopy() {
        return new AllCardPower(owner, amount);
    }
}

