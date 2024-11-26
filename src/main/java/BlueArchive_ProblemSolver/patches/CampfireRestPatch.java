package BlueArchive_ProblemSolver.patches;


import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;

@SpirePatch(
        clz = CampfireSleepEffect.class,
        method = "<ctor>"
)
public class CampfireRestPatch {
    public CampfireRestPatch() {
    }

    public static void Postfix(CampfireSleepEffect __instance) {
        if (AbstractDungeon.player instanceof Aru) {
            int healAmt_mod = 0;
            int max_health = 0;
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if (ProblemSolver68.isProblemSolver(ps.solverType)) {
                    max_health += ps.maxHealth;
                }
            }

            if (ModHelper.isModEnabled("Night Terrors")) {
                healAmt_mod = (int)((float)max_health * 1.0F);
            } else {
                healAmt_mod = (int)((float)max_health * 0.3F);
            }

            if (AbstractDungeon.player.hasRelic("Regal Pillow")) {
                healAmt_mod += 15;
            }

            ReflectionHacks.setPrivate(__instance, CampfireSleepEffect.class, "healAmount", healAmt_mod);
        }
    }
}

