package BlueArchive_ProblemSolver.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;


public class GameActionManagerPatch {

    public static int deadThisCombat = 0;
    public static int tacticalChallengeCount = 0;
    public static int evildeedThisTurn = 0;
    public static int increaseMercenaryMaxHP = 0;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {

            evildeedThisTurn = 0;
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ThisCombatClearPatch {
        public static void Postfix(GameActionManager __instance)
        {
            evildeedThisTurn = 0;
            deadThisCombat = 0;
            increaseMercenaryMaxHP = 0;
            tacticalChallengeCount = 0;
        }
    }

}
