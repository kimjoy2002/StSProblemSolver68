package BlueArchive_ProblemSolver.patches;


import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;

@SpirePatch(
        clz = CampfireUI.class,
        method = "updateCharacterPosition"
)
public class CampfireAnimationPatch {

    public static void Postfix(CampfireUI __instance) {
        if (AbstractDungeon.player instanceof ProblemSolver68) {
            float charAnimTimer = (float) ReflectionHacks.getPrivate(__instance, CampfireUI.class, "charAnimTimer");
            for( ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer ){

                switch (ps.solverType) {
                    case PROBLEM_SOLVER_68_ARU:
                        ps.animX = Interpolation.exp10In.apply(0.0F, -300.0F * Settings.scale, charAnimTimer / 2.0F);
                        break;
                    case PROBLEM_SOLVER_68_MUTSUKI:
                        ps.animX = Interpolation.exp10In.apply(0.0F, -550.0F * Settings.scale, charAnimTimer / 2.0F);
                        break;
                    case PROBLEM_SOLVER_68_KAYOKO:
                        ps.animX = Interpolation.exp10In.apply(0.0F, 550.0f * Settings.scale, charAnimTimer / 2.0F);
                        break;
                    case PROBLEM_SOLVER_68_HARUKA:
                        ps.animX = Interpolation.exp10In.apply(0.0F, 300.0F * Settings.scale, charAnimTimer / 2.0F);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

