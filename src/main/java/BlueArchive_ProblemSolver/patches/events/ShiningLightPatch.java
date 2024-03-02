package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.GoldenWing;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.events.exordium.ShiningLight;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;

public class ShiningLightPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:ShiningLight");
    }


    @SpirePatch(
            clz = ShiningLight.class,
            method = "buttonEffect"
    )
    public static class ShiningLightHpHeal {
        @SpireInsertPatch(
                rloc = 8
        )
        public static void Insert(ShiningLight __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if (AbstractDungeon.player instanceof ProblemSolver68) {
                    int dmg = (Integer) ReflectionHacks.getPrivate(__instance, ShiningLight.class, "damage");
                    ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
                }
            }
        }
    }

    @SpirePatch(
            clz = ShiningLight.class,
            method = "<ctor>"
    )
    public static class ShiningLightConstructior {
        public static void Postfix(ShiningLight __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int deal = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(AbstractDungeon.ascensionLevel >= 15) {
                        deal += ps.maxHealth* 0.3F;
                    } else {
                        deal += ps.maxHealth* 0.2F;
                    }
                }
                deal /= ProblemSolver68.problemSolverPlayer.size();
                ReflectionHacks.setPrivate(__instance, ShiningLight.class, "damage", deal);

                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + deal + ShiningLight.OPTIONS[1]);
            }
        }
    }
}
