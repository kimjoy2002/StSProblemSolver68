package BlueArchive_ProblemSolver.patches;


import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CtBehavior;

import java.util.Iterator;

@SpirePatch(
        clz = CampfireSleepEffect.class,
        method = "update"
)
public class CampfireRestResultPatch {
    public CampfireRestResultPatch() {
    }

    @SpireInsertPatch(
            locator = Locator.class

    )
    public static void Insert(CampfireSleepEffect __instance) {
        if (AbstractDungeon.player instanceof Aru) {
            boolean hasHealed = (boolean)ReflectionHacks.getPrivate(__instance, CampfireSleepEffect.class, "hasHealed");
            if (__instance.duration < __instance.startingDuration - 0.5F && !hasHealed) {
                ReflectionHacks.RMethod playSleepJingle = ReflectionHacks.privateMethod(CampfireSleepEffect.class,"playSleepJingle");
                playSleepJingle.invoke(__instance);

                ReflectionHacks.setPrivate(__instance, CampfireSleepEffect.class, "hasHealed", true);
                if (AbstractDungeon.player.hasRelic("Regal Pillow")) {
                    AbstractDungeon.player.getRelic("Regal Pillow").flash();
                }
                int toHeal = (Integer)ReflectionHacks.getPrivate(__instance, CampfireSleepEffect.class, "healAmount");
                int total_heal = toHeal;
                int remain_totalhealth = 0;
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ProblemSolver68.isProblemSolver(ps.solverType)) {
                        remain_totalhealth += ps.maxHealth - ps.currentHealth;
                    }
                }
                int will_heal[] = new int[ProblemSolver68.problemSolverPlayer.size()];
                int i = 0;
                if(remain_totalhealth > 0) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        if (ProblemSolver68.isProblemSolver(ps.solverType)) {
                            int remain_health = ps.maxHealth - ps.currentHealth;
                            double healAmount = (double) remain_health * toHeal / remain_totalhealth;
                            will_heal[i] = (int) healAmount;
                            total_heal -= will_heal[i];
                        }
                        i++;
                    }
                    for(int j = 0;total_heal>0 && j < ProblemSolver68.problemSolverPlayer.size();j++) {
                        if (ProblemSolver68.isProblemSolver(ProblemSolver68.problemSolverPlayer.get(j).solverType) && ProblemSolver68.problemSolverPlayer.get(j).currentHealth < ProblemSolver68.problemSolverPlayer.get(j).maxHealth) {
                            will_heal[j]++;
                            total_heal--;
                        }
                    }

                    for(int j = 0;j < ProblemSolver68.problemSolverPlayer.size();j++) {
                        if (ProblemSolver68.isProblemSolver(ProblemSolver68.problemSolverPlayer.get(j).solverType)) {
                            ProblemSolver68.problemSolverPlayer.get(j).heal(will_heal[j], false);
                            ProblemSolver68.problemSolverPlayer.get(j).isDead = false;
                        }
                    }
                }

                Iterator var1 = AbstractDungeon.player.relics.iterator();

                while (var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic) var1.next();
                    r.onRest();
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CampfireSleepEffect.class, "hasHealed");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}

