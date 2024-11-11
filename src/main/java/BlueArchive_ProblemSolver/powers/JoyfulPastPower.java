package BlueArchive_ProblemSolver.powers;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_ProblemSolver.DefaultMod.makePowerPath;

public class JoyfulPastPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = DefaultMod.makeID("JoyfulPastPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("JoyfulPastPower84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("JoyfulPastPower32.png"));

    public JoyfulPastPower(final AbstractCreature owner, int amount) {
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
        if(amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }


    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if(isPlayer) {
            int powerAmount = amount;
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    int value = 0;
                    if(AbstractDungeon.player instanceof ProblemSolver68) {
                        for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                            if(ps.hasPower(StrengthPower.POWER_ID) && ps.getPower(StrengthPower.POWER_ID).amount > 0){
                                value += ps.getPower(StrengthPower.POWER_ID).amount;
                            }
                            if(ps.hasPower(ImpPower.POWER_ID)) {
                                value +=ps.getPower(ImpPower.POWER_ID).amount;
                            }
                        }
                    } else {
                        if(AbstractDungeon.player.hasPower(StrengthPower.POWER_ID) && AbstractDungeon.player.getPower(POWER_ID).amount > 0){
                            value += AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
                        }
                        if(AbstractDungeon.player.hasPower(ImpPower.POWER_ID)) {
                            value += AbstractDungeon.player.getPower(ImpPower.POWER_ID).amount;
                        }
                    }

                    if(powerAmount*value > 0) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, powerAmount*value));
                    }
                    isDone = true;
                }
            });
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new JoyfulPastPower(owner, amount);
    }

}
