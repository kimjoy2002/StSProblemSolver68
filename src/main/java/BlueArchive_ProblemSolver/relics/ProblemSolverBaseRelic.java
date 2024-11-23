package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddTemporaryHPToAllAllyAction;
import BlueArchive_ProblemSolver.cards.Shift;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ProblemSolverBaseRelic extends CustomRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("ProblemSolverBaseRelic");


    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ProblemSolverBaseRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ProblemSolverBaseRelic.png"));

    public ProblemSolverBaseRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }


    public void atBattleStartPreDraw() {
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new MakeTempCardInHandAction(new Shift(), 1, false));
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
