package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.GoldenIdolEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

public class GoldenIdolPatch {
    private static final EventStrings strings;


    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:GoldenIdol");
    }

    @SpirePatch(
            clz = GoldenIdolEvent.class,
            method = "buttonEffect"
    )
    public static class GoldenIdolDamage {
        @SpireInsertPatch(
                rloc = 56
        )
        public static void Insert(GoldenIdolEvent __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, GoldenIdolEvent.class, "damage");
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
            }

        }
    }

    @SpirePatch(
            clz = GoldenIdolEvent.class,
            method = "buttonEffect"
    )
    public static class GoldenIdolMaxHpDamage {
        @SpireInsertPatch(
                rloc = 67
        )
        public static void Insert(GoldenIdolEvent __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        int maxHpLoss = 0;
                        if(AbstractDungeon.ascensionLevel >= 15) {
                            maxHpLoss = (int)(ps.maxHealth * 0.1f);
                        } else {
                            maxHpLoss  = (int)(ps.maxHealth * 0.08f);
                        }
                        if(maxHpLoss < 1) {
                            maxHpLoss = 1;
                        }
                        ps.decreaseMaxHealth(maxHpLoss);
                    }
                }
            }

        }
    }

    @SpirePatch(
            clz = GoldenIdolEvent.class,
            method = "buttonEffect"
    )
    public static class GoldenIdolInit {
        @SpireInsertPatch(
                rloc = 23
        )
        public static void Insert(GoldenIdolEvent __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = (Integer) ReflectionHacks.getPrivate(__instance, GoldenIdolEvent.class, "damage");

                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[0] + dmg + GoldenIdolEvent.OPTIONS[4] );
                __instance.imageEventText.updateDialogOption(2, strings.OPTIONS[1]);
            }

        }
    }


    @SpirePatch(
            clz = GoldenIdolEvent.class,
            method = "<ctor>"
    )
    public static class GoldenIdolConstructior {

        public static void Postfix(GoldenIdolEvent __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int dmg = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(AbstractDungeon.ascensionLevel >= 15) {
                        dmg += ps.maxHealth * 0.35f;
                    } else {
                        dmg += ps.maxHealth * 0.25f;
                    }
                }
                dmg /= ProblemSolver68.problemSolverPlayer.size();
                ReflectionHacks.setPrivate(__instance, GoldenIdolEvent.class, "damage", dmg);
            }

        }
    }
}
