package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.TheLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class TheLibraryPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:TheLibrary");
    }

    @SpirePatch(
            clz = TheLibrary.class,
            method = "buttonEffect"
    )
    public static class TheLibraryHeal {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(TheLibrary __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int healAmt = ReflectionHacks.getPrivate(__instance, TheLibrary.class, "healAmt");;
                for (AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        ps.heal(healAmt,true);
                    }
                }
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "heal");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = TheLibrary.class,
            method = "<ctor>"
    )
    public static class TheLibraryConstructior {
        public static void Postfix(TheLibrary __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int heal_amt = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if (AbstractDungeon.ascensionLevel >= 15) {
                        heal_amt += ps.maxHealth * 0.2F;
                    } else {
                        heal_amt += ps.maxHealth * 0.33F;
                    }
                }
                heal_amt /= ProblemSolver68.problemSolverPlayer.size();
                ReflectionHacks.setPrivate(__instance, TheLibrary.class, "healAmt", heal_amt);
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[0] + heal_amt + TheLibrary.OPTIONS[2]);
            }
        }
    }
}
