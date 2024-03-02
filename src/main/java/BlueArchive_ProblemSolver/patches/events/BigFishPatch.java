package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.BigFish;
import com.megacrit.cardcrawl.localization.EventStrings;

public class BigFishPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:BigFish");
    }


    @SpirePatch(
            clz = BigFish.class,
            method = "buttonEffect"
    )
    public static class BigFishHpHeal {
        @SpireInsertPatch(
                rloc = 5
        )
        public static void Insert(BigFish __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        int healAmt = (Integer) ReflectionHacks.getPrivate(__instance, BigFish.class, "healAmt");
                        ps.heal(healAmt);
                    }
                }
            }
        }
    }

    /*public static class BigFishMaxHPUp {

        @SpireInsertPatch(
                rloc = 10
        )
        public static SpireReturn Insert(BigFish __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    ps.increaseMaxHp(3, true);
                }
                __instance.imageEventText.updateBodyText(BigFish.DESCRIPTIONS[2]);
                AbstractEvent.logMetricMaxHPGain("Big Fish", "Donut", 3);
                __instance.imageEventText.clearAllDialogs();
                __instance.imageEventText.setDialogOption(BigFish.OPTIONS[5]);
                ReflectionHacks.setPrivate(__instance, BigFish.class, "screen", Enum.valueOf(BigFish.CurScreen.class, "RESULT"));
                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }*/
    @SpirePatch(
            clz = BigFish.class,
            method = "buttonEffect"
    )
    public static class BigFishMaxHPUp {

        @SpireInsertPatch(
                rloc = 11
        )
        public static void Insert(BigFish __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ps != AbstractDungeon.player) {
                        ps.increaseMaxHp(3, true);
                    } else {
                        ps.decreaseMaxHealth(2);
                    }
                }
            }
        }
    }


    @SpirePatch(
            clz = BigFish.class,
            method = "<ctor>"
    )
    public static class BigFishConstructior {
        public static void Postfix(BigFish __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int heal_amt = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    heal_amt += ps.maxHealth/3;
                }
                heal_amt /= ProblemSolver68.problemSolverPlayer.size();
                ReflectionHacks.setPrivate(__instance, BigFish.class, "healAmt", heal_amt);
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + heal_amt + BigFish.OPTIONS[1]);
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[1] + 3 + BigFish.OPTIONS[3]);
            }
        }
    }
}
