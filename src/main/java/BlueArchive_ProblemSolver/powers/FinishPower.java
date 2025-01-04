package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ImpAmountAction;
import BlueArchive_ProblemSolver.cards.FinishCard;
import BlueArchive_ProblemSolver.cards.ImpChorus;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;
import static com.badlogic.gdx.graphics.Color.WHITE;


//Gain 1 dex for the turn for each card played.

public class FinishPower extends AbstractPower implements CloneablePowerInterface, ForSubPower {
    public static final String POWER_ID = DefaultMod.makeID("FinishPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public abstract static class FinishString {
        abstract public String getFinishString(int amount);
        abstract public AbstractCard getCard();
    }


    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("FinishPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("FinishPower32.png"));
    public ArrayList<AbstractGameAction> actions;
    private static int finishIdOffset;
    public int finish_count;
    public int self_finish_count = 0;

    private FinishString stringFunc;
    String text;
    public FinishPower(final AbstractCreature player, ArrayList<AbstractGameAction> actions, FinishString stringFunc, boolean isDebuf) {
        name = NAME;
        ID = POWER_ID + finishIdOffset;
        ++finishIdOffset;

        this.owner = player;
        this.amount = 1;
        this.stringFunc = stringFunc;
        this.text = stringFunc.getFinishString(1);
        this.actions = actions;

        type = isDebuf?PowerType.DEBUFF:PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    public void setSelfFinishCount() {
        AbstractList<AbstractPower> finishList = new ArrayList<>();

        for(AbstractPower p : owner.powers) {
            if(p.ID.startsWith(FinishPower.POWER_ID)) {
                finishList.add(p);
            }
        }

        finishList.sort((a, b) -> {
            int numberA = Integer.parseInt(a.ID.replace(FinishPower.POWER_ID, ""));
            int numberB = Integer.parseInt(b.ID.replace(FinishPower.POWER_ID, ""));
            return Integer.compare(numberA, numberB);
        });
        int i = 0;
        for(AbstractPower power : finishList) {
            if(power == this) {
                self_finish_count = i;
                break;
            }
            i++;
        }
    }
    public void setFinishCount() {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                AbstractList<AbstractPower> finishList = new ArrayList<>();
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ps.currentHealth > 0) {
                        for (AbstractPower p : ps.powers) {
                            if (p.ID.startsWith(FinishPower.POWER_ID)) {
                                finishList.add(p);
                            }
                        }
                    }
                }

                finishList.sort((a, b) -> {
                    int numberA = Integer.parseInt(a.ID.replace(FinishPower.POWER_ID, ""));
                    int numberB = Integer.parseInt(b.ID.replace(FinishPower.POWER_ID, ""));
                    return Integer.compare(numberA, numberB);
                });
                int i = 1;
                for(AbstractPower power : finishList) {
                    if(power == this) {
                        finish_count = i;
                        break;
                    }
                    i++;
                }
            }
        } else {
            AbstractList<AbstractPower> finishList = new ArrayList<>();

            for(AbstractPower p : AbstractDungeon.player.powers) {
                if(p.ID.startsWith(FinishPower.POWER_ID)) {
                    finishList.add(p);
                }
            }

            finishList.sort((a, b) -> {
                int numberA = Integer.parseInt(a.ID.replace(FinishPower.POWER_ID, ""));
                int numberB = Integer.parseInt(b.ID.replace(FinishPower.POWER_ID, ""));
                return Integer.compare(numberA, numberB);
            });
            int i = 1;
            for(AbstractPower power : finishList) {
                if(power == this) {
                    finish_count = i;
                    break;
                }
                i++;
            }
        }
    }

    public void onInitialApplication() {
        setFinishCount();
        setSelfFinishCount();
    }
    public void onUseCardForSub(AbstractCard card, UseCardAction action) {
        setFinishCount();
        setSelfFinishCount();
    }
    public void renderFinish(SpriteBatch sb, float x, float y) {
        AbstractCard card_ = stringFunc.getCard();
        if(owner instanceof AbstractPlayer && card_ != null) {
            AbstractPlayer player_ = (AbstractPlayer)owner;
            float tX = player_.drawX + player_.hb_w/2;
            float tY = player_.drawY + player_.hb_h+ (200.0f*card_.drawScale * Settings.scale)*self_finish_count;

            try {
                card_.current_x = tX;
                card_.current_y = tY;
                Method renderCardBg = ReflectionHacks.getCachedMethod(AbstractCard.class, "renderCardBg", SpriteBatch.class, float.class, float.class);

                renderCardBg.invoke(card_, sb, tX, tY);


                if (!UnlockTracker.betaCardPref.getBoolean(card_.cardID, false) && !Settings.PLAYTESTER_ART_MODE) {
                    Method renderPortrait = ReflectionHacks.getCachedMethod(AbstractCard.class, "renderPortrait", SpriteBatch.class);
                    renderPortrait.invoke(card_, sb);
                } else {
                    Method renderJokePortrait = ReflectionHacks.getCachedMethod(AbstractCard.class, "renderJokePortrait", SpriteBatch.class);
                    renderJokePortrait.invoke(card_, sb);
                }

                if(finish_count > 0) {
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.finish_count), tX + 150.0f*card_.drawScale * Settings.scale, tY + 150.0f*card_.drawScale * Settings.scale, this.fontScale, WHITE);
                }
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }


    public void atEndOfTurn(boolean isPlayer) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            for (AbstractGameAction action : actions) {
                this.addToBot(action);
            }
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        }
    }

    @Override
    public void updateDescription() {
        text = stringFunc.getFinishString(amount);
        if(!text.isEmpty()) {
            description = DESCRIPTIONS[0] + text;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FinishPower(owner, actions, stringFunc, type == PowerType.BUFF);
    }

}
