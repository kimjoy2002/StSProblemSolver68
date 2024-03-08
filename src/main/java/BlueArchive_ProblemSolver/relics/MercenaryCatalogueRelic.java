package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class MercenaryCatalogueRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("MercenaryCatalogueRelic");

    private boolean firstTurn = true;
    public static final int GOLD = 8;
    public static final int HP = 40;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("MercenaryCatalogueRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("MercenaryCatalogueRelic.png"));

    public MercenaryCatalogueRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStartPostDraw() {
        if (this.firstTurn) {
            if(AbstractDungeon.player.gold > GOLD) {
                this.flash();
                AbstractDungeon.player.loseGold(GOLD);
                ArrayList<Aru.ProblemSolver68Type> able = new ArrayList<>();
                able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_IRONCLAD);
                able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SILENT);
                able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_DEFECT);
                able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_WATCHER);
                AbstractDungeon.actionManager.addToBottom(new AddCharacterAction(able.get(AbstractDungeon.miscRng.random(able.size() - 1)) , HP));
            }
            this.firstTurn = false;
            this.grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        this.grayscale = false;
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + GOLD + this.DESCRIPTIONS[1] + HP + this.DESCRIPTIONS[2];
    }
}
