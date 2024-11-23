package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cards.Shift;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class ProblemSolverBaseRelicPlus extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ProblemSolverBaseRelicPlus");


    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ProblemSolverBaseRelicPlus.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ProblemSolverBaseRelicPlus.png"));

    public ProblemSolverBaseRelicPlus() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    public void atTurnStart() {
        if(AbstractDungeon.player.hand.findCardById(Shift.ID) == null) {
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            Shift s = new Shift();
            s.upgrade();
            this.addToBot(new MakeTempCardInHandAction(s, 1, false));
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ProblemSolverBaseRelic.ID)) {
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (((AbstractRelic)AbstractDungeon.player.relics.get(i)).relicId.equals(ProblemSolverBaseRelic.ID)) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ProblemSolverBaseRelic.ID);
    }
}
