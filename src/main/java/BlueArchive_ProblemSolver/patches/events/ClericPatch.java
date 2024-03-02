package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.localization.EventStrings;

public class ClericPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Cleric");
    }


    @SpirePatch(
            clz = Cleric.class,
            method = "buttonEffect"
    )
    public static class ClericHpHeal {
        @SpireInsertPatch(
                rloc = 5
        )
        public static void Insert(Cleric __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        int healAmt = (Integer) ReflectionHacks.getPrivate(__instance, Cleric.class, "healAmt");
                        ps.heal(healAmt);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = Cleric.class,
            method = "<ctor>"
    )
    public static class ClericConstructior {
        public static void Postfix(Cleric __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int gold = AbstractDungeon.player.gold;
                if (gold >= 35) {
                    int heal_amt = 0;
                    for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                        heal_amt += ps.maxHealth* 0.25F;
                    }
                    heal_amt /= ProblemSolver68.problemSolverPlayer.size();
                    ReflectionHacks.setPrivate(__instance, Cleric.class, "healAmt", heal_amt);
                    __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + heal_amt + Cleric.OPTIONS[8], gold < 35);
                }
            }
        }
    }
}
