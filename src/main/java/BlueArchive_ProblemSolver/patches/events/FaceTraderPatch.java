package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class FaceTraderPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:FaceTrader");
    }

    @SpirePatch(
            clz = FaceTrader.class,
            method = "buttonEffect"
    )
    public static class FaceTraderDamage {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(FaceTrader __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int damage = ReflectionHacks.getPrivate(__instance, FaceTrader.class, "damage");
                ProblemSolver68.damageAll(damage);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "damage");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = FaceTrader.class,
            method = "buttonEffect"
    )
    public static class FaceTraderDescription {
        @SpireInsertPatch(
                rloc = 8
        )
        public static void Insert(FaceTrader __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int damage = ReflectionHacks.getPrivate(__instance, FaceTrader.class, "damage");
                int goldReward = ReflectionHacks.getPrivate(__instance, FaceTrader.class, "goldReward");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + damage + FaceTrader.OPTIONS[5] + goldReward + FaceTrader.OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = FaceTrader.class,
            method = "<ctor>"
    )
    public static class FaceTraderConstructior {
        public static void Postfix(FaceTrader __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int damage = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    damage += ps.maxHealth * 0.1F;
                }
                damage /= ProblemSolver68.problemSolverPlayer.size();

                ReflectionHacks.setPrivate(__instance, FaceTrader.class, "damage", damage);
            }
        }
    }
}
