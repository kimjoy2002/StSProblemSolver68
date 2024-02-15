package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ProblemSolverBaseRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ProblemSolverBaseRelic");

    private boolean firstTurn = true;
    public static final int HEAL = 5;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ProblemSolverBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ProblemSolverBaseRelic.png"));

    public ProblemSolverBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
            this.firstTurn = false;
            this.grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + HEAL + this.DESCRIPTIONS[1];
    }
}
