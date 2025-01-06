package BlueArchive_ProblemSolver.patches.stance;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;

public class StancePatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "resetPlayer"
    )
    public static class ResetPlayerPatch {
        public static void Postfix()
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        ps.orbs.clear();
                        ps.loseBlock(true);
                        if (!ps.stance.ID.equals("Neutral")) {
                            ps.stance.onExitStance();
                            ps.stance = new NeutralStance();
                            ps.onStanceChange("Neutral");
                        }
                    }
                }
            }
        }
    }


}
