package BlueArchive_ProblemSolver.patches;


import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "dungeonTransitionSetup"
)
public class BossHealPatch {
    public BossHealPatch() {
    }

    @SpireInsertPatch(
            rloc = 27
    )
    public static SpireReturn Insert() {
        if (AbstractDungeon.player instanceof Aru) {
            for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps != AbstractDungeon.player) {
                    if (AbstractDungeon.ascensionLevel >= 5) {
                        ps.heal(MathUtils.round((float)(ps.maxHealth - ps.currentHealth) * 0.75F), false);
                    } else {
                        ps.heal(ps.maxHealth, false);
                    }
                }
            }
        }
        return SpireReturn.Continue();
    }
}
