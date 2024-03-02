package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;

public class MushroomsPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Mushrooms");
    }


    @SpirePatch(
            clz = Mushrooms.class,
            method = "buttonEffect"
    )
    public static class MushroomsHpHeal {
        @SpireInsertPatch(
                rloc = 36
        )
        public static void Insert(Mushrooms __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int heal_amt = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    heal_amt += ps.maxHealth* 0.25F;
                }
                heal_amt /= ProblemSolver68.problemSolverPlayer.size();
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        ps.heal(heal_amt);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = Mushrooms.class,
            method = "<ctor>"
    )
    public static class MushroomsConstructior {
        public static void Postfix(Mushrooms __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int heal_amt = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    heal_amt += ps.maxHealth* 0.25F;
                }
                heal_amt /= ProblemSolver68.problemSolverPlayer.size();
                __instance.roomEventText.removeDialogOption(1);
                __instance.roomEventText.addDialogOption(strings.OPTIONS[0] + heal_amt + Mushrooms.OPTIONS[2],  CardLibrary.getCopy("Parasite"));
            }
        }
    }
}
