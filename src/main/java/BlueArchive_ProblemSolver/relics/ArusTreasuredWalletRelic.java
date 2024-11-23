package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddTemporaryHPToAllAllyAction;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class ArusTreasuredWalletRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ArusTreasuredWalletRelic");

    private boolean firstTurn = true;
    public static final int HEAL = 3;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ArusTreasuredWalletRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ArusTreasuredWalletRelic.png"));

    public ArusTreasuredWalletRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPToAllAllyAction(HEAL));
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
